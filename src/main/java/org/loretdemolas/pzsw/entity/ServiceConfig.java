package org.loretdemolas.pzsw.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
@Entity
@Getter
@Setter
@Data
public class ServiceConfig {

    @Id
    private Long id = 1L;

    @Column(nullable = false)
    private String adminUsername;

    @Column(nullable = false)
    private String adminPassword;

    @Column(nullable = false)
    private String rconPassword;

    @Column(nullable = false)
    private String resetID;

    @Column(nullable = false)
    private String autosaveInterval = "5";

    @Column(nullable = false)
    private String bindIp = "0.0.0.0";

    @Column(nullable = false)
    private String defaultPort = "16261";

    @Column(nullable = false)
    private String gameVersion = "public";

    @Column(nullable = false)
    private String gcConfig = "ZGC";

    @Column(nullable = false)
    private String mapNames = "Muldraugh, KY";

    @Column(nullable = false)
    private String maxPlayers = "10";

    @Column(nullable = false)
    private String maxRam = "8000m";

    @Column(nullable = false)
    private boolean pauseOnEmpty = true;

    @Column(nullable = false)
    private boolean publicServer = true;

    @Column(nullable = false)
    private String rconPort = "27015";

    @Column(nullable = false)
    private String serverName = "servertest";

    @Column
    private String serverPassword;

    @Column(nullable = false)
    private boolean steamVac = true;

    @Column(nullable = false)
    private String udpPort = "16262";

    @Column(nullable = false)
    private boolean useSteam = true;

    @Column(nullable = false)
    private String tz = "UTC";

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Mod> mods;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<WorkshopId> workshopIds;
}
