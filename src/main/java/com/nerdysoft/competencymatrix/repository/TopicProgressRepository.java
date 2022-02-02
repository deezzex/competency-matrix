package com.nerdysoft.competencymatrix.repository;

import com.nerdysoft.competencymatrix.entity.Topic;
import com.nerdysoft.competencymatrix.entity.TopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicProgressRepository extends JpaRepository<TopicProgress, Long> {
    @Query(value = "SELECT * FROM progress WHERE progress.finished = true",
            nativeQuery = true)
    List<Topic> findAllFinishedTopics();

    @Query(value = "SELECT count(*) FROM  progress WHERE finished = true",
            nativeQuery = true)
    Integer countFinishedTopics();

    @Query(value = "SELECT count(*) FROM  progress\n" +
            "JOIN topic as t ON progress.topic_id = t.id\n" +
            "WHERE progress.finished = true AND t.required = true;",
            nativeQuery = true)
    Integer countFinishedAndRequiredTopic();
}



