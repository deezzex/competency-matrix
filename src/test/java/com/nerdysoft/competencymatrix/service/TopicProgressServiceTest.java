package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.TopicProgress;
import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.repository.TopicProgressRepository;
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
class TopicProgressServiceTest {

    @Mock
    private TopicProgressRepository repository;

    @Mock
    private TopicService topicService;

    @InjectMocks
    private TopicProgressService service;

    private static final Long ID = 1L;

    @Test
    void createProgress() {
        TopicProgress progress = new TopicProgress(ID, "testComment", 10, false, new Topic());

        when(repository.save(progress)).thenReturn(new TopicProgress(ID, "testCommentCreated", 10, false, new Topic()));

        TopicProgress createdProgress = service.createProgress(progress);

        assertEquals("testCommentCreated", createdProgress.getComment());
        verify(repository, times(1)).save(progress);
    }

    @Test
    void findProgressById() {
        when(repository.findById(ID)).thenReturn(Optional.of(new TopicProgress(ID, "testComment", 10, false, new Topic())));

        Optional<TopicProgress> maybeProgress = service.findProgressById(ID);

        maybeProgress.ifPresent(progress -> assertEquals("testComment", progress.getComment()));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAllProgresses() {
        ArrayList<TopicProgress> list = new ArrayList<>();

        TopicProgress progress1 = new TopicProgress(ID, "testComment", 10, false, new Topic());
        TopicProgress progress2 = new TopicProgress(ID+1L, "testComment2", 10, false, new Topic());

        list.add(progress1);
        list.add(progress2);

        when(repository.findAll()).thenReturn(list);

        List<TopicProgress> progressesFromService = service.findAllProgresses();

        assertEquals(2, progressesFromService.size());
        assertTrue(progressesFromService.contains(progress1));
        verify(repository, times(1)).findAll();
    }

    @Test
    void deleteProgress() {
        boolean delete = service.deleteProgress(ID);
        repository.delete(new TopicProgress());

        assertTrue(delete);
        verify(repository, times(2)).findById(ID);
    }

    @Test
    void addTopicToProgress() {
        TopicProgress progress = new TopicProgress();

        Topic topic = new Topic(ID, "testName", "testDescription", true, Priority.LOW, any(Category.class), List.of(), List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(progress));
        when(topicService.findTopicById(2L)).thenReturn(Optional.of(topic));

        progress.setTopic(topic);
        service.addTopicToProgress(1L, 2L);

        assertEquals(progress.getTopic(), topic);
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void setCommentAndMarkToProgress() {
        TopicProgress progress = new TopicProgress(ID, "testComment", 10, false, new Topic());
        when(repository.findById(ID)).thenReturn(Optional.of(progress));

        TopicProgress topicProgress = service.setCommentAndMarkToProgress(ID, progress, any(UserDetails.class));

        assertEquals("testComment", topicProgress.getComment());
        assertEquals(10, topicProgress.getMark());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void finishProgress() {
        TopicProgress progress = new TopicProgress(ID, "testComment", 10, false, new Topic());
        when(repository.findById(ID)).thenReturn(Optional.of(progress));

        TopicProgress topicProgress = service.finishProgress(ID);

        assertTrue(topicProgress.isFinished());

        verify(repository, times(1)).findById(ID);
    }

    @Test
    void isSuccessfullyEndedLevel() {
        when(repository.countFinishedAndRequiredTopic()).thenReturn(10);
        when(repository.countFinishedTopics()).thenReturn(10);
        when(topicService.countAllTopics()).thenReturn(10);
        when(topicService.countRequiredTopics()).thenReturn(10);

        boolean successfullyEndedLevel = service.isSuccessfullyEndedLevel();

        assertTrue(successfullyEndedLevel);
        verify(repository, times(1)).countFinishedAndRequiredTopic();
        verify(repository, times(1)).countFinishedTopics();
        verify(topicService, times(1)).countAllTopics();
        verify(topicService, times(1)).countRequiredTopics();
    }
}