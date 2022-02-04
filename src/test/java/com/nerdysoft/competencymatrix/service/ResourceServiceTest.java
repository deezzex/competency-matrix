package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.repository.ResourceRepository;
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
class ResourceServiceTest {

    @Mock
    private ResourceRepository repository;

    @InjectMocks
    private ResourceService service;

    private static final Long ID = 1L;

    @Test
    void createResource() {
        Resource resource = new Resource(ID, "testName", "testDescription", "testUrl");

        when(repository.save(resource)).thenReturn(new Resource(ID, "created", "created", "created"));

        Resource createdResource = service.createResource(resource);

        assertEquals("created", createdResource.getName());

        verify(repository, times(1)).save(resource);
    }

    @Test
    void findResourceById() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Resource(ID, "testName", "testDescription", "testUrl")));

        Optional<Resource> maybeResource = service.findResourceById(ID);

        maybeResource.ifPresent(resource -> assertEquals("testName", resource.getName()));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAllResources() {
        ArrayList<Resource> list = new ArrayList<>();

        Resource resource1 = new Resource(ID, "testName", "testDescription", "testUrl");
        Resource resource2 = new Resource(ID+1L, "testName2", "testDescription2", "testUrl2");

        list.add(resource1);
        list.add(resource2);

        when(repository.findAll()).thenReturn(list);

        List<Resource> resourcesFromService = service.findAllResources();

        assertEquals(2, resourcesFromService.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateResource() {
        Resource resource = new Resource(ID, "testName", "testDescription", "testUrl");

        when(repository.findById(1L)).thenReturn(Optional.of(resource));

        Resource updatedResource = service.updateResource(ID, new Resource(ID, "testNameUPD", "testDescription", "testUrl"));

        assertEquals("testNameUPD", updatedResource.getName());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deleteResource() {
        boolean delete = service.deleteResource(ID);
        repository.delete(new Resource());

        assertTrue(delete);
        verify(repository, times(2)).findById(1L);
    }
}