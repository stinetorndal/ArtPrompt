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
    private final APIService apiService;
    private Random random = new Random();

    // Bruges i den rigtige app
    public UnsplashService() {
        this(new APIService());
    }

    // Konstruktør til unit-testing (dependency injection)
    public UnsplashService(APIService apiService) {
        this.apiService = apiService;
    }

    public List<PhotoDTO> getPicturesWithPickedColor(Color color) {
        String colorString = color.name().toLowerCase();

        //Unsplash cacher så man kan ikke bruge random direkte. Derfor er jeg nøndt til at bruge Random-klassen og
        //vælge et vilkårligt sidenummer mellem 1-x hver gang. (kan ændres efter behov)
        int randomPage = random.nextInt(50) + 1;

        List<PhotoDTO> photos = fetchPage(colorString, randomPage);
        if (photos.isEmpty() && randomPage != 1) {
            logger.info("Side {} var tom for farven {}, prøver side 1", randomPage, colorString);
            photos = fetchPage(colorString, 1);
        }
        return photos;
    }

    // Fetcher en side med billeder (10) fra Unsplash. Returner tom liste hvis siden var tom fx orange)
    private List<PhotoDTO> fetchPage(String colorString, int page) {
        String url = buildSearchUrl(colorString, page);
        try {
            //Mapper til SearchDTO da json-response er et objekt
            SearchDTO response = apiService.fetchAndConvert(url, SearchDTO.class);
            if (response != null && response.getSearchResult() != null) {
                logger.info("Hentede {} billeder fra Unsplash for farven {}",
                        response.getSearchResult().size(), colorString, page);
                return response.getSearchResult();
            }
        } catch (Exception e) {
            logger.error("Fejl ved hentning af billeder fra Unsplash for farven {}: {}", colorString, e.getMessage());
            throw new ApiException(500, "Fejl ved hentning af billeder");
        }
        logger.warn("Svaret fra Unsplash indeholdt ingen billeder for farven {}", colorString);
        return new ArrayList<>();
    }

    private String buildSearchUrl(String colorString, int page) {
        //Unsplash sender som standard 10 billeder
        return "https://api.unsplash.com/search/photos?query="
                + colorString
                + "&color=" + colorString
                + "&page=" + page
                + "&per_page=10"
                + "&order_by=random"
                + "&client_id=" + System.getenv("API_KEY");
    }
}
