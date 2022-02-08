package com.nerdysoft.competencymatrix.repository.access;

import com.nerdysoft.competencymatrix.entity.access.EvaluatorProgressAccess;
import com.nerdysoft.competencymatrix.entity.access.ManagerCompetencyAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerCompetencyRepository extends JpaRepository<ManagerCompetencyAccess, Long> {
    @Query("select a from ManagerCompetencyAccess a where a.manager.username = :managerUsername and a.competency.id = :competencyId")
    Optional<ManagerCompetencyAccess> findByManagerAndCompetency(@Param("managerUsername")String managerUsername
            , @Param("competencyId")Long id);

    @Query("select a from ManagerCompetencyAccess a where a.manager.username = :username")
    List<ManagerCompetencyAccess> findByManager(@Param("username") String username);
}
