package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.TopicProgress;
import com.nerdysoft.competencymatrix.entity.dto.TopicProgressDto;
import com.nerdysoft.competencymatrix.service.TopicProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/progress")
public class TopicProgressController {

    private final TopicProgressService topicProgressService;

    @Autowired
    public TopicProgressController(TopicProgressService topicProgressService) {
        this.topicProgressService = topicProgressService;
    }

    @PreAuthorize("hasAuthority('CAN_CREATE_PROGRESS')")
    @PostMapping
    public ResponseEntity<TopicProgressDto> createProgress(@RequestBody TopicProgressDto progressDto){
        TopicProgress progress = topicProgressService.createProgress(TopicProgress.from(progressDto));
        return new ResponseEntity<>(progressDto, OK);
    }

    @PreAuthorize("hasAuthority('CAN_CREATE_PROGRESS')")
    @PostMapping("/{progressId}/add/{topicId}")
    public ResponseEntity<TopicProgressDto> addTopicToProgress(@PathVariable Long progressId, @PathVariable Long topicId){
        TopicProgress progress = topicProgressService.addTopicToProgress(progressId, topicId);

        return new ResponseEntity<>(TopicProgressDto.fromForAdding(progress), OK);
    }

    @PreAuthorize("hasAuthority('CAN_END_LEVEL')")
    @GetMapping("/end")
    public ResponseEntity<Boolean> checkEnd(){
        return new ResponseEntity<>(topicProgressService.isSuccessfullyEndedLevel(), OK);
    }

    @PreAuthorize("hasAuthority('CAN_EVALUATE_PROGRESS')")
    @PutMapping("/evaluate/{id}")
    public ResponseEntity<TopicProgressDto> evaluateProgress(@PathVariable Long id, @RequestBody TopicProgressDto progressDto,
                                                             @AuthenticationPrincipal UserDetails user){

        TopicProgress progress = topicProgressService.setCommentAndMarkToProgress(id, TopicProgress.from(progressDto), user);

        if(Objects.nonNull(progress.getId())){
            return new ResponseEntity<>(TopicProgressDto.fromForAdding(progress), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('CAN_FINISH_PROGRESS')")
    @PutMapping("/finish/{id}")
    public ResponseEntity<TopicProgressDto> finishProgress(@PathVariable Long id){
        TopicProgress progress = topicProgressService.finishProgress(id);

        if(Objects.nonNull(progress.getId())){
            return new ResponseEntity<>(TopicProgressDto.fromForAdding(progress), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
