package app.services;

import app.dtos.unsplash.PhotoDTO;
import app.dtos.unsplash.PhotographerDTO;
import app.dtos.unsplash.PictureUrlDTO;
import app.dtos.unsplash.SearchDTO;
import app.entities.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UnsplashServiceTest {

    private APIService apiServiceMock;
    private UnsplashService unsplashService;

    @BeforeEach
    void setup() {
        apiServiceMock = Mockito.mock(APIService.class);
        unsplashService = new UnsplashService(apiServiceMock);
    }

    @Test
    @DisplayName("Returnerer billeder fra Unsplash for en given farve")
    void getPicturesWithPickedColor() {
        // Arrange
        PhotoDTO photo = new PhotoDTO("id_1", new PhotographerDTO(), new PictureUrlDTO());
        SearchDTO searchDTO = new SearchDTO(Collections.nCopies(10, photo));

        when(apiServiceMock.fetchAndConvert(anyString(), eq(SearchDTO.class)))
                .thenReturn(searchDTO);

        // Act
        List<PhotoDTO> result = unsplashService.getPicturesWithPickedColor(Color.YELLOW);

        // Assert
        assertEquals(10, result.size());
        assertEquals("id_1", result.get(0).getId());
        assertNotNull(result.get(0).getUser());
        assertNotNull(result.get(0).getUrl());
    }
}