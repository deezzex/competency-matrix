package com.nerdysoft.competencymatrix.repository;

import com.nerdysoft.competencymatrix.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
