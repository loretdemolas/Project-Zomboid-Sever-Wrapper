package org.loretdemolas.pzsw.service;

import org.loretdemolas.pzsw.config.ServiceConfigProperties;
import org.loretdemolas.pzsw.dto.DockerComposeRequestDTO;
import org.loretdemolas.pzsw.entity.Mod;
import org.loretdemolas.pzsw.entity.ServiceConfig;
import org.loretdemolas.pzsw.entity.WorkshopId;
import org.loretdemolas.pzsw.repository.ModRepository;
import org.loretdemolas.pzsw.repository.ServiceConfigRepository;
import org.loretdemolas.pzsw.repository.WorkshopIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServerService {
    private static final String DOCKER_COMPOSE_FILE_PATH = "./docker-compose.yml";

    private final ServiceConfigRepository serviceConfigRepository;
    private final ModRepository modRepository;
    private final WorkshopIdRepository workshopIdRepository;
    private final ServiceConfigProperties serviceConfigProperties;

    @Value("${SteamCMD}")
    private String SteamCMD;

    @Value("${steam}")
    private String steam;

    @Value("${ZomboidConfig}")
    private String ZomboidConfig;

    @Value("${ZomboidDedicatedServer}")
    private String ZomboidDedicatedServer;

    @Autowired
    public ServerService(ServiceConfigRepository serviceConfigRepository,
                         ModRepository modRepository,
                         WorkshopIdRepository workshopIdRepository,
                         ServiceConfigProperties serviceConfigProperties) {
        this.serviceConfigRepository = serviceConfigRepository;
        this.modRepository = modRepository;
        this.workshopIdRepository = workshopIdRepository;
        this.serviceConfigProperties = serviceConfigProperties;
    }

    public void createDockerComposeFile(DockerComposeRequestDTO request) throws IOException {
        DockerComposeBuilderService composeBuilder = new DockerComposeBuilderService();
        DockerComposeBuilderService.DockerService zomboidService = new DockerComposeBuilderService.DockerService();

        String modsAsString = modRepository.findByEnabledTrue().stream()
                .map(Mod::getModName)
                .collect(Collectors.joining(";"));

        String workshopIdAsString = workshopIdRepository.findByEnabledTrue().stream()
                .map(WorkshopId::getWorkshopId)
                .collect(Collectors.joining(";"));

        ServiceConfig serviceConfig = serviceConfigRepository.findById(1L).orElseGet(this::createDefaultServiceConfig);
        Map<String, String> envVariables = getStringStringMap(request, serviceConfig, modsAsString, workshopIdAsString);
        envVariables.forEach(zomboidService::addEnvironmentVariable);

        zomboidService
                .addPort(String.format("%s:%s/udp",
                        getValueOrDefault(request.getDefaultPort(), serviceConfig.getDefaultPort()),
                        getValueOrDefault(request.getDefaultPort(), serviceConfig.getDefaultPort())))
                .addPort(String.format("%s:%s/tcp", serviceConfig.getRconPort(), serviceConfig.getRconPort()));

        zomboidService
                .addVolume(ZomboidDedicatedServer)
                .addVolume(ZomboidConfig)
                .addVolume(steam)
                .addVolume(SteamCMD);

        composeBuilder.addService(zomboidService);
        composeBuilder.addVolume("ZomboidDedicatedServer:\n    external: true");
        composeBuilder.addVolume("ZomboidConfig:\n    external: true");
        composeBuilder.addVolume("steamCMD:\n    external: true");
        composeBuilder.addVolume("steam:\n    external: true");

        try (FileWriter fileWriter = new FileWriter(DOCKER_COMPOSE_FILE_PATH)) {
            fileWriter.write(composeBuilder.build());
        }
    }

    private ServiceConfig createDefaultServiceConfig() {
        ServiceConfig config = new ServiceConfig();
        config.setAdminUsername(serviceConfigProperties.getAdminUsername());
        config.setAdminPassword(serviceConfigProperties.getAdminPasswords());
        config.setRconPassword(serviceConfigProperties.getRconPassword());
        config.setResetID(serviceConfigProperties.getResetID());
        // Set other default values here if needed
        return config;
    }

    private static Map<String, String> getStringStringMap(DockerComposeRequestDTO request, ServiceConfig serviceConfig,
                                                          String modsAsString, String workshopIdAsString) {
        Map<String, String> envVariables = new HashMap<>();

        envVariables.put("ADMIN_PASSWORD", serviceConfig.getAdminPassword());
        envVariables.put("ADMIN_USERNAME", serviceConfig.getAdminUsername());
        envVariables.put("AUTOSAVE_INTERVAL", getValueOrDefault(request.getAutosaveInterval(), serviceConfig.getAutosaveInterval()));
        envVariables.put("BIND_IP", getValueOrDefault(request.getBindIp(), serviceConfig.getBindIp()));
        envVariables.put("DEFAULT_PORT", getValueOrDefault(request.getDefaultPort(), serviceConfig.getDefaultPort()));
        envVariables.put("GAME_VERSION", getValueOrDefault(request.getGameVersion(), serviceConfig.getGameVersion()));
        envVariables.put("GC_CONFIG", getValueOrDefault(request.getGcConfig(), serviceConfig.getGcConfig()));
        envVariables.put("MAP_NAMES", getValueOrDefault(request.getMapNames(), serviceConfig.getMapNames()));
        envVariables.put("MAX_PLAYERS", getValueOrDefault(request.getMaxPlayers(), serviceConfig.getMaxPlayers()));
        envVariables.put("MAX_RAM", getValueOrDefault(request.getMaxRam(), serviceConfig.getMaxRam()));
        envVariables.put("MOD_NAMES", modsAsString);
        envVariables.put("MOD_WORKSHOP_IDS", workshopIdAsString);
        envVariables.put("PAUSE_ON_EMPTY", getValueOrDefault(String.valueOf(request.isPauseOnEmpty()), String.valueOf(serviceConfig.isPauseOnEmpty())));
        envVariables.put("PUBLIC_SERVER", getValueOrDefault(String.valueOf(request.isPublicServer()), String.valueOf(serviceConfig.isPublicServer())));
        envVariables.put("RCON_PASSWORD", serviceConfig.getRconPassword());
        envVariables.put("RCON_PORT", serviceConfig.getRconPort());
        envVariables.put("SERVER_NAME", getValueOrDefault(request.getServerName(), serviceConfig.getServerName()));
        envVariables.put("SERVER_PASSWORD", getValueOrDefault(request.getServerPassword(), serviceConfig.getServerPassword()));
        envVariables.put("STEAM_VAC", getValueOrDefault(String.valueOf(request.isSteamVac()), String.valueOf(serviceConfig.isSteamVac())));
        envVariables.put("UDP_PORT", getValueOrDefault(request.getUdpPort(), serviceConfig.getUdpPort()));
        envVariables.put("USE_STEAM", getValueOrDefault(String.valueOf(request.isUseSteam()), String.valueOf(serviceConfig.isUseSteam())));
        envVariables.put("TZ", getValueOrDefault(request.getTimezone(), serviceConfig.getTz()));
        envVariables.put("RESETID", getValueOrDefault(request.getResetId(), serviceConfig.getResetID()));

        return envVariables;
    }
    private static String getValueOrDefault(String valueFromRequest, String valueFromServiceConfig) {
        return valueFromRequest != null ? valueFromRequest : valueFromServiceConfig;
    }
    public void startComposeFile() {
        try {
            executeDockerComposeCommand("up -d");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopComposeFile() {
        try {
            executeDockerComposeCommand("down");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restartComposeFile() {
        try {
            executeDockerComposeCommand("restart");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void executeDockerComposeCommand(String command) throws IOException, InterruptedException {
        String[] cmd = {"docker-compose", "-f", DOCKER_COMPOSE_FILE_PATH, command};
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // Read output (optional)
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print Docker Compose output if needed
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException(STR."Failed to execute Docker Compose command: \{command}");
        }
    }
    public boolean checkDockerComposeFileExists() {
        Path filePath = Paths.get(DOCKER_COMPOSE_FILE_PATH);
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }

    public boolean isServiceRunning() {
        try {
            // Command to list all running containers with specific service name
            String[] cmd = {"docker-compose", "-f", DOCKER_COMPOSE_FILE_PATH, "ps", "-q", "zomboid-dedicated-server"};
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Read output (container IDs)
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Check if there is any output (container IDs)
                return !result.toString().trim().isEmpty();
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception as needed
            return false; // Return false in case of exception
        }
    }

}
