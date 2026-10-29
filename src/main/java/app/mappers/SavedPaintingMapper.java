package app.mappers;

import app.dtos.rijksmuseum.PaintingDTO;
import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.ArtistCategory;
import app.entities.SavedPainting;
import app.entities.User;
import app.entities.Painting;

//Oversætter objekt fra JPA til API og omvendt
public class SavedPaintingMapper {

    public SavedPaintingDTO fromApiDTO (PaintingDTO paintingDTO, ArtistCategory artistCategory) {
        if (paintingDTO == null) return null;

        //Nogle malerier har ikke fået lagt billede ind endnu, derfor null så koden fortsætter
        String url = (paintingDTO.getImage() != null) ? paintingDTO.getImage().getContentUrl() : null;
        //Null så koden kan køre videre hvis man glemmer at sende kategori med
        String artist  = (artistCategory != null) ? artistCategory.name(): "Unknown Artist";

        return new SavedPaintingDTO(
                paintingDTO.getId(),
                paintingDTO.getTitle(),
                artist,
                url,
                parseYear(paintingDTO.getDateCreated()),
                null
        );
    }

        //Returnerer null hvis årstal mangler eller ikke kan læses som heltal, så koden kan køre videre
        private Integer parseYear(String yearString) {
        if (yearString == null) return null;
        try {
            return Integer.parseInt(yearString.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public SavedPaintingDTO toDTO(SavedPainting painting) {
        if (painting == null) return null;
        Long userId = (painting.getUser() != null) ? painting.getUser().getId() : null; //undgår nullpointerexception

    //Når DTO kaldes skal parametre stå i præcis samme rækkefølge som i DTO
        return new SavedPaintingDTO(
                painting.getExternalId(),
                painting.getTitle(),
                painting.getArtist(),
                painting.getUrl(),
                painting.getYear(),
                userId
        );
    }
    //Konverterer katalogmaleri fra Rijksmuseum  til DTO. Ingen bruger så = null
    public SavedPaintingDTO fromCatalogPainting(Painting painting) {
        if (painting == null) return null;

        return new SavedPaintingDTO(
                painting.getExternalId(),
                painting.getTitle(),
                painting.getArtist(),
                painting.getUrl(),
                painting.getYear(),
                null
        );
    }

    public SavedPainting toEntity(SavedPaintingDTO dto, User user) {
        if (dto == null) return null;

        SavedPainting painting = new SavedPainting();
        painting.setExternalId(dto.getExternalId());
        painting.setTitle(dto.getTitle());
        painting.setArtist(dto.getArtist());
        painting.setUrl(dto.getUrl());
        painting.setYear(dto.getYear());
        painting.setUser(user); // JPA bruger kun user.getId() til fremmednøglen i DB
        return painting;

    }
}
