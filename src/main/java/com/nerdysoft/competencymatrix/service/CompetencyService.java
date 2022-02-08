package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.access.ManagerCompetencyAccess;
import com.nerdysoft.competencymatrix.repository.CompetencyRepository;
import com.nerdysoft.competencymatrix.repository.access.ManagerCompetencyRepository;
import com.nerdysoft.competencymatrix.service.utility.AccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CompetencyService {

    private final CompetencyRepository repository;
    private final CategoryService categoryService;
    private final ManagerCompetencyRepository accessRepository;

    @Autowired
    public CompetencyService(CompetencyRepository repository, CategoryService categoryService, ManagerCompetencyRepository accessRepository) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.accessRepository = accessRepository;
    }

    public Competency createCompetency(Competency competency){
        return repository.save(competency);
    }

    public Optional<Competency> findCompetencyById(Long id){
        return repository.findById(id);
    }

    public List<Competency> findAllCompetencies(){
        return repository.findAll();
    }

    @Transactional
    public Competency updateCompetency(Long id, Competency competencyData, UserDetails user){
        Optional<Competency> maybeCompetency = repository.findById(id);

        if (!AccessUtils.getAccessForManager(user, id)){
            return new Competency();
        }

        if (maybeCompetency.isPresent()){
            Competency competency = maybeCompetency.get();

            competency.setName(competencyData.getName());
            competency.setDescription(competencyData.getName());

            return competency;
        }else
            return new Competency();
    }

    public boolean deleteCompetency(Long id){
        Optional<Competency> byId = repository.findById(id);

        if (byId.isPresent()){
            Competency competency = byId.get();
            repository.delete(competency);
        }
        Optional<Competency> removed = repository.findById(id);

        return removed.isEmpty();
    }

    @Transactional
    public Competency addCategoryToCompetency(Long competencyId, Long categoryId, UserDetails user){
        Optional<Competency> maybeCompetency = findCompetencyById(competencyId);
        Optional<Category> maybeCategory = categoryService.findCategoryById(categoryId);

        if (!AccessUtils.getAccessForManager(user, competencyId)){
            return new Competency();
        }

        if (maybeCompetency.isPresent() && maybeCategory.isPresent()){
            Competency competency = maybeCompetency.get();
            Category category = maybeCategory.get();

            competency.addCategory(category);

            return competency;
        }else
            return new Competency();
    }

    @Transactional
    public Competency removeCategoryFromCompetency(Long competencyId, Long categoryId, UserDetails user){
        Optional<Competency> maybeCompetency = findCompetencyById(competencyId);
        Optional<Category> maybeCategory = categoryService.findCategoryById(categoryId);

        if (!AccessUtils.getAccessForManager(user, competencyId)){
            return new Competency();
        }

        if (maybeCompetency.isPresent() && maybeCategory.isPresent()){
            Competency competency = maybeCompetency.get();
            Category category = maybeCategory.get();

            competency.removeCategory(category);

            return competency;
        }else
            return new Competency();
    }
}
