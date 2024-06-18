package org.loretdemolas.pzsw.service;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class DockerComposeBuilderService {

    private final List<DockerService> services = new ArrayList<>();
    private final List<String> volumes = new ArrayList<>();
    private final List<String> networks = new ArrayList<>();

    public DockerComposeBuilderService addService(DockerService service) {
        services.add(service);
        return this;
    }

    public DockerComposeBuilderService addVolume(String volume) {
        volumes.add(volume);
        return this;
    }

    public DockerComposeBuilderService addNetwork(String network) {
        networks.add(network);
        return this;
    }

    public String build() {
        StringBuilder composeFile = new StringBuilder("version: '3.8'\n");

        // Standard service configuration
        composeFile.append("services:\n")
                .append("  zomboid-dedicated-server:\n")
                .append("    user: \"1000:1000\"\n")
                .append("    build:\n")
                .append("      context: .\n")
                .append("      dockerfile: docker/zomboid-dedicated-server.Dockerfile\n")
                .append("      args:\n")
                .append("        - \"UID=${UID:-1000}\"\n")
                .append("        - \"GID=${GID:-1000}\"\n")
                .append("    image: \"ghcr.io/loretdemolas/pz-server\"\n")
                .append("    container_name: zomboid-dedicated-server\n")
                .append("    restart: unless-stopped\n");

        // Append user-defined services
        services.forEach(service -> composeFile.append(service).append("\n"));

        if (!volumes.isEmpty()) {
            composeFile.append("volumes:\n");
            volumes.forEach(volume -> composeFile.append("  ").append(volume).append("\n"));
        }
        if (!networks.isEmpty()) {
            composeFile.append("networks:\n");
            networks.forEach(network -> composeFile.append("  ").append(network).append("\n"));
        }

        return composeFile.toString();
    }

    @Data
    public static class DockerService {
        private final String name;
        private final String image;
        private final String containerName;
        private final String restart;
        private final List<String> environment = new ArrayList<>();
        private final List<String> ports = new ArrayList<>();
        private final List<String> volumes = new ArrayList<>();
        private String user;
        private String buildContext;
        private String dockerfile;
        private List<String> buildArgs = new ArrayList<>();

        public DockerService() {
            this.name = "zomboid-dedicated-server";
            this.image = "ghcr.io/loretdemolas/pz-server:master";
            this.containerName = "zomboid-dedicated-server";
            this.restart = "unless-stopped";
        }

        public DockerService addUser(String user) {
            this.user = user;
            return this;
        }

        public DockerService setBuildContext(String context) {
            this.buildContext = context;
            return this;
        }

        public DockerService setDockerfile(String dockerfile) {
            this.dockerfile = dockerfile;
            return this;
        }

        public DockerService addBuildArg(String arg) {
            this.buildArgs.add(arg);
            return this;
        }

        public DockerService addEnvironmentVariable(String key, String value) {
            environment.add(String.format("%s=%s", key, value));
            return this;
        }

        public DockerService addPort(String port) {
            ports.add(port);
            return this;
        }

        public DockerService addVolume(String volume) {
            volumes.add(volume);
            return this;
        }

        @Override
        public String toString() {
            StringBuilder service = new StringBuilder();
            service.append("  ").append(name).append(":\n")
                    .append("    image: ").append(image).append("\n")
                    .append("    container_name: ").append(containerName).append("\n")
                    .append("    restart: ").append(restart).append("\n")
                    .append("    user: ").append(user).append("\n")
                    .append("    build:\n")
                    .append("      context: ").append(buildContext).append("\n")
                    .append("      dockerfile: ").append(dockerfile).append("\n");

            if (!buildArgs.isEmpty()) {
                service.append("      args:\n");
                buildArgs.forEach(arg -> service.append("        - ").append(arg).append("\n"));
            }

            if (!environment.isEmpty()) {
                service.append("    environment:\n");
                environment.forEach(env -> service.append("      - ").append(env).append("\n"));
            }
            if (!ports.isEmpty()) {
                service.append("    ports:\n");
                ports.forEach(port -> service.append("      - ").append(port).append("\n"));
            }
            if (!volumes.isEmpty()) {
                service.append("    volumes:\n");
                volumes.forEach(volume -> service.append("      - ").append(volume).append("\n"));
            }
            return service.toString();
        }
    }
}



