package app.mappers;

import app.dtos.savedimages.SavedImageDTO;
import app.entities.SavedImage;
import app.entities.User;

public class SavedImageMapper {

    public SavedImageDTO toDTO(SavedImage image) {
        if (image == null) return null;

        User user = image.getUser();
        return new SavedImageDTO(
                image.getId(),
                image.getUrl(),
                image.getExternalId(),
                image.getTitle(),
                image.getSource(),
                user != null ? user.getId() : null
        );
    }

    public SavedImage toEntity(SavedImageDTO dto, User user) {
        if (dto == null) return null;

        SavedImage image = new SavedImage();
        image.setId(dto.getId());
        image.setUrl(dto.getUrl());
        image.setExternalId(dto.getExternalId());
        image.setTitle(dto.getTitle());
        image.setSource(dto.getSource());
        image.setUser(user);
        return image;
    }
}