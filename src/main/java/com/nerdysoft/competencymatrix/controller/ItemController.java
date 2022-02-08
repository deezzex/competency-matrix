package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Item;
import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.entity.dto.ItemDto;
import com.nerdysoft.competencymatrix.entity.dto.ResourceDto;
import com.nerdysoft.competencymatrix.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/item")
@PreAuthorize("hasAuthority('CAN_ALL')")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto){
        Item item = itemService.createItem(Item.from(itemDto));
        return new ResponseEntity<>(ItemDto.from(item), OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems(){
        List<Item> allItems = itemService.findAllItems();
        List<ItemDto> itemDtoList = allItems.stream()
                .map(ItemDto::from).collect(Collectors.toList());

        if (itemDtoList.isEmpty()){
            return new ResponseEntity<>(NO_CONTENT);
        }

        return new ResponseEntity<>(itemDtoList, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getOneItem(@PathVariable Long id){
        Optional<Item> maybeItem = itemService.findItemById(id);

        if (maybeItem.isPresent()){
            Item item = maybeItem.get();

            return new ResponseEntity<>(ItemDto.from(item), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDto itemDto){
        Item item = itemService.updateItem(id, Item.from(itemDto));

        if(Objects.nonNull(item.getId())){
            return new ResponseEntity<>(ItemDto.from(item), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemDto> removeItem(@PathVariable Long id){
        try {
            itemService.deleteItem(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{itemId}/add/{resourceId}")
    public ResponseEntity<ItemDto> addResourceToItem(@PathVariable Long itemId, @PathVariable Long resourceId){
        Item item = itemService.addResourceToItem(itemId, resourceId);

        if(Objects.nonNull(item.getId())){
            return new ResponseEntity<>(ItemDto.from(item), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{itemId}/delete/{resourceId}")
    public ResponseEntity<ItemDto> removeResourceFromItem(@PathVariable Long itemId, @PathVariable Long resourceId){
        Item item = itemService.removeResourceFromItem(itemId, resourceId);

        if(Objects.nonNull(item.getId())){
            return new ResponseEntity<>(ItemDto.from(item), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
