package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.entity.enums.Type;
import com.nerdysoft.competencymatrix.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private TopicService topicService;

    @InjectMocks
    private CategoryService service;

    private static final Long ID = 1L;

    @Test
    void createCategory() {
        Category category = new Category(ID, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of());

        when(repository.save(category)).thenReturn(new Category(ID, "testNameCreated", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of()));

        Category createdCategory = service.createCategory(category);

        assertEquals("testNameCreated", createdCategory.getName());
        verify(repository, times(1)).save(category);
    }

    @Test
    void findCategoryById() {
        when(repository.findById(ID)).thenReturn(Optional.of(new Category(ID, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of())));

        Optional<Category> maybeCategory = service.findCategoryById(ID);

        maybeCategory.ifPresent(category -> assertEquals("testName", category.getName()));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAllCategories() {
        ArrayList<Category> list = new ArrayList<>();

        Category category1 = new Category(ID, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of());
        Category category2 = new Category(ID+1L, "testName2", "testDesc2", Type.HARD_SKILL, Priority.LOW, List.of());

        list.add(category1);
        list.add(category2);

        when(repository.findAll()).thenReturn(list);

        List<Category> categoriesFromService = service.findAllCategories();

        assertEquals(2, categoriesFromService.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateCategory() {
        Category category = new Category(ID, "testName", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(category));

        Category updateCategory = service.updateCategory(ID, new Category(ID, "testNameUPD", "testDesc", Type.HARD_SKILL, Priority.LOW, List.of()));

        assertEquals("testNameUPD", updateCategory.getName());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void deleteCategory() {
        boolean delete = service.deleteCategory(ID);
        repository.delete(new Category());

        assertTrue(delete);
        verify(repository, times(2)).findById(ID);
    }

    @Test
    void addTopicToCategory() {
        Category category = new Category();

        Topic topic = new Topic(ID, "testName", "testDescription", true, Priority.LOW, any(Category.class), List.of(), List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(category));
        when(topicService.findTopicById(2L)).thenReturn(Optional.of(new Topic()));

        category.addTopic(topic);
        service.addTopicToCategory(1L, 2L);

        assertTrue(category.getTopics().contains(topic));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void removeTopicFromCategory() {
        Category category = new Category();

        Topic topic = new Topic(ID, "testName", "testDescription", true, Priority.LOW, any(Category.class), List.of(), List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(category));
        when(topicService.findTopicById(2L)).thenReturn(Optional.of(new Topic()));

        category.addTopic(topic);
        category.removeTopic(topic);
        service.removeTopicFromCategory(1L, 2L);

        assertFalse(category.getTopics().contains(topic));
        verify(repository, times(1)).findById(ID);
    }
}