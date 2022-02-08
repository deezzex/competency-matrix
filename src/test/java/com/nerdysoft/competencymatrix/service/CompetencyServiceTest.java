package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.entity.enums.Type;
import com.nerdysoft.competencymatrix.repository.CompetencyRepository;
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

@ExtendWith(MockitoExtension.class)
class CompetencyServiceTest {

    @Mock
    private CompetencyRepository repository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CompetencyService service;

    private static final Long ID = 1L;

    @Test
    void createCompetency() {
        Competency competency = new Competency(ID, "testName", "testDesc", List.of());

        when(repository.save(competency)).thenReturn(new Competency(ID, "testNameCreated", "testDesc", List.of()));

        Competency createdCompetency = service.createCompetency(competency);

        assertEquals("testNameCreated", createdCompetency.getName());
        verify(repository, times(1)).save(competency);
    }

    @Test
    void findCompetencyById() {
        when(repository.findById(ID)).thenReturn(Optional.of(new Competency(ID, "testName", "testDesc", List.of())));

        Optional<Competency> maybeCompetency = service.findCompetencyById(ID);

        maybeCompetency.ifPresent(competency -> assertEquals("testName", competency.getName()));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAllCompetencies() {
        ArrayList<Competency> list = new ArrayList<>();

        Competency competency1 = new Competency(ID, "testName", "testDesc", List.of());
        Competency competency2 = new Competency(ID+1L, "testName2", "testDesc2", List.of());

        list.add(competency1);
        list.add(competency2);

        when(repository.findAll()).thenReturn(list);

        List<Competency> competenciesFromService = service.findAllCompetencies();

        assertEquals(2, competenciesFromService.size());
        assertTrue(competenciesFromService.contains(competency1));
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateCompetency() {
        Competency level = new Competency(ID, "testName", "testDesc", List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(level));

        Competency updateCompetency = service.updateCompetency(ID, new Competency(ID, "testNameUPD", "testDesc", List.of()),any(UserDetails.class));

        assertEquals("testNameUPD", updateCompetency.getName());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void deleteCompetency() {
        boolean delete = service.deleteCompetency(ID);
        repository.delete(new Competency());

        assertTrue(delete);
        verify(repository, times(2)).findById(ID);
    }

    @Test
    void addCategoryToCompetency() {
        Competency competency = new Competency();

        Category category = new Category(ID, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(competency));
        when(categoryService.findCategoryById(2L)).thenReturn(Optional.of(new Category()));

        competency.addCategory(category);
        service.addCategoryToCompetency(1L, 2L, any(UserDetails.class));

        assertTrue(competency.getCategories().contains(category));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void removeCategoryFromCompetency() {
        Competency competency = new Competency();

        Category category = new Category(ID, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(competency));
        when(categoryService.findCategoryById(2L)).thenReturn(Optional.of(new Category()));

        competency.addCategory(category);
        competency.removeCategory(category);
        service.removeCategoryFromCompetency(1L, 2L, any(UserDetails.class));

        assertFalse(competency.getCategories().contains(category));
        verify(repository, times(1)).findById(ID);
    }
}