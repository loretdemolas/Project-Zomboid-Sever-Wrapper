package org.loretdemolas.pzsw.repository;

import org.loretdemolas.pzsw.entity.Role;
import org.loretdemolas.pzsw.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
