package app.controllers;

import app.dtos.savedimages.SavedImageDTO;
import app.services.SavedImageService;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

//Formålet er at teste atController kalder Service når der kommer Javalin-kald. Og sender korrekt statuskode
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
        //ARRANGE: Definer Mockdatas adfærd
        SavedImageDTO dto = new SavedImageDTO(
                null, "https://url.com", "ext1", "Titel", "Kilde", 1L);

        //Simulerer brugerdto med i requestbody
        when(ctxMock.bodyAsClass(SavedImageDTO.class)).thenReturn(dto);
        //Service returnerer samme dto
        when(serviceMock.saveImageForUser(dto)).thenReturn(dto);
        //Sikrer fx ctx.status .json virker på mock
        when(ctxMock.status(201)).thenReturn(ctxMock);

        //ACT: Udfør i controller
        controller.saveImage(ctxMock);

        //ASSERT: Verificér at controlleren satte status 201 og returnerede JSON
        verify(ctxMock).status(201);
        verify(ctxMock).json(dto);
    }

    @Test
    void testGetSavedImagesByUser() {
        //ARRANGE: imuler URL indeholder ID "1"
        when(ctxMock.pathParam("userId")).thenReturn("1");
        when(serviceMock.getSavedImagesByUserId(1L)).thenReturn(List.of());
        when(ctxMock.status(200)).thenReturn(ctxMock);

        //ACT: Udfør Controller-metode
        controller.getSavedImagesByUser(ctxMock);

        //ASSERT: Status 200
        verify(ctxMock).status(200);
    }
}