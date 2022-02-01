package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Item;
import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    private final TopicRepository repository;
    private final ItemService itemService;
    private final ResourceService resourceService;

    @Autowired
    public TopicService(TopicRepository repository, ItemService itemService, ResourceService resourceService) {
        this.repository = repository;
        this.itemService = itemService;
        this.resourceService = resourceService;
    }

    public Topic createTopic(Topic topic){
        return repository.save(topic);
    }

    public Optional<Topic> findTopicById(Long id){
        return repository.findById(id);
    }

    public List<Topic> findAllTopics(){
        return repository.findAll();
    }

    @Transactional
    public Topic updateTopic(Long id, Topic topicData){
        Optional<Topic> maybeTopic = repository.findById(id);

        if (maybeTopic.isPresent()){
            Topic topic = maybeTopic.get();

            topic.setName(topicData.getName());
            topic.setDescription(topicData.getDescription());
            topic.setRequired(topicData.isRequired());
            topic.setPriority(topicData.getPriority());

            return topic;
        }else
            return new Topic();
    }

    public void deleteTopic(Long id){
        repository.deleteById(id);
    }

    @Transactional
    public Topic addResourceToTopic(Long topicId, Long resourceId){
        Optional<Topic> maybeTopic = findTopicById(topicId);
        Optional<Resource> maybeResource = resourceService.findResourceById(resourceId);
        if (maybeTopic.isPresent() && maybeResource.isPresent()){
            Topic topic = maybeTopic.get();
            Resource resource = maybeResource.get();

            topic.addResource(resource);

            return topic;
        }else
            return new Topic();
    }

    @Transactional
    public Topic removeResourceFromTopic(Long topicId, Long resourceId){
        Optional<Topic> maybeTopic = findTopicById(topicId);
        Optional<Resource> maybeResource = resourceService.findResourceById(resourceId);
        if (maybeTopic.isPresent() && maybeResource.isPresent()){
            Topic topic = maybeTopic.get();
            Resource resource = maybeResource.get();

            topic.removeResource(resource);

            return topic;
        }else
            return new Topic();
    }

    @Transactional
    public Topic addItemToTopic(Long topicId, Long itemId){
        Optional<Topic> maybeTopic = findTopicById(topicId);
        Optional<Item> maybeItem = itemService.findItemById(itemId);
        if (maybeTopic.isPresent() && maybeItem.isPresent()){
            Topic topic = maybeTopic.get();
            Item item = maybeItem.get();

            topic.addItem(item);

            return topic;
        }else
            return new Topic();
    }

    @Transactional
    public Topic removeItemFromTopic(Long topicId, Long itemId){
        Optional<Topic> maybeTopic = findTopicById(topicId);
        Optional<Item> maybeItem = itemService.findItemById(itemId);
        if (maybeTopic.isPresent() && maybeItem.isPresent()){
            Topic topic = maybeTopic.get();
            Item item = maybeItem.get();

            topic.removeItem(item);

            return topic;
        }else
            return new Topic();
    }
}
