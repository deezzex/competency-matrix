package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.Matrix;
import com.nerdysoft.competencymatrix.entity.User;
import com.nerdysoft.competencymatrix.entity.dto.CompetencyDto;
import com.nerdysoft.competencymatrix.entity.dto.MatrixDto;
import com.nerdysoft.competencymatrix.service.MatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PreAuthorize("hasAuthority('CAN_CREATE_MATRIX')")
    public ResponseEntity<MatrixDto> createMatrix(@Valid @RequestBody MatrixDto matrixDto){
        Matrix matrix = matrixService.createMatrix(Matrix.from(matrixDto));
        return new ResponseEntity<>(MatrixDto.from(matrix), OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CAN_READ')")
    public ResponseEntity<List<MatrixDto>> getMatrices(){
        List<Matrix> allMatrices = matrixService.findAllMatrices();
        List<MatrixDto> matrixDtoList = allMatrices.stream()
                .map(MatrixDto::from).collect(Collectors.toList());

        if (matrixDtoList.isEmpty()){
            return new ResponseEntity<>(NO_CONTENT);
        }

        return new ResponseEntity<>(matrixDtoList, OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_READ')")
    public ResponseEntity<MatrixDto> getOneMatrix(@PathVariable Long id){
        Optional<Matrix> maybeMatrix = matrixService.findMatrixById(id);

        if (maybeMatrix.isPresent()){
            Matrix matrix = maybeMatrix.get();

            return new ResponseEntity<>(MatrixDto.from(matrix), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_EDIT')")
    public ResponseEntity<MatrixDto> updateMatrix(@PathVariable Long id, @Valid @RequestBody MatrixDto matrixDto){
        Matrix matrix = matrixService.updateMatrix(id, Matrix.from(matrixDto));

        if(Objects.nonNull(matrix.getId())){
            return new ResponseEntity<>(MatrixDto.from(matrix), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_EDIT')")
    public ResponseEntity<MatrixDto> removeMatrix(@PathVariable Long id){
        try {
            matrixService.deleteMatrix(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('CAN_CREATE_MATRIX')")
    @PostMapping("/{matrixId}/add/level/{levelId}")
    public ResponseEntity<MatrixDto> addLevelToMatrix(@PathVariable Long matrixId, @PathVariable Long levelId){
        Matrix matrix = matrixService.addLevelToMatrix(matrixId, levelId);

        if(Objects.nonNull(matrix.getId())){
            return new ResponseEntity<>(MatrixDto.from(matrix), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_CREATE_MATRIX')")
    @DeleteMapping("/{matrixId}/delete/level/{levelId}")
    public ResponseEntity<MatrixDto> deleteLevelFromMatrix(@PathVariable Long matrixId, @PathVariable Long levelId){
        Matrix matrix = matrixService.removeLevelFromMatrix(matrixId, levelId);

        if(Objects.nonNull(matrix.getId())){
            return new ResponseEntity<>(MatrixDto.from(matrix), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_CREATE_MATRIX')")
    @PostMapping("/{matrixId}/add/competency/{competencyId}")
    public ResponseEntity<MatrixDto> addCompetencyToMatrix(@PathVariable Long matrixId, @PathVariable Long competencyId, @AuthenticationPrincipal UserDetails user){
        Matrix matrix = matrixService.addCompetencyToMatrix(matrixId, competencyId, user);

        if(Objects.nonNull(matrix.getId())){
            return new ResponseEntity<>(MatrixDto.from(matrix), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_CREATE_MATRIX')")
    @DeleteMapping("/{matrixId}/delete/competency/{competencyId}")
    public ResponseEntity<MatrixDto> deleteCompetencyFromMatrix(@PathVariable Long matrixId, @PathVariable Long competencyId){
        Matrix matrix = matrixService.removeCompetencyFromMatrix(matrixId, competencyId);

        if(Objects.nonNull(matrix.getId())){
            return new ResponseEntity<>(MatrixDto.from(matrix), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
