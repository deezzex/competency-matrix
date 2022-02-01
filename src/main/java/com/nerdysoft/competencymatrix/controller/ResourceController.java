package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.entity.dto.ResourceDto;
import com.nerdysoft.competencymatrix.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController()
@RequestMapping("/resource")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody ResourceDto resourceDto){
        Resource resource = resourceService.createResource(Resource.from(resourceDto));
        return new ResponseEntity<>(ResourceDto.from(resource), OK);
    }

    @GetMapping
    public ResponseEntity<List<ResourceDto>> getResource(){
        List<Resource> allResources = resourceService.findAllResources();
        List<ResourceDto> resourceDtoList = allResources.stream()
                .map(ResourceDto::from).collect(Collectors.toList());

        return new ResponseEntity<>(resourceDtoList, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDto> getOneResource(@PathVariable Long id){
        Optional<Resource> maybeResource = resourceService.findResourceById(id);

        if (maybeResource.isPresent()){
            Resource resource = maybeResource.get();

            return new ResponseEntity<>(ResourceDto.from(resource), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceDto> updateResource(@PathVariable Long id, @Valid @RequestBody ResourceDto resourceDto){
        Resource resource = resourceService.updateResource(id, Resource.from(resourceDto));

        if(Objects.nonNull(resource.getId())){
            return new ResponseEntity<>(ResourceDto.from(resource), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResourceDto> removeResource(@PathVariable Long id){
        try {
            resourceService.deleteResource(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }
}
