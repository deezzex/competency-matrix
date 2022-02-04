package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    private final ResourceRepository repository;

    @Autowired
    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    public Resource createResource(Resource resource){
        return repository.save(resource);
    }

    public Optional<Resource> findResourceById(Long id){
        return repository.findById(id);
    }

    public List<Resource> findAllResources(){
        return repository.findAll();
    }

    @Transactional
    public Resource updateResource(Long id, Resource resourceData){
        Optional<Resource> maybeResource = repository.findById(id);

        if (maybeResource.isPresent()){
            Resource resource = maybeResource.get();

            resource.setName(resourceData.getName());
            resource.setDescription(resourceData.getDescription());
            resource.setUrl(resourceData.getUrl());

            return resource;
        }else
            return new Resource();

    }

    public boolean deleteResource(Long id){
        Optional<Resource> byId = repository.findById(id);

        if (byId.isPresent()){
            Resource resource = byId.get();
            repository.delete(resource);
        }
        Optional<Resource> removed = repository.findById(id);

        return removed.isEmpty();
    }
}
