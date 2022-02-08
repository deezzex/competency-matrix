package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Matrix;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.entity.enums.Type;
import com.nerdysoft.competencymatrix.repository.MatrixRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class MatrixServiceTest {

    @Mock
    private MatrixRepository repository;

    @Mock
    private LevelService levelService;

    @Mock
    private CompetencyService competencyService;

    @InjectMocks
    private MatrixService service;

    private static final Long ID = 1L;

    @Test
    void createMatrix() {
        Matrix matrix = new Matrix(ID, "testName", "testDesc", List.of(), List.of());

        when(repository.save(matrix)).thenReturn(new Matrix(ID, "testNameCreated", "testDesc", List.of(), List.of()));

        Matrix createdMatrix = service.createMatrix(matrix);

        assertEquals("testNameCreated", createdMatrix.getName());
        verify(repository, times(1)).save(matrix);
    }

    @Test
    void findMatrixById() {
        when(repository.findById(ID)).thenReturn(Optional.of(new Matrix(ID, "testName", "testDesc", List.of(), List.of())));

        Optional<Matrix> maybeMatrix = service.findMatrixById(ID);

        maybeMatrix.ifPresent(matrix -> assertEquals("testName", matrix.getName()));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAllMatrices() {
        ArrayList<Matrix> list = new ArrayList<>();

        Matrix matrix1 = new Matrix(ID, "testName", "testDesc", List.of(), List.of());
        Matrix matrix2 = new Matrix(ID+1L, "testName2", "testDesc2", List.of(), List.of());

        list.add(matrix1);
        list.add(matrix2);

        when(repository.findAll()).thenReturn(list);

        List<Matrix> matricesFromService = service.findAllMatrices();

        assertEquals(2, matricesFromService.size());
        assertTrue(matricesFromService.contains(matrix1));
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateMatrix() {
        Matrix level = new Matrix(ID, "testName", "testDesc", List.of(), List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(level));

        Matrix updateMatrix = service.updateMatrix(ID, new Matrix(ID, "testNameUPD", "testDesc", List.of(), List.of()));

        assertEquals("testNameUPD", updateMatrix.getName());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void deleteMatrix() {
        boolean delete = service.deleteMatrix(ID);
        repository.delete(new Matrix());

        assertTrue(delete);
        verify(repository, times(2)).findById(ID);
    }

    @Test
    void addLevelToMatrix() {
        Matrix matrix = new Matrix();

        Level level = new Level(ID, "testName", "testDesc", List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(matrix));
        when(levelService.findLevelById(2L)).thenReturn(Optional.of(new Level()));

        matrix.addLevel(level);
        service.addLevelToMatrix(1L, 2L);

        assertTrue(matrix.getLevels().contains(level));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void removeLevelFromMatrix() {
        Matrix matrix = new Matrix();

        Level level = new Level(ID, "testName", "testDesc", List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(matrix));
        when(levelService.findLevelById(2L)).thenReturn(Optional.of(new Level(ID, "testName", "testDesc", List.of())));

        matrix.addLevel(level);
        matrix.removeLevel(level);
        service.removeLevelFromMatrix(1L, 2L);

        assertFalse(matrix.getLevels().contains(level));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void addCompetencyToMatrix() {
        Matrix matrix = new Matrix();

        Competency competency = new Competency(ID, "testName", "testDesc", List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(matrix));
        when(competencyService.findCompetencyById(2L)).thenReturn(Optional.of(new Competency()));

        matrix.addCompetency(competency);
        service.addCompetencyToMatrix(1L, 2L, any(UserDetails.class));

        assertTrue(matrix.getCompetencies().contains(competency));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void removeCompetencyFromMatrix() {
        Matrix matrix = new Matrix();

        Competency competency = new Competency(ID, "testName", "testDesc", List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(matrix));
        when(competencyService.findCompetencyById(2L)).thenReturn(Optional.of(new Competency()));

        matrix.addCompetency(competency);
        matrix.removeCompetency(competency);
        service.removeCompetencyFromMatrix(1L, 2L);

        assertFalse(matrix.getCompetencies().contains(competency));
        verify(repository, times(1)).findById(ID);
    }
}