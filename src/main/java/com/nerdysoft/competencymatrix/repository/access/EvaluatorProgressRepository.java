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
public interface EvaluatorProgressRepository extends JpaRepository<EvaluatorProgressAccess, Long> {
    @Query("select a from EvaluatorProgressAccess a where a.evaluator.username = :evaluatorUsername and a.progress.id = :progressId")
    Optional<EvaluatorProgressAccess> findByEvaluatorAndProgress(@Param("evaluatorUsername")String evaluatorUsername
            , @Param("progressId")Long progressId);

    @Query("select a from EvaluatorProgressAccess a where a.evaluator.username = :username")
    List<EvaluatorProgressAccess> findByEvaluator(@Param("username") String username);

}
