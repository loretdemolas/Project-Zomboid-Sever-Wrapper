package org.loretdemolas.pzsw.controller;

import org.loretdemolas.pzsw.dto.DockerComposeRequestDTO;
import org.loretdemolas.pzsw.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/server")
public class ServerController {

    private final ServerService serverService;

    @Autowired
    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startComposeFile() {
        boolean composeExists = serverService.checkDockerComposeFileExists();
        if (!composeExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Docker Compose file not found.");
        }

        boolean isRunning = serverService.isServiceRunning();
        if (isRunning) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Docker Compose is already running.");
        }

        serverService.startComposeFile();
        return ResponseEntity.ok("Started Docker Compose file.");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopComposeFile() {
        serverService.stopComposeFile();
        return ResponseEntity.ok("Stopped Docker Compose file.");
    }

    @PostMapping("/restart")
    public ResponseEntity<String> restartComposeFile() {
        boolean composeExists = serverService.checkDockerComposeFileExists();
        if (!composeExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Docker Compose file not found.");
        }

        boolean isRunning = serverService.isServiceRunning();
        if (!isRunning) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Docker Compose is not running.");
        }

        serverService.restartComposeFile();
        return ResponseEntity.ok("Restarted Docker Compose file.");
    }

    @PostMapping("/create")
    public ResponseEntity<String> createDockerComposeFile(@RequestBody DockerComposeRequestDTO requestDTO) {
        try {
            serverService.createDockerComposeFile(requestDTO);
            return ResponseEntity.ok("Created Docker Compose file successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(STR."Failed to create Docker Compose file: \{e.getMessage()}");
        }
    }
}