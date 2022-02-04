package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Category;
import com.nerdysoft.competencymatrix.entity.Level;
import com.nerdysoft.competencymatrix.entity.Resource;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LevelService {
    private final LevelRepository repository;
    private final TopicService topicService;

    @Autowired
    public LevelService(LevelRepository repository, TopicService topicService) {
        this.repository = repository;
        this.topicService = topicService;
    }

    public Level createLevel(Level level){
        return repository.save(level);
    }

    public Optional<Level> findLevelById(Long id){
        return repository.findById(id);
    }

    public List<Level> findAllLevels(){
        return repository.findAll();
    }

    @Transactional
    public Level updateLevel(Long id, Level levelData){
        Optional<Level> maybeLevel = repository.findById(id);

        if (maybeLevel.isPresent()){
            Level level = maybeLevel.get();

            level.setName(levelData.getName());
            level.setDescription(levelData.getDescription());


            return level;
        }else
            return new Level();
    }

    public boolean deleteLevel(Long id){
        Optional<Level> byId = repository.findById(id);

        if (byId.isPresent()){
            Level level = byId.get();
            repository.delete(level);
        }
        Optional<Level> removed = repository.findById(id);

        return removed.isEmpty();
    }

    @Transactional
    public Level addTopicToLevel(Long levelId, Long topicId){
        Optional<Level> maybeLevel = findLevelById(levelId);
        Optional<Topic> maybeTopic = topicService.findTopicById(topicId);
        if (maybeLevel.isPresent() && maybeTopic.isPresent()){
            Level level = maybeLevel.get();
            Topic topic = maybeTopic.get();

            level.addTopic(topic);

            return level;
        }else
            return new Level();
    }

    @Transactional
    public Level removeTopicFromLevel(Long levelId, Long topicId){
        Optional<Level> maybeLevel = findLevelById(levelId);
        Optional<Topic> maybeTopic = topicService.findTopicById(topicId);
        if (maybeLevel.isPresent() && maybeTopic.isPresent()){
            Level level = maybeLevel.get();
            Topic topic = maybeTopic.get();

            level.removeTopic(topic);

            return level;
        }else
            return new Level();
    }
}
