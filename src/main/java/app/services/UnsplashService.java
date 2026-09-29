package app.services;

import app.dtos.unsplash.PhotoDTO;
import app.dtos.unsplash.SearchDTO;
import app.entities.Color;
import app.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnsplashService {

    private static final Logger logger = LoggerFactory.getLogger(UnsplashService.class);
    private APIService apiService = new APIService();
    private Random random = new Random();


    public List<PhotoDTO> getPicturesWithPickedColor(Color color) {

        //Konverterer Enum (fx YELLOW) til små bogstaver
        String colorString = color.name().toLowerCase();

        //Unsplash cacher så man kan ikke bruge random direkte. Derfor er jeg nøndt til at bruge Random-klassen og
        //vælge et vilkårligt sidenummer mellem 1-x hver gang. (kan ændres efter behov)
        int randomPage = random.nextInt(50) + 1;
           //Unsplash sender som standard 10 billeder
        String url = "https://api.unsplash.com/search/photos?query="
                + colorString
                + "&color=" + colorString
                + "&page=" + randomPage
                + "&per_page=10"
                + "&order_by=random"
                + "&client_id=" + System.getenv("API_KEY");

        try {
            //Mapper til SearchDTO da json-response er et objekt
            SearchDTO response = apiService.fetchAndConvert(url, SearchDTO.class);
            if (response != null && response.getSearchResult() != null) {
                logger.info("Hentede {} billeder fra Unsplash for farven {}", response.getSearchResult().size(), colorString);
                return response.getSearchResult();
            }
        } catch (Exception e) {
            logger.error("Fejl ved netværkskald til URL {}: {}", url, e.getMessage());
            throw new ApiException(500, "Fejl ved hentning af billeder");
        }
        logger.warn("Svaret fra Unsplash indeholdt ingen billeder for farven {}", colorString);
        return new ArrayList<>();

    }

}
