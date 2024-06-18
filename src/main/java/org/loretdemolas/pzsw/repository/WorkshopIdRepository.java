package org.loretdemolas.pzsw.repository;

import org.loretdemolas.pzsw.entity.WorkshopId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WorkshopIdRepository extends JpaRepository<WorkshopId, Long> {

    List<WorkshopId> findByEnabledTrue();

}
