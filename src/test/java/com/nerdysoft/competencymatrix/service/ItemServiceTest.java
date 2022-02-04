package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Item;
import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;

    @Mock
    private ResourceService resourceService;

    @InjectMocks
    private ItemService service;

    private static final Long ID = 1L;

    @Test
    void createItem() {
        Item item = new Item(ID, "testLabel", List.of());

        when(repository.save(item)).thenReturn(new Item(ID, "created", List.of()));

        Item createdItem = service.createItem(item);

        assertEquals("created", createdItem.getLabel());

        verify(repository, times(1)).save(item);
    }

    @Test
    void findItemById() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Item(ID, "testLabel", List.of())));

        Optional<Item> maybeItem = service.findItemById(ID);

        maybeItem.ifPresent(item -> assertEquals("testLabel", item.getLabel()));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAllItems() {
        ArrayList<Item> list = new ArrayList<>();

        Item item1 = new Item(ID, "testLabel", List.of());
        Item item2 = new Item(ID+1L, "testLabel2", List.of());

        list.add(item1);
        list.add(item2);

        when(repository.findAll()).thenReturn(list);

        List<Item> itemsFromService = service.findAllItems();

        assertEquals(2, itemsFromService.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateItem() {
        Item item = new Item(ID, "testLabel", List.of());

        when(repository.findById(ID)).thenReturn(Optional.of(item));

        Item updatedItem = service.updateItem(ID, new Item(ID, "testLabelUPD", List.of()));

        assertEquals("testLabelUPD", updatedItem.getLabel());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void deleteItem() {
        boolean delete = service.deleteItem(ID);
        repository.delete(new Item());

        assertTrue(delete);
        verify(repository, times(2)).findById(ID);
    }

    @Test
    void addResourceToItem() {
        Item item = new Item();

        Resource resource = new Resource(ID, "testName", "testDescription", "testUrl");

        when(repository.findById(ID)).thenReturn(Optional.of(item));
        when(resourceService.findResourceById(2L)).thenReturn(Optional.of(new Resource()));

        item.addResource(resource);
        service.addResourceToItem(1L, 2L);

        assertTrue(item.getResources().contains(resource));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    void removeResourceFromItem() {
        Item item = new Item();

        Resource resource = new Resource(ID, "testName", "testDescription", "testUrl");

        when(repository.findById(ID)).thenReturn(Optional.of(item));
        when(resourceService.findResourceById(2L)).thenReturn(Optional.of(new Resource()));

        item.removeResource(resource);
        service.removeResourceFromItem(1L, 2L);

        assertFalse(item.getResources().contains(resource));
        verify(repository, times(1)).findById(ID);
    }
}