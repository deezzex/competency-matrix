package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.*;
import com.nerdysoft.competencymatrix.repository.UserRepository;
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
class UserServiceTest {
    @Mock
    private UserRepository repository;

    @Mock
    private MatrixService matrixService;

    @Mock
    private TopicProgressService topicProgressService;

    @InjectMocks
    private UserService service;

    private static final Long ID = 1L;

    @Test
    void createUser() {
        User user = new User(ID, "testFirst", "testLast", List.of(), List.of());

        when(repository.save(user)).thenReturn(new User(ID, "testFirstCreated", "testLast", List.of(), List.of()));

        User createdUser = service.createUser(user);

        assertEquals("testFirstCreated", createdUser.getFirstName());
        verify(repository, times(1)).save(user);
    }

    @Test
    void findUserById() {
        when(repository.findById(ID)).thenReturn(Optional.of(new User(ID, "testFirst", "testLast", List.of(), List.of())));

        Optional<User> maybeMatrix = service.findUserById(ID);

        maybeMatrix.ifPresent(user -> assertEquals("testFirst", user.getFirstName()));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAllUsers() {
        ArrayList<User> list = new ArrayList<>();

        User user1 = new User(ID, "testFirst", "testLast", List.of(), List.of());
        User user2 = new User(ID+1L, "testFirst2", "testLast2", List.of(), List.of());

        list.add(user1);
        list.add(user2);

        when(repository.findAll()).thenReturn(list);

        List<User> usersFromService = service.findAllUsers();

        assertEquals(2, usersFromService.size());
        assertTrue(usersFromService.contains(user1));
        verify(repository, times(1)).findAll();
    }

    @Test
    void addMatrixToUser() {
        User user = new User();

        Matrix matrix = new Matrix(ID, "testName", "testDesc", List.of(), List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(user));
        when(matrixService.findMatrixById(2L)).thenReturn(Optional.of(new Matrix()));

        user.addMatrix(matrix);
        service.addMatrixToUser(1L, 2L);

        assertTrue(user.getMatrices().contains(matrix));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void addProgressToUser() {
        User user = new User();

        TopicProgress progress = new TopicProgress(ID, "testComment", 10, false, new Topic());

        when(repository.findById(ID)).thenReturn(Optional.of(user));
        when(topicProgressService.findProgressById(2L)).thenReturn(Optional.of(new TopicProgress()));

        user.addProgress(progress);
        service.addProgressToUser(1L, 2L);

        assertTrue(user.getTopicProgressList().contains(progress));
        verify(repository, times(1)).findById(ID);
    }
}