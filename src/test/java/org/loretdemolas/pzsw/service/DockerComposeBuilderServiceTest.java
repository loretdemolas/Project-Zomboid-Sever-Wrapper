package org.loretdemolas.pzsw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DockerComposeBuilderServiceTest {

    private DockerComposeBuilderService dockerComposeBuilderService;

    @BeforeEach
    public void setUp() {
        dockerComposeBuilderService = new DockerComposeBuilderService();
    }

    @Test
    public void testBuildDockerComposeFile() {
        // Create Docker services
        DockerComposeBuilderService.DockerService service1 = new DockerComposeBuilderService.DockerService()
                .setBuildContext(".")
                .setDockerfile("docker/zomboid-dedicated-server.Dockerfile")
                .addBuildArg("UID=${UID:-1000}")
                .addBuildArg("GID=${GID:-1000}")
                .addEnvironmentVariable("KEY1", "VALUE1")
                .addPort("8080:8080")
                .addVolume("/path/to/volume1");

        DockerComposeBuilderService.DockerService service2 = new DockerComposeBuilderService.DockerService()
                .setBuildContext(".")
                .setDockerfile("docker/another-service.Dockerfile")
                .addBuildArg("UID=${UID:-1001}")
                .addBuildArg("GID=${GID:-1001}")
                .addEnvironmentVariable("KEY2", "VALUE2;VALUE3;VALUE4")
                .addPort("9090:9090")
                .addVolume("/path/to/volume2");

        // Add services to DockerComposeBuilderService
        dockerComposeBuilderService.addService(service1)
                .addService(service2)
                .addVolume("/shared-data")
                .addNetwork("network1");

        // Build Docker Compose file
        String dockerComposeFile = dockerComposeBuilderService.build();

        // Print the resulting Docker Compose file
        System.out.println("Generated Docker Compose file:\n" + dockerComposeFile);

        // You can add assertions to validate the generated Docker Compose file if needed
        // For example, you can check if certain parts of the Docker Compose file are present
        // Assertions.assertTrue(dockerComposeFile.contains("services:"));
        // Assertions.assertTrue(dockerComposeFile.contains("volumes:"));
        // Assertions.assertTrue(dockerComposeFile.contains("networks:"));
    }
}
