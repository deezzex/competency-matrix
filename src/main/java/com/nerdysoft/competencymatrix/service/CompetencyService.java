package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Matrix;
import com.nerdysoft.competencymatrix.repository.CompetencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CompetencyService {

    private final CompetencyRepository repository;
    private final CategoryService categoryService;

    @Autowired
    public CompetencyService(CompetencyRepository repository, CategoryService categoryService) {
        this.repository = repository;
        this.categoryService = categoryService;
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
    public Competency updateCompetency(Long id, Competency competencyData){
        Optional<Competency> maybeCompetency = repository.findById(id);

        if (maybeCompetency.isPresent()){
            Competency competency = maybeCompetency.get();

            competency.setName(competencyData.getName());
            competency.setDescription(competencyData.getName());

            return competency;
        }else
            return new Competency();
    }

    public void deleteCompetency(Long id){
        repository.deleteById(id);
    }

    @Transactional
    public Competency addCategoryToCompetency(Long competencyId, Long categoryId){
        Optional<Competency> maybeCompetency = findCompetencyById(competencyId);
        Optional<Category> maybeCategory = categoryService.findCategoryById(categoryId);
        if (maybeCompetency.isPresent() && maybeCategory.isPresent()){
            Competency competency = maybeCompetency.get();
            Category category = maybeCategory.get();

            competency.addCategory(category);

            return competency;
        }else
            return new Competency();
    }

    @Transactional
    public Competency removeCategoryFromCompetency(Long competencyId, Long categoryId){
        Optional<Competency> maybeCompetency = findCompetencyById(competencyId);
        Optional<Category> maybeCategory = categoryService.findCategoryById(categoryId);
        if (maybeCompetency.isPresent() && maybeCategory.isPresent()){
            Competency competency = maybeCompetency.get();
            Category category = maybeCategory.get();

            competency.removeCategory(category);

            return competency;
        }else
            return new Competency();
    }
}
