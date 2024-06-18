package org.loretdemolas.pzsw.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Mod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceConfig service;

    @Column(nullable = false)
    private String modName;

    @ManyToOne
    private WorkshopId workshopId;

    @Column(nullable = false)
    private Boolean enabled;

    public Mod() {
        // Assuming the ID of the default ServiceConfig entity is known (e.g., 1L)
        this.service = new ServiceConfig();
        this.service.setId(1L);
    }
}
