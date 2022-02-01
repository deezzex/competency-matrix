package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.dto.CategoryDto;
import com.nerdysoft.competencymatrix.entity.dto.CompetencyDto;
import com.nerdysoft.competencymatrix.service.CompetencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/competency")
public class CompetencyController {

    private final CompetencyService competencyService;

    @Autowired
    public CompetencyController(CompetencyService competencyService) {
        this.competencyService = competencyService;
    }

    @PostMapping
    public ResponseEntity<CompetencyDto> createCompetency(@Valid @RequestBody CompetencyDto competencyDto){
        Competency competency = competencyService.createCompetency(Competency.from(competencyDto));
        return new ResponseEntity<>(CompetencyDto.from(competency), OK);
    }

    @GetMapping
    public ResponseEntity<List<CompetencyDto>> getCompetencies(){
        List<Competency> allCompetencies = competencyService.findAllCompetencies();
        List<CompetencyDto> competencyDtoList = allCompetencies.stream()
                .map(CompetencyDto::from).collect(Collectors.toList());

        return new ResponseEntity<>(competencyDtoList, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetencyDto> getOneCompetency(@PathVariable Long id){
        Optional<Competency> maybeCompetency = competencyService.findCompetencyById(id);

        if (maybeCompetency.isPresent()){
            Competency competency = maybeCompetency.get();

            return new ResponseEntity<>(CompetencyDto.from(competency), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetencyDto> updateCompetency(@PathVariable Long id, @Valid @RequestBody CompetencyDto competencyDto){
        Competency competency = competencyService.updateCompetency(id, Competency.from(competencyDto));

        if(Objects.nonNull(competency.getId())){
            return new ResponseEntity<>(CompetencyDto.from(competency), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CompetencyDto> removeCompetency(@PathVariable Long id){
        try {
            competencyService.deleteCompetency(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{competencyId}/add/{categoryId}")
    public ResponseEntity<CompetencyDto> addTopicToCompetency(@PathVariable Long competencyId, @PathVariable Long categoryId){
        Competency competency = competencyService.addCategoryToCompetency(competencyId, categoryId);

        return new ResponseEntity<>(CompetencyDto.from(competency), OK);
    }

    @DeleteMapping("/{competencyId}/delete/{categoryId}")
    public ResponseEntity<CompetencyDto> deleteTopicFromCompetency(@PathVariable Long competencyId, @PathVariable Long categoryId){
        Competency category = competencyService.removeCategoryFromCompetency(competencyId, categoryId);

        return new ResponseEntity<>(CompetencyDto.from(category), OK);
    }
}
