package org.loretdemolas.pzsw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.loretdemolas.pzsw.config.ServiceConfigProperties;
import org.loretdemolas.pzsw.dto.DockerComposeRequestDTO;
import org.loretdemolas.pzsw.entity.ServiceConfig;
import org.loretdemolas.pzsw.repository.ModRepository;
import org.loretdemolas.pzsw.repository.ServiceConfigRepository;
import org.loretdemolas.pzsw.repository.WorkshopIdRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServerServiceTest {

    @Mock
    private ServiceConfigRepository serviceConfigRepository;

    @Mock
    private ModRepository modRepository;

    @Mock
    private WorkshopIdRepository workshopIdRepository;

    @Mock
    private ServiceConfigProperties serviceConfigProperties;

    @InjectMocks
    private ServerService serverService;

    @BeforeEach
    public void setUp() {
        lenient().when(serviceConfigProperties.getAdminUsername()).thenReturn("admin");
        lenient().when(serviceConfigProperties.getAdminPasswords()).thenReturn("password");
        lenient().when(serviceConfigProperties.getRconPassword()).thenReturn("rconpassword");
        lenient().when(serviceConfigProperties.getResetID()).thenReturn("reset123");

        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setAdminUsername("admin");
        serviceConfig.setAdminPassword("password");
        serviceConfig.setRconPassword("rconpassword");
        serviceConfig.setResetID("reset123");

        lenient().when(serviceConfigRepository.findById(anyLong())).thenReturn(Optional.of(serviceConfig));
    }


    @Test
    public void testCreateDockerComposeFile() {
        DockerComposeRequestDTO request = new DockerComposeRequestDTO();
        // Set request properties as needed

        assertDoesNotThrow(() -> serverService.createDockerComposeFile(request));
    }
}
