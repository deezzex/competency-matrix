package com.nerdysoft.competencymatrix.repository;

import com.nerdysoft.competencymatrix.entity.Matrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatrixRepository extends JpaRepository<Matrix, Long> {
}
