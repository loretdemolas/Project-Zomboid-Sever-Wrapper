package org.loretdemolas.pzsw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.loretdemolas.pzsw.dto.DockerComposeRequestDTO;
import org.loretdemolas.pzsw.service.ServerService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServerControllerTest {

    @Mock
    private ServerService serverService;

    @InjectMocks
    private ServerController serverController;

    @BeforeEach
    void setUp() {
        // Reset any Mockito interactions before each test
        reset(serverService);
    }

    @Test
    public void testStartComposeFile_HappyPath() {
        // Mocking behavior
        when(serverService.checkDockerComposeFileExists()).thenReturn(true);
        when(serverService.isServiceRunning()).thenReturn(false);

        // Execute the controller method
        ResponseEntity<String> response = serverController.startComposeFile();

        // Verify the expected behavior
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Started Docker Compose file.", response.getBody());
        verify(serverService).startComposeFile();
    }

    @Test
    public void testStartComposeFile_FileNotFound() {
        // Mocking behavior
        when(serverService.checkDockerComposeFileExists()).thenReturn(false);

        // Execute the controller method
        ResponseEntity<String> response = serverController.startComposeFile();

        // Verify the expected behavior
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Docker Compose file not found.", response.getBody());
        verify(serverService, never()).startComposeFile(); // Ensure startComposeFile() was not called
    }

    @Test
    public void testStartComposeFile_AlreadyRunning() {
        // Mocking behavior
        when(serverService.checkDockerComposeFileExists()).thenReturn(true);
        when(serverService.isServiceRunning()).thenReturn(true);

        // Execute the controller method
        ResponseEntity<String> response = serverController.startComposeFile();

        // Verify the expected behavior
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Docker Compose is already running.", response.getBody());
        verify(serverService, never()).startComposeFile(); // Ensure startComposeFile() was not called
    }

    @Test
    public void testStopComposeFile_HappyPath() {
        // Execute the controller method
        ResponseEntity<String> response = serverController.stopComposeFile();

        // Verify the expected behavior
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Stopped Docker Compose file.", response.getBody());
        verify(serverService).stopComposeFile();
    }

    @Test
    public void testRestartComposeFile_HappyPath() {
        // Mocking behavior
        when(serverService.checkDockerComposeFileExists()).thenReturn(true);
        when(serverService.isServiceRunning()).thenReturn(true);

        // Execute the controller method
        ResponseEntity<String> response = serverController.restartComposeFile();

        // Verify the expected behavior
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Restarted Docker Compose file.", response.getBody());
        verify(serverService).restartComposeFile();
    }

    @Test
    public void testRestartComposeFile_FileNotFound() {
        // Mocking behavior
        when(serverService.checkDockerComposeFileExists()).thenReturn(false);

        // Execute the controller method
        ResponseEntity<String> response = serverController.restartComposeFile();

        // Verify the expected behavior
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Docker Compose file not found.", response.getBody());
        verify(serverService, never()).restartComposeFile(); // Ensure restartComposeFile() was not called
    }

    @Test
    public void testRestartComposeFile_NotRunning() {
        // Mocking behavior
        when(serverService.checkDockerComposeFileExists()).thenReturn(true);
        when(serverService.isServiceRunning()).thenReturn(false);

        // Execute the controller method
        ResponseEntity<String> response = serverController.restartComposeFile();

        // Verify the expected behavior
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Docker Compose is not running.", response.getBody());
        verify(serverService, never()).restartComposeFile(); // Ensure restartComposeFile() was not called
    }

    @Test
    public void testCreateDockerComposeFile_HappyPath() throws Exception {
        // Mocking behavior
        DockerComposeRequestDTO requestDTO = new DockerComposeRequestDTO(); // Create a valid request object
        doNothing().when(serverService).createDockerComposeFile(requestDTO);

        // Execute the controller method
        ResponseEntity<String> response = serverController.createDockerComposeFile(requestDTO);

        // Verify the expected behavior
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Created Docker Compose file successfully.", response.getBody());
        verify(serverService).createDockerComposeFile(requestDTO);
    }

    @Test
    public void testCreateDockerComposeFile_ExceptionThrown() throws IOException {
        // Mocking behavior
        DockerComposeRequestDTO requestDTO = new DockerComposeRequestDTO(); // Create a valid request object
        doThrow(new IOException("Mock Exception")).when(serverService).createDockerComposeFile(requestDTO);

        // Execute the controller method
        ResponseEntity<String> response = serverController.createDockerComposeFile(requestDTO);

        // Verify the expected behavior
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to create Docker Compose file: Mock Exception", response.getBody());
        verify(serverService).createDockerComposeFile(requestDTO);
    }
}
