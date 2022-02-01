package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Item;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.dto.ItemDto;
import com.nerdysoft.competencymatrix.entity.dto.ResourceDto;
import com.nerdysoft.competencymatrix.entity.dto.TopicDto;
import com.nerdysoft.competencymatrix.service.TopicService;
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

@RestController
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<TopicDto> createTopic(@Valid @RequestBody TopicDto topicDto){
        Topic topic = topicService.createTopic(Topic.from(topicDto));
        return new ResponseEntity<>(TopicDto.from(topic), OK);
    }

    @GetMapping
    public ResponseEntity<List<TopicDto>> getTopics(){
        List<Topic> allTopics = topicService.findAllTopics();
        List<TopicDto> topicDtoList = allTopics.stream()
                .map(TopicDto::from).collect(Collectors.toList());

        return new ResponseEntity<>(topicDtoList, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getOneTopic(@PathVariable Long id){
        Optional<Topic> maybeTopic = topicService.findTopicById(id);

        if (maybeTopic.isPresent()){
            Topic topic = maybeTopic.get();

            return new ResponseEntity<>(TopicDto.from(topic), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable Long id, @Valid @RequestBody TopicDto topicDto){
        Topic topic = topicService.updateTopic(id, Topic.from(topicDto));

        if(Objects.nonNull(topic.getId())){
            return new ResponseEntity<>(TopicDto.from(topic), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TopicDto> removeTopic(@PathVariable Long id){
        try {
            topicService.deleteTopic(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{topicId}/add/item/{itemId}")
    public ResponseEntity<TopicDto> addItemToTopic(@PathVariable Long topicId, @PathVariable Long itemId){
        Topic topic = topicService.addItemToTopic(topicId, itemId);

        return new ResponseEntity<>(TopicDto.from(topic), OK);
    }

    @DeleteMapping("/{topicId}/delete/item/{itemId}")
    public ResponseEntity<TopicDto> deleteItemFromTopic(@PathVariable Long topicId, @PathVariable Long itemId){
        Topic topic = topicService.removeItemFromTopic(topicId, itemId);

        return new ResponseEntity<>(TopicDto.from(topic), OK);
    }

    @PostMapping("/{topicId}/add/resource/{resourceId}")
    public ResponseEntity<TopicDto> addResourceToTopic(@PathVariable Long topicId, @PathVariable Long resourceId){
        Topic topic = topicService.addResourceToTopic(topicId, resourceId);

        return new ResponseEntity<>(TopicDto.from(topic), OK);
    }

    @DeleteMapping("/{topicId}/delete/resource/{resourceId}")
    public ResponseEntity<TopicDto> deleteResourceFromTopic(@PathVariable Long topicId, @PathVariable Long resourceId){
        Topic topic = topicService.removeResourceFromTopic(topicId, resourceId);

        return new ResponseEntity<>(TopicDto.from(topic), OK);
    }
}
