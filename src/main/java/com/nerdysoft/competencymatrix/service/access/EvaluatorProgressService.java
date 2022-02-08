package com.nerdysoft.competencymatrix.service.access;

import com.nerdysoft.competencymatrix.entity.Item;
import com.nerdysoft.competencymatrix.entity.access.EvaluatorProgressAccess;
import com.nerdysoft.competencymatrix.repository.access.EvaluatorProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluatorProgressService {
    private final EvaluatorProgressRepository repository;

    @Autowired
    public EvaluatorProgressService(EvaluatorProgressRepository repository) {
        this.repository = repository;
    }

    public EvaluatorProgressAccess addAccess(EvaluatorProgressAccess access) {
        return repository.save(access);
    }

    public boolean deleteAccess(Long id) {
        Optional<EvaluatorProgressAccess> byId = repository.findById(id);

        if (byId.isPresent()){
            EvaluatorProgressAccess access = byId.get();
            repository.delete(access);
        }
        Optional<EvaluatorProgressAccess> removed = repository.findById(id);

        return removed.isEmpty();
    }

    public List<EvaluatorProgressAccess> getAccessForEvaluator(UserDetails user) {
        return repository.findByEvaluator(user.getUsername());
    }
}
