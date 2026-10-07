package app.mappers;

import app.dtos.savedpaintings.SavedPaintingDTO;
import app.entities.SavedPainting;
import app.entities.User;

public class SavedPaintingMapper {

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
