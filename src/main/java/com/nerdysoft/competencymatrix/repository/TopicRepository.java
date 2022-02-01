package com.nerdysoft.competencymatrix.repository;

import com.nerdysoft.competencymatrix.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
}
