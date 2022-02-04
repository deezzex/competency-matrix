package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Item;
import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository repository;
    private final ResourceService resourceService;

    @Autowired
    public ItemService(ItemRepository repository, ResourceService resourceService) {
        this.repository = repository;
        this.resourceService = resourceService;
    }

    public Item createItem(Item item){
        return repository.save(item);
    }

    public Optional<Item> findItemById(Long id){
        return repository.findById(id);
    }

    public List<Item> findAllItems(){
        return repository.findAll();
    }

    @Transactional
    public Item updateItem(Long id, Item itemData){
        Optional<Item> maybeItem = repository.findById(id);

        if (maybeItem.isPresent()){
            Item item = maybeItem.get();

            item.setLabel(itemData.getLabel());

            return item;
        }else
            return new Item();
    }

    public boolean deleteItem(Long id){
        Optional<Item> byId = repository.findById(id);

        if (byId.isPresent()){
            Item item = byId.get();
            repository.delete(item);
        }
        Optional<Item> removed = repository.findById(id);

        return removed.isEmpty();
    }

    @Transactional
    public Item addResourceToItem(Long itemId, Long resourceId){
        Optional<Item> maybeItem = findItemById(itemId);
        Optional<Resource> maybeResource = resourceService.findResourceById(resourceId);
        if (maybeItem.isPresent() && maybeResource.isPresent()){
            Item item = maybeItem.get();
            Resource resource = maybeResource.get();

            item.addResource(resource);

            return item;
        }else
            return new Item();
    }

    @Transactional
    public Item removeResourceFromItem(Long itemId, Long resourceId){
        Optional<Item> maybeItem = findItemById(itemId);
        Optional<Resource> maybeResource = resourceService.findResourceById(resourceId);
        if (maybeItem.isPresent() && maybeResource.isPresent()){
            Item item = maybeItem.get();
            Resource resource = maybeResource.get();

            item.removeResource(resource);

            return item;
        }else
            return new Item();
    }
}
