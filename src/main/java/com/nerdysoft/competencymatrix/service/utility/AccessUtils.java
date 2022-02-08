package com.nerdysoft.competencymatrix.service.utility;

import com.nerdysoft.competencymatrix.entity.access.EvaluatorProgressAccess;
import com.nerdysoft.competencymatrix.entity.access.ManagerCompetencyAccess;
import com.nerdysoft.competencymatrix.repository.access.EvaluatorProgressRepository;
import com.nerdysoft.competencymatrix.repository.access.ManagerCompetencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class AccessUtils {

    private static ManagerCompetencyRepository accessManagerRepository;
    private static EvaluatorProgressRepository accessEvaluatorRepository;

    @Autowired
    public AccessUtils(ManagerCompetencyRepository accessManagerRepository, EvaluatorProgressRepository accessEvaluatorRepository) {
        AccessUtils.accessManagerRepository = accessManagerRepository;
        AccessUtils.accessEvaluatorRepository = accessEvaluatorRepository;
    }

    public static boolean getAccessForManager(UserDetails manager, Long id){
        Optional<ManagerCompetencyAccess> access = accessManagerRepository
                .findByManagerAndCompetency(manager.getUsername(), id);

        return access.isPresent();
    }

    public static boolean getAccessForEvaluator(UserDetails evaluator, Long progressId){
        Optional<EvaluatorProgressAccess> access = accessEvaluatorRepository
                .findByEvaluatorAndProgress(evaluator.getUsername(), progressId);

        return access.isPresent();
    }
}
