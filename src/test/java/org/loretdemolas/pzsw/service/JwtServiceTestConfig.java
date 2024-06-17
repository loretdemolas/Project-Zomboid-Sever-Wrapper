package org.loretdemolas.pzsw.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Value;

@TestConfiguration
@TestPropertySource(properties = {
        "security.jwt.secret-key=YOUR_SECRET_KEY_HERE",
        "security.jwt.expiration-time=3600000"
})
public class JwtServiceTestConfig {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Bean
    @Primary
    public JwtService jwtService() {
        JwtService jwtService = new JwtService();
        jwtService.secretKey = secretKey;
        jwtService.jwtExpiration = jwtExpiration;
        return jwtService;
    }
}
