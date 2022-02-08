package com.nerdysoft.competencymatrix.repository.previlege;

import com.nerdysoft.competencymatrix.entity.privilege.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
