package app.services;

import app.dtos.unsplash.PhotoDTO;
import app.dtos.unsplash.SearchDTO;
import app.entities.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnsplashService {
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
                return response.getSearchResult();
            }
        } catch (Exception e) {
            throw new RuntimeException("Fejl ved hentning af billeder" + e.getMessage());
        }
        return new ArrayList<>();

    }

}
