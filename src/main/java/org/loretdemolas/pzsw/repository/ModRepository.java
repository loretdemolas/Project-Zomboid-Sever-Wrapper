package org.loretdemolas.pzsw.repository;

import org.loretdemolas.pzsw.entity.Mod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModRepository extends JpaRepository<Mod, Long> {

    List<Mod> findByEnabledTrue();

}
