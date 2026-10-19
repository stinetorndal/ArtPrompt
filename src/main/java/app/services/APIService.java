package app.services;

import app.exceptions.ApiException;
import app.exceptions.RetryableApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class APIService {

    private static final Logger logger = LoggerFactory.getLogger(APIService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public String readAPI(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();

            if (response.statusCode() == 200) {
                return response.body();
            }
            logger.warn("Kald til {} gav status {}", url, status);
            if (status == 429 || status >= 500) {
                throw new RetryableApiException("Ekstern tjeneste midlertidigt utilgængelig");
            }
            throw new ApiException(500, "Kunne ikke hente data fra ekstern tjeneste");
        } catch (ApiException e) {
            throw e;                                  // Egne fejl passerer uændret
        } catch (IOException e) {
            logger.warn("Netværksfejl eller timeout for {}: {}", url, e.getMessage());
            throw new RetryableApiException("Netværksfejl eller timeout");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(500, "Kaldet blev afbrudt");
        }
    }

    public <T> T convertFromJson(String json, Class<T> tClass) {
        try {
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException e) {
            logger.error("Kunne ikke konvertere JSON til klasse {}: {}", tClass.getSimpleName(), e.getMessage());
            throw new ApiException(500, "Fejl ved læsning af data fra serveren");
        }
    }

    public <T> T fetchAndConvert(String url, Class<T> tClass) {
        String json = readAPI(url);
        return convertFromJson(json, tClass);
    }
}