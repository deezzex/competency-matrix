package com.nerdysoft.competencymatrix.repository;

import com.nerdysoft.competencymatrix.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query(value = "SELECT * FROM topic WHERE topic.required = true",
            nativeQuery = true)
    List<Topic> findAllRequiredTopics();

    @Query(value = "SELECT count(*) FROM topic WHERE required = true",
            nativeQuery = true)
    Integer countRequiredTopics();


    @Query(value = "SELECT count(*) FROM topic",
            nativeQuery = true)
    Integer countAllTopics();
}
