package app.services;

import app.dtos.rijksmuseum.PaintingDTO;
import app.dtos.rijksmuseum.PaintingReferenceDTO;
import app.dtos.rijksmuseum.SearchPainterDTO;
import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.ArtistCategory;
import app.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;



public class RijksmuseumService {
    private static final Logger logger = LoggerFactory.getLogger(RijksmuseumService.class);
    private APIService apiService = new APIService();

    public List<String> fetchPaintingsIdsByArtist(ArtistCategory artist) {
        String query = artist.getApiQuery();
        String url = "https://data.rijksmuseum.nl/search/collection?creator=" + query.replace(" ", "%20");

        try {
            //Mapper til SearchDTO da json-response er et objekt
            SearchPainterDTO response = apiService.fetchAndConvert(url, SearchPainterDTO.class);
            List<String> ids = new ArrayList<>();

            if (response != null && response.getSearchResults() != null) {
                for (PaintingReferenceDTO ref : response.getSearchResults()) {
                    if (ref.getId() != null) {
                        ids.add(ref.getId());
                    }
                }
            }
            logger.info("Hentede {} maleri-IDér for kunstneren {}", ids.size(), artist.name());
            return ids;
        }catch (Exception e) {
            logger.error("Fejl ved hentning af maleri-IDér for {}:{}", artist.name(), e.getMessage());
            throw new ApiException(500, "Fejl ved hentning af maleri-ID'er fra Rijksmuseum");
        }
        }

     //Hent detaljer for ET billede
    public PaintingDTO fetchPaintingDetailsById (String objectId){
        String url = objectId + "?_profile=la-framed";
        try {
            PaintingDTO response = apiService.fetchAndConvert(url, PaintingDTO.class);
            return  response;
        } catch (Exception e){
            logger.error("Fejl ved hentning af detaljer for ID {}: {}", objectId, e.getMessage());
            throw new ApiException(500, "Fejl ved hentning af maleridetaljer");
        }
    }
}
