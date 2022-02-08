package com.nerdysoft.competencymatrix.service.access;

import com.nerdysoft.competencymatrix.entity.access.EvaluatorProgressAccess;
import com.nerdysoft.competencymatrix.entity.access.ManagerCompetencyAccess;
import com.nerdysoft.competencymatrix.repository.access.ManagerCompetencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerCompetencyService {
    private final ManagerCompetencyRepository repository;

    @Autowired
    public ManagerCompetencyService(ManagerCompetencyRepository repository) {
        this.repository = repository;
    }

    public ManagerCompetencyAccess addAccess(ManagerCompetencyAccess access) {
        return repository.save(access);
    }

    public boolean deleteAccess(Long id) {
        Optional<ManagerCompetencyAccess> byId = repository.findById(id);

        if (byId.isPresent()){
            ManagerCompetencyAccess access = byId.get();
            repository.delete(access);
        }
        Optional<ManagerCompetencyAccess> removed = repository.findById(id);

        return removed.isEmpty();
    }

    public List<ManagerCompetencyAccess> getAccessForManager(UserDetails user) {
        return repository.findByManager(user.getUsername());
    }
}
