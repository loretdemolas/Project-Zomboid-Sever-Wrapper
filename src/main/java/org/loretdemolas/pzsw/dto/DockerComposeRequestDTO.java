package org.loretdemolas.pzsw.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DockerComposeRequestDTO {
    private String autosaveInterval;
    private String bindIp;
    private String defaultPort;
    private String gameVersion;
    private String gcConfig;
    private String mapNames;
    private String maxPlayers;
    private String maxRam;
    private String modNames;
    private String modWorkshopIds;
    private boolean pauseOnEmpty;
    private boolean publicServer;
    private String serverName;
    private String serverPassword;
    private boolean steamVac;
    private String udpPort;
    private boolean useSteam;
    private String timezone;
    private String resetId;
}
