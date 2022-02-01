package com.nerdysoft.competencymatrix.repository;

import com.nerdysoft.competencymatrix.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
