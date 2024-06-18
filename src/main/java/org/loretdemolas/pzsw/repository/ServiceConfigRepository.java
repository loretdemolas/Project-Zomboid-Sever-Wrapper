package org.loretdemolas.pzsw.repository;

import org.loretdemolas.pzsw.entity.ServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceConfigRepository extends JpaRepository<ServiceConfig, Long> {

}

