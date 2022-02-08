package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.dto.TopicDto;
import com.nerdysoft.competencymatrix.service.TopicService;
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
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @PostMapping
    public ResponseEntity<TopicDto> createTopic(@Valid @RequestBody TopicDto topicDto){
        Topic topic = topicService.createTopic(Topic.from(topicDto));
        return new ResponseEntity<>(TopicDto.from(topic), OK);
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @GetMapping
    public ResponseEntity<List<TopicDto>> getTopics(){
        List<Topic> allTopics = topicService.findAllTopics();
        List<TopicDto> topicDtoList = allTopics.stream()
                .map(TopicDto::from).collect(Collectors.toList());

        if (topicDtoList.isEmpty()){
            return new ResponseEntity<>(NO_CONTENT);
        }

        return new ResponseEntity<>(topicDtoList, OK);
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getOneTopic(@PathVariable Long id){
        Optional<Topic> maybeTopic = topicService.findTopicById(id);

        if (maybeTopic.isPresent()){
            Topic topic = maybeTopic.get();

            return new ResponseEntity<>(TopicDto.from(topic), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_EDIT_TOPIC')")
    @PutMapping("/{id}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable Long id, @Valid @RequestBody TopicDto topicDto){
        Topic topic = topicService.updateTopic(id, Topic.from(topicDto));

        if(Objects.nonNull(topic.getId())){
            return new ResponseEntity<>(TopicDto.from(topic), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<TopicDto> removeTopic(@PathVariable Long id){
        try {
            topicService.deleteTopic(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @PostMapping("/{topicId}/add/item/{itemId}")
    public ResponseEntity<TopicDto> addItemToTopic(@PathVariable Long topicId, @PathVariable Long itemId){
        Topic topic = topicService.addItemToTopic(topicId, itemId);

        if(Objects.nonNull(topic.getId())){
            return new ResponseEntity<>(TopicDto.from(topic), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @DeleteMapping("/{topicId}/delete/item/{itemId}")
    public ResponseEntity<TopicDto> deleteItemFromTopic(@PathVariable Long topicId, @PathVariable Long itemId){
        Topic topic = topicService.removeItemFromTopic(topicId, itemId);

        if(Objects.nonNull(topic.getId())){
            return new ResponseEntity<>(TopicDto.from(topic), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @PostMapping("/{topicId}/add/resource/{resourceId}")
    public ResponseEntity<TopicDto> addResourceToTopic(@PathVariable Long topicId, @PathVariable Long resourceId){
        Topic topic = topicService.addResourceToTopic(topicId, resourceId);

        if(Objects.nonNull(topic.getId())){
            return new ResponseEntity<>(TopicDto.from(topic), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_ALL')")
    @DeleteMapping("/{topicId}/delete/resource/{resourceId}")
    public ResponseEntity<TopicDto> deleteResourceFromTopic(@PathVariable Long topicId, @PathVariable Long resourceId){
        Topic topic = topicService.removeResourceFromTopic(topicId, resourceId);

        if(Objects.nonNull(topic.getId())){
            return new ResponseEntity<>(TopicDto.from(topic), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
