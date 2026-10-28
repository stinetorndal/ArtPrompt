package app.controllers;

import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

// Tests that HealthController answers 200 when the database responds and 503 when it does not
class HealthControllerTest {

    private EntityManagerFactory emfMock;
    private EntityManager emMock;
    private Query queryMock;
    private Context ctxMock;
    private HealthController controller;

    // Creates mocks of the database and of Javalin's Context
    @BeforeEach
    void setUp() {
        emfMock = mock(EntityManagerFactory.class);
        emMock = mock(EntityManager.class);
        queryMock = mock(Query.class);
        ctxMock = mock(Context.class);
        controller = new HealthController(emfMock);

        when(ctxMock.status(anyInt())).thenReturn(ctxMock);
    }

    // A database that answers the query gives status 200 and UP
    @Test
    void testCheckReturns200WhenDatabaseAnswers() {
        //Arrange
        when(emfMock.createEntityManager()).thenReturn(emMock);
        when(emMock.createNativeQuery("SELECT 1")).thenReturn(queryMock);
        when(queryMock.getSingleResult()).thenReturn(1);
        //Act
        controller.check(ctxMock);
        //Assert
        verify(ctxMock).status(200);
        verify(ctxMock).json(Map.of("status", "UP"));
    }

    // A database that cannot be reached gives status 503 and DOWN
    @Test
    void testCheckReturns503WhenDatabaseIsDown() {
        when(emfMock.createEntityManager()).thenThrow(new RuntimeException("Connection refused"));

        controller.check(ctxMock);

        verify(ctxMock).status(503);
        verify(ctxMock).json(Map.of("status", "DOWN"));
    }
}