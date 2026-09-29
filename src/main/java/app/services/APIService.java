package app.services;

import app.exceptions.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIService {

    private static final Logger logger = LoggerFactory.getLogger(APIService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String readAPI(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                //Gemmes i logfil:
                logger.error("Fejl ved kald til URL {}: status {}", url, response.statusCode());
                throw new ApiException(500, "Kunne ikke hente data fra ekstern tjeneste");
            }
            return response.body();
        } catch (Exception e) {
            //Gem præcise fejl i logfil:
            logger.error("Netværksfejl til URL {}: {}", url, e.getMessage());
            throw new ApiException(500, "Der opstod en netværksfejl");
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