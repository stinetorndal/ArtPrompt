package app.controllers;

import app.exceptions.ApiException;
import app.utils.RequestUtil;
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

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    //Hjælpemetode: Her oprettes en ny Bucket for en klient, der genopfylder hvert minut. Max 20 tokens
    private Bucket createNewBucket() {

        Refill refill = Refill.intervally(20, Duration.ofMinutes(1));

        Bandwidth limit = Bandwidth.classic(20, refill);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    //Denne metode bruges i Javalins before-filter
    public void handleRateLimit(Context ctx) {

        String clientIp = RequestUtil.getClientIp(ctx);
        // Hent eksisterende bucket for denne IP, eller opret en ny hvis den ikke findes endnu
        Bucket bucket = buckets.computeIfAbsent(clientIp, ip -> createNewBucket());

        if (!bucket.tryConsume(1)) {

            logger.warn("Rate limit overskredet for IP: {} på route: {}", clientIp, ctx.path());
            throw new ApiException(429, "Too Many Requests - Du har overskredet grænsen på 20 kald pr. minut.");
        }
    }
}
