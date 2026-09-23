package app.services;

import app.dtos.unsplash.PhotoDTO;
import app.dtos.unsplash.SearchDTO;
import app.entities.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnsplashService {
    private APIService apiService = new APIService();


    public List<PhotoDTO> getPicturesWithPickedColor(Color color) {

        //Konverterer Enum (fx YELLOW) til små bogstaver
        String colorString = color.name().toLowerCase();
           //Unsplash sender som standard 10 billeder
        String url = "https://api.unsplash.com/search/photos?query="
                + colorString
                + "&color=" + colorString
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
