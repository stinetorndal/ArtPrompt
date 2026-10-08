package app.mappers;

import app.dtos.rijksmuseum.PaintingDTO;
import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.ArtistCategory;
import app.entities.SavedPainting;
import app.entities.User;

public class SavedPaintingMapper {

    //Konverterer Rijksmuseums API-svar til  det JSON jeg vil sende ud i mit eget API
    public SavedPaintingDTO fromApiDTO (PaintingDTO paintingDTO, ArtistCategory artistCategory) {
        if (paintingDTO == null) return null;

        //Nogle malerier har ikke fået lagt billede ind endnu, derfor null så koden fortsætter
        String url = (paintingDTO.getImage() != null) ? paintingDTO.getImage().getContentUrl() : null;
        //Null så koden kan køre videre hvis man glemmer at sende kategori med
        String artist  = (artistCategory != null) ? artistCategory.name(): "Unknown Artist";

        return new SavedPaintingDTO(
                paintingDTO.getId(),
                url,
                paintingDTO.getTitle(),
                artist,
                parseYear(paintingDTO.getDateCreated()),
                null
        );
    }
    //Behøver ikke logger, det er bare forkert årstal
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
                painting.getUrl(),
                painting.getTitle(),
                painting.getArtist(),
                painting.getYear(),
                userId
        );
    }

    public SavedPainting toEntity(SavedPaintingDTO dto, User user) {
        if (dto == null) return null;

        SavedPainting painting = new SavedPainting();
        painting.setExternalId(dto.getExternalId());
        painting.setUrl(dto.getUrl());
        painting.setTitle(dto.getTitle());
        painting.setArtist(dto.getArtist());
        painting.setYear(dto.getYear());
        painting.setUser(user); // JPA bruger kun user.getId() til fremmednøglen i DB
        return painting;

    }
}
