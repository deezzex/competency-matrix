package com.nerdysoft.competencymatrix.controller;

import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.dto.LevelDto;
import com.nerdysoft.competencymatrix.entity.dto.ResourceDto;
import com.nerdysoft.competencymatrix.entity.dto.TopicDto;
import com.nerdysoft.competencymatrix.service.LevelService;
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
@RequestMapping("/level")
@PreAuthorize("hasAuthority('CAN_ALL')")
public class LevelController {

    private final LevelService levelService;

    @Autowired
    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @PostMapping
    public ResponseEntity<LevelDto> createLevel(@Valid @RequestBody LevelDto levelDto){
        Level level = levelService.createLevel(Level.from(levelDto));
        return new ResponseEntity<>(LevelDto.from(level), OK);
    }

    @GetMapping
    public ResponseEntity<List<LevelDto>> getLevels(){
        List<Level> allLevels = levelService.findAllLevels();
        List<LevelDto> levelDtoList = allLevels.stream()
                .map(LevelDto::from).collect(Collectors.toList());

        if (levelDtoList.isEmpty()){
            return new ResponseEntity<>(NO_CONTENT);
        }

        return new ResponseEntity<>(levelDtoList, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LevelDto> getOneLevel(@PathVariable Long id){
        Optional<Level> maybeLevel = levelService.findLevelById(id);

        if (maybeLevel.isPresent()){
            Level level = maybeLevel.get();

            return new ResponseEntity<>(LevelDto.from(level), OK);
        }else
            return new ResponseEntity<>(NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LevelDto> updateLevel(@PathVariable Long id, @Valid @RequestBody LevelDto levelDto){
        Level level = levelService.updateLevel(id, Level.from(levelDto));

        if(Objects.nonNull(level.getId())){
            return new ResponseEntity<>(LevelDto.from(level), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LevelDto> removeLevel(@PathVariable Long id){
        try {
            levelService.deleteLevel(id);
            return new ResponseEntity<>(OK);
        }catch (Exception e){
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{levelId}/add/{topicId}")
    public ResponseEntity<LevelDto> addTopicToLevel(@PathVariable Long levelId, @PathVariable Long topicId){
        Level level = levelService.addTopicToLevel(levelId, topicId);

        if(Objects.nonNull(level.getId())){
            return new ResponseEntity<>(LevelDto.from(level), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{levelId}/delete/{topicId}")
    public ResponseEntity<LevelDto> deleteTopicFromLevel(@PathVariable Long levelId, @PathVariable Long topicId){
        Level level = levelService.removeTopicFromLevel(levelId, topicId);

        if(Objects.nonNull(level.getId())){
            return new ResponseEntity<>(LevelDto.from(level), HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
