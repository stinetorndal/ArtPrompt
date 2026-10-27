package app.controllers;

import app.exceptions.ApiException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//Denne klasse har til formål at begrænse antallet af requests per minut, skal undgå DoS/DDoS
public class RateLimitController {

    Logger logger = LoggerFactory.getLogger(RateLimitController.class);
    //En bucket til at rumme antal kald oprettes her
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();


    //Hjælpemetode: Her oprettes en ny Bucket for en klient
    private Bucket createNewBucket() {
        //Genopfyld 20 tokens hvert minut
        Refill refill = Refill.intervally(20, Duration.ofMinutes(1));
        //Sæt max kapacitet til 20 tokens
        Bandwidth limit = Bandwidth.classic(20, refill);
        //Byg og returnér færdige bucket
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    //Metode til at håndtere den faktiske IP fordi alle beøgende deler bucket. Det bliver meget hurtigt 20 kald
    private String getClientIp(Context ctx) {
        String forwarded = ctx.header("X-Forwarded-For");
        if (forwarded == null || forwarded.isBlank()) {
            return ctx.ip();
        }
        //Headeren kan "holde" en liste fx "client, proxy". Første er klient
        return forwarded.split(",")[0].trim();
    }


    //Denne metode bruges i Javalins before-filter
    public void handleRateLimit(Context ctx) {
        //Find klientens IP-adresse
        String clientIp = getClientIp(ctx);
        // Hent eksisterende bucket for denne IP, eller opret en ny hvis den ikke findes endnu
        Bucket bucket = buckets.computeIfAbsent(clientIp, ip -> createNewBucket());
        // Hvis bucket er tom for tokens
        if (!bucket.tryConsume(1)) {
            // Hvis der ikke er flere tokens tilbage, kastes en HTTP 429
            logger.warn("Rate limit overskredet for IP: {} på route: {}", clientIp, ctx.path());
            throw new ApiException(429, "Too Many Requests - Du har overskredet grænsen på 20 kald pr. minut.");
        }
    }
}
