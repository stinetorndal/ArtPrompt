package app.controllers;

import app.dtos.savedimages.SavedImageDTO;
import app.services.SavedImageService;
import io.javalin.http.Context;
import app.exceptions.ApiException;
import app.routes.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

//Formålet er at teste at Controller kalder Service når der kommer Javalin-kald. Og sender korrekt statuskode
class SavedImageControllerTest {

    private SavedImageService serviceMock;
    private SavedImageController controller;
    private Context ctxMock;

    @BeforeEach
    void setUp() {
        //Mock-udgave af servicelag
        serviceMock = mock(SavedImageService.class);
        controller = new SavedImageController(serviceMock);
        //Mock-udgave af Javalin Context
        ctxMock = mock(Context.class);
    }

    @Test
    void testSaveImage() {
        //Arrange
        SavedImageDTO dto = new SavedImageDTO(
                null, "https://url.com", "ext1", "Titel", "Kilde", 1L);


        when(ctxMock.bodyAsClass(SavedImageDTO.class)).thenReturn(dto);
        when(ctxMock.<Long>attribute("userId")).thenReturn(1L);
        when(serviceMock.saveImageForUser(dto)).thenReturn(dto);
        when(ctxMock.status(201)).thenReturn(ctxMock);

        //Act
        controller.saveImage(ctxMock);

        //Assert
        verify(ctxMock).status(201);
        verify(ctxMock).json(dto);
    }

    @Test
    void testGetSavedImagesByUser() {
        //Arrange
        when(ctxMock.pathParam("userId")).thenReturn("1");
        when(ctxMock.<Long>attribute("userId")).thenReturn(1L);
        when(ctxMock.<Role>attribute("userRole")).thenReturn(Role.USER);
        when(serviceMock.getSavedImagesByUserId(1L)).thenReturn(List.of());
        when(ctxMock.status(200)).thenReturn(ctxMock);

        //Act
        controller.getSavedImagesByUser(ctxMock);

        //Assert
        verify(ctxMock).status(200);
    }

    @Test
    void testGetSavedImagesForOtherUserIsForbidden() {
        //Arrange
        when(ctxMock.pathParam("userId")).thenReturn("2");
        when(ctxMock.<Long>attribute("userId")).thenReturn(1L);
        when(ctxMock.<Role>attribute("userRole")).thenReturn(Role.USER);

        ApiException exception = assertThrows(ApiException.class,
                () -> controller.getSavedImagesByUser(ctxMock));

        assertEquals(403, exception.getCode());
        verifyNoInteractions(serviceMock);
    }
}