package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.entity.enums.Type;
import com.nerdysoft.competencymatrix.repository.LevelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.ManyToOne;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LevelServiceTest {

    @Mock
    private LevelRepository repository;

    @Mock
    private TopicService topicService;

    @InjectMocks
    private LevelService service;

    private static final Long ID = 1L;

    @Test
    void createLevel() {
        Level level = new Level(ID, "testName", "testDesc", List.of());

        when(repository.save(level)).thenReturn(new Level(ID, "testNameCreated", "testDesc", List.of()));

        Level createdLevel = service.createLevel(level);

        assertEquals("testNameCreated", createdLevel.getName());
        verify(repository, times(1)).save(level);
    }

    @Test
    void findLevelById() {
        when(repository.findById(ID)).thenReturn(Optional.of(new Level(ID, "testName", "testDesc", List.of())));

        Optional<Level> maybeLevel = service.findLevelById(ID);

        maybeLevel.ifPresent(level -> assertEquals("testName", level.getName()));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAllLevels() {
        ArrayList<Level> list = new ArrayList<>();

        Level level1 = new Level(ID, "testName", "testDesc", List.of());
        Level level2 = new Level(ID+1L, "testName2", "testDesc2", List.of());

        list.add(level1);
        list.add(level2);

        when(repository.findAll()).thenReturn(list);

        List<Level> levelsFromService = service.findAllLevels();

        assertEquals(2, levelsFromService.size());
        assertTrue(levelsFromService.contains(level1));
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateLevel() {
        Level level = new Level(ID, "testName", "testDesc", List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(level));

        Level updateLevel = service.updateLevel(ID, new Level(ID, "testNameUPD", "testDesc", List.of()));

        assertEquals("testNameUPD", updateLevel.getName());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void deleteLevel() {
        boolean delete = service.deleteLevel(ID);
        repository.delete(new Level());

        assertTrue(delete);
        verify(repository, times(2)).findById(ID);
    }

    @Test
    void addTopicToLevel() {
        Level level = new Level();

        Topic topic = new Topic(ID, "testName", "testDescription", true, Priority.LOW, List.of(), List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(level));
        when(topicService.findTopicById(2L)).thenReturn(Optional.of(new Topic()));

        level.addTopic(topic);
        service.addTopicToLevel(1L, 2L);

        assertTrue(level.getTopics().contains(topic));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void removeTopicFromLevel() {
        Level level = new Level();

        Topic topic = new Topic(ID, "testName", "testDescription", true, Priority.LOW, List.of(), List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(level));
        when(topicService.findTopicById(2L)).thenReturn(Optional.of(new Topic()));

        level.addTopic(topic);
        level.removeTopic(topic);
        service.removeTopicFromLevel(1L, 2L);

        assertFalse(level.getTopics().contains(topic));
        verify(repository, times(1)).findById(ID);
    }
}