package com.nerdysoft.competencymatrix.repository;

import com.nerdysoft.competencymatrix.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
