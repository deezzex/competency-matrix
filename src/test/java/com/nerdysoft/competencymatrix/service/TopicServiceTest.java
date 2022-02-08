package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Item;
import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private TopicRepository repository;

    @Mock
    private ItemService itemService;

    @Mock
    private ResourceService resourceService;

    @InjectMocks
    private TopicService topicService;

    private static final Long ID = 1L;

    @Test
    void createTopic() {
        Topic topic = new Topic(ID, "testName", "testDescription", true, Priority.LOW, any(Category.class), List.of(), List.of());

        when(repository.save(topic)).thenReturn(new Topic(ID, "testNameCreated", "testDescriptionCreated", true, Priority.LOW, any(Category.class), List.of(), List.of()));

        Topic createdTopic = topicService.createTopic(topic);

        assertEquals("testNameCreated", createdTopic.getName());

        verify(repository, times(1)).save(topic);
    }

    @Test
    void findTopicById() {
        when(repository.findById(ID)).thenReturn(Optional.of(new Topic(ID, "testName", "testDescription", true, Priority.LOW, any(Category.class), List.of(), List.of())));

        Optional<Topic> maybeTopic = topicService.findTopicById(ID);

        maybeTopic.ifPresent(topic -> assertEquals("testName", topic.getName()));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAllTopics() {
        ArrayList<Topic> list = new ArrayList<>();

        Topic topic1 = new Topic(ID, "testName", "testDescription", true, Priority.LOW, any(Category.class), List.of(), List.of());
        Topic topic2 = new Topic(ID+1L, "testName2", "testDescription2", true, Priority.LOW, any(Category.class), List.of(), List.of());

        list.add(topic1);
        list.add(topic2);

        when(repository.findAll()).thenReturn(list);

        List<Topic> itemsFromService = topicService.findAllTopics();

        assertEquals(2, itemsFromService.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateTopic() {
        Topic topic = new Topic(ID, "testName", "testDescription", true, Priority.LOW, any(Category.class), List.of(), List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(topic));

        Topic updateTopic = topicService.updateTopic(ID, new Topic(ID, "testNameUPD", "testDescription", true, Priority.LOW, any(Category.class), List.of(), List.of()));

        assertEquals("testNameUPD", updateTopic.getName());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void deleteTopic() {
        boolean delete = topicService.deleteTopic(ID);
        repository.delete(new Topic());

        assertTrue(delete);
        verify(repository, times(2)).findById(ID);
    }

    @Test
    void addResourceToTopic() {
        Topic topic = new Topic();

        Resource resource = new Resource(ID, "testName", "testDescription", "testUrl");

        when(repository.findById(ID)).thenReturn(Optional.of(topic));
        when(resourceService.findResourceById(2L)).thenReturn(Optional.of(new Resource()));

        topic.addResource(resource);
        topicService.addResourceToTopic(1L, 2L);

        assertTrue(topic.getResources().contains(resource));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void removeResourceFromTopic() {
        Topic topic = new Topic();

        Resource resource = new Resource(ID, "testName", "testDescription", "testUrl");

        when(repository.findById(ID)).thenReturn(Optional.of(topic));
        when(resourceService.findResourceById(2L)).thenReturn(Optional.of(new Resource()));

        topic.addResource(resource);
        topic.removeResource(resource);
        topicService.removeResourceFromTopic(1L, 2L);

        assertFalse(topic.getResources().contains(resource));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void addItemToTopic() {
        Topic topic = new Topic();

        Item item = new Item(ID, "testLabel", List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(topic));
        when(itemService.findItemById(2L)).thenReturn(Optional.of(new Item()));

        topic.addItem(item);
        topicService.addItemToTopic(1L, 2L);

        assertTrue(topic.getItems().contains(item));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void removeItemFromTopic() {
        Topic topic = new Topic();

        Item item = new Item(ID, "testLabel", List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(topic));
        when(itemService.findItemById(2L)).thenReturn(Optional.of(new Item()));

        topic.addItem(item);
        topic.removeItem(item);
        topicService.removeItemFromTopic(1L, 2L);

        assertFalse(topic.getItems().contains(item));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void countRequiredTopics() {
        when(repository.countRequiredTopics()).thenReturn(10);
        int i = topicService.countRequiredTopics();

        assertEquals(10, i);
        verify(repository, times(1)).countRequiredTopics();
    }

    @Test
    void countAllTopics() {
        when(repository.countAllTopics()).thenReturn(20);
        int i = topicService.countAllTopics();

        assertEquals(20, i);
        verify(repository, times(1)).countAllTopics();
    }
}