package org.loretdemolas.pzsw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "service.config")
@Getter
@Setter
public class ServiceConfigProperties {
    private String adminUsername;
    private String adminPasswords;
    private String rconPassword;
    private String resetID;
}
