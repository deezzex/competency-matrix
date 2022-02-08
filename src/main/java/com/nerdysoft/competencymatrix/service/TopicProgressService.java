package com.nerdysoft.competencymatrix.service;

import com.nerdysoft.competencymatrix.entity.Competency;
import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.TopicProgress;
import com.nerdysoft.competencymatrix.entity.access.EvaluatorProgressAccess;
import com.nerdysoft.competencymatrix.entity.access.ManagerCompetencyAccess;
import com.nerdysoft.competencymatrix.repository.TopicProgressRepository;
import com.nerdysoft.competencymatrix.repository.access.EvaluatorProgressRepository;
import com.nerdysoft.competencymatrix.service.utility.AccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TopicProgressService {

    private final TopicProgressRepository repository;
    private final TopicService topicService;
    private final EvaluatorProgressRepository accessRepository;

    @Autowired
    public TopicProgressService(TopicProgressRepository repository, TopicService topicService, EvaluatorProgressRepository accessRepository) {
        this.repository = repository;
        this.topicService = topicService;
        this.accessRepository = accessRepository;
    }

    public TopicProgress createProgress(TopicProgress progress){
        return repository.save(progress);
    }

    public Optional<TopicProgress> findProgressById(Long id){
        return repository.findById(id);
    }

    public List<TopicProgress> findAllProgresses(){
        return repository.findAll();
    }

    public boolean deleteProgress(Long id){
        Optional<TopicProgress> byId = repository.findById(id);

        if (byId.isPresent()){
            TopicProgress progress = byId.get();
            repository.delete(progress);
        }
        Optional<TopicProgress> removed = repository.findById(id);

        return removed.isEmpty();
    }

    @Transactional
    public TopicProgress addTopicToProgress(Long progressId, Long topicId){
        Optional<TopicProgress> maybeProgress = findProgressById(progressId);
        Optional<Topic> maybeTopic = topicService.findTopicById(topicId);
        if (maybeProgress.isPresent() && maybeTopic.isPresent()){
            TopicProgress topicProgress = maybeProgress.get();
            Topic topic = maybeTopic.get();

            topicProgress.setTopic(topic);

            return topicProgress;
        }else
            return new TopicProgress();
    }

    @Transactional
    public TopicProgress setCommentAndMarkToProgress(Long progressId, TopicProgress progress, UserDetails user){
        Optional<TopicProgress> maybeProgress = findProgressById(progressId);

        if (!AccessUtils.getAccessForEvaluator(user, progressId)){
            return new TopicProgress();
        }

        if (maybeProgress.isPresent()){
            TopicProgress topicProgress = maybeProgress.get();

            topicProgress.setComment(progress.getComment());
            topicProgress.setMark(progress.getMark());

            return topicProgress;
        }else
            return new TopicProgress();
    }

    @Transactional
    public TopicProgress finishProgress(Long progressId) {
        Optional<TopicProgress> maybeProgress = findProgressById(progressId);
        if (maybeProgress.isPresent()){
            TopicProgress topicProgress = maybeProgress.get();

            topicProgress.setFinished(true);

            return topicProgress;
        }else
            return new TopicProgress();
    }

    public boolean isSuccessfullyEndedLevel() {
        int finishedTopics = repository.countFinishedTopics();
        int allTopics = topicService.countAllTopics();
        int requiredTopics = topicService.countRequiredTopics();
        int finishedAndRequiredTopic = repository.countFinishedAndRequiredTopic();

        var percentage = (double) finishedTopics/allTopics;

        return requiredTopics==finishedAndRequiredTopic && percentage >= 0.8;
    }

}
