package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.*;
import com.nerdysoft.competencymatrix.repository.MatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MatrixService {

    private final MatrixRepository repository;
    private final LevelService levelService;
    private final CompetencyService competencyService;

    @Autowired
    public MatrixService(MatrixRepository repository, LevelService levelService, CompetencyService competencyService) {
        this.repository = repository;
        this.levelService = levelService;
        this.competencyService = competencyService;
    }

    public Matrix createMatrix(Matrix matrix){
        return repository.save(matrix);
    }

    public Optional<Matrix> findMatrixById(Long id){
        return repository.findById(id);
    }

    public List<Matrix> findAllMatrices(){
        return repository.findAll();
    }

    @Transactional
    public Matrix updateMatrix(Long id, Matrix matrixData){
        Optional<Matrix> maybeMatrix = repository.findById(id);

        if (maybeMatrix.isPresent()){
            Matrix matrix = maybeMatrix.get();

            matrix.setName(matrixData.getName());
            matrix.setDescription(matrixData.getName());

            return matrix;
        }else
            return new Matrix();
    }

    public boolean deleteMatrix(Long id){
        Optional<Matrix> byId = repository.findById(id);

        if (byId.isPresent()){
            Matrix matrix = byId.get();
            repository.delete(matrix);
        }
        Optional<Matrix> removed = repository.findById(id);

        return removed.isEmpty();
    }

    @Transactional
    public Matrix addLevelToMatrix(Long matrixId, Long levelId){
        Optional<Matrix> maybeMatrix = findMatrixById(matrixId);
        Optional<Level> maybeLevel = levelService.findLevelById(levelId);
        if (maybeMatrix.isPresent() && maybeLevel.isPresent()){
            Matrix matrix = maybeMatrix.get();
            Level level = maybeLevel.get();

            matrix.addLevel(level);

            return matrix;
        }else
            return new Matrix();
    }

    @Transactional
    public Matrix removeLevelFromMatrix(Long matrixId, Long levelId){
        Optional<Matrix> maybeMatrix = findMatrixById(matrixId);
        Optional<Level> maybeLevel = levelService.findLevelById(levelId);
        if (maybeMatrix.isPresent() && maybeLevel.isPresent()){
            Matrix matrix = maybeMatrix.get();
            Level level = maybeLevel.get();

            matrix.removeLevel(level);

            return matrix;
        }else
            return new Matrix();
    }

    @Transactional
    public Matrix addCompetencyToMatrix(Long matrixId, Long competencyId){
        Optional<Matrix> maybeMatrix = findMatrixById(matrixId);
        Optional<Competency> maybeCompetency = competencyService.findCompetencyById(competencyId);

        if (maybeMatrix.isPresent() && maybeCompetency.isPresent()){
            Matrix matrix = maybeMatrix.get();
            Competency competency = maybeCompetency.get();

            matrix.addCompetency(competency);

            return matrix;
        }else
            return new Matrix();
    }

    @Transactional
    public Matrix removeCompetencyFromMatrix(Long matrixId, Long competencyId){
        Optional<Matrix> maybeMatrix = findMatrixById(matrixId);
        Optional<Competency> maybeCompetency = competencyService.findCompetencyById(competencyId);

        if (maybeMatrix.isPresent()){
            Matrix matrix = maybeMatrix.get();
            Competency competency = maybeCompetency.get();

            matrix.removeCompetency(competency);

            return matrix;
        }else
            return new Matrix();
    }
}
