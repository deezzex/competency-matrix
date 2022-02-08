package com.nerdysoft.competencymatrix.repository.previlege;

import com.nerdysoft.competencymatrix.entity.privilege.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
