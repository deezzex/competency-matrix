package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.Matrix;
import com.nerdysoft.competencymatrix.entity.dto.CompetencyDto;
import com.nerdysoft.competencymatrix.entity.dto.MatrixDto;
import com.nerdysoft.competencymatrix.service.MatrixService;
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
@RequestMapping("/matrix")
public class MatrixController {

    private final MatrixService matrixService;

    @Autowired
    public MatrixController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @PostMapping
    public ResponseEntity<MatrixDto> createMatrix(@Valid @RequestBody MatrixDto matrixDto){
        Matrix matrix = matrixService.createMatrix(Matrix.from(matrixDto));
        return new ResponseEntity<>(MatrixDto.from(matrix), OK);
    }

    @GetMapping
    public ResponseEntity<List<MatrixDto>> getMatrices(){
        List<Matrix> allMatrices = matrixService.findAllMatrices();
        List<MatrixDto> matrixDtoList = allMatrices.stream()
                .map(MatrixDto::from).collect(Collectors.toList());

        return new ResponseEntity<>(matrixDtoList, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatrixDto> getOneMatrix(@PathVariable Long id){
        Optional<Matrix> maybeMatrix = matrixService.findMatrixById(id);

        if (maybeMatrix.isPresent()){
            Matrix matrix = maybeMatrix.get();

            return new ResponseEntity<>(MatrixDto.from(matrix), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatrixDto> updateMatrix(@PathVariable Long id, @Valid @RequestBody MatrixDto matrixDto){
        Matrix matrix = matrixService.updateMatrix(id, Matrix.from(matrixDto));

        if(Objects.nonNull(matrix.getId())){
            return new ResponseEntity<>(MatrixDto.from(matrix), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MatrixDto> removeMatrix(@PathVariable Long id){
        try {
            matrixService.deleteMatrix(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{matrixId}/add/level/{levelId}")
    public ResponseEntity<MatrixDto> addLevelToMatrix(@PathVariable Long matrixId, @PathVariable Long levelId){
        Matrix matrix = matrixService.addLevelToMatrix(matrixId, levelId);

        return new ResponseEntity<>(MatrixDto.from(matrix), OK);
    }

    @DeleteMapping("/{matrixId}/delete/level/{levelId}")
    public ResponseEntity<MatrixDto> deleteLevelFromMatrix(@PathVariable Long matrixId, @PathVariable Long levelId){
        Matrix matrix = matrixService.removeLevelFromMatrix(matrixId, levelId);

        return new ResponseEntity<>(MatrixDto.from(matrix), OK);
    }

    @PostMapping("/{matrixId}/add/competency/{competencyId}")
    public ResponseEntity<MatrixDto> addCompetencyToMatrix(@PathVariable Long matrixId, @PathVariable Long competencyId){
        Matrix matrix = matrixService.addCompetencyToMatrix(matrixId, competencyId);

        return new ResponseEntity<>(MatrixDto.from(matrix), OK);
    }

    @DeleteMapping("/{matrixId}/delete/competency/{competencyId}")
    public ResponseEntity<MatrixDto> deleteCompetencyFromMatrix(@PathVariable Long matrixId, @PathVariable Long competencyId){
        Matrix matrix = matrixService.removeCompetencyFromMatrix(matrixId, competencyId);

        return new ResponseEntity<>(MatrixDto.from(matrix), OK);
    }
}
