package com.nerdysoft.competencymatrix.repository;

import com.nerdysoft.competencymatrix.entity.Competency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetencyRepository extends JpaRepository<Competency, Long> {
}
