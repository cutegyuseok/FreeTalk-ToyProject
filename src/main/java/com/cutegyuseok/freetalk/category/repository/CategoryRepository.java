package com.cutegyuseok.freetalk.category.repository;

import com.cutegyuseok.freetalk.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByParentIsNull();

    boolean existsByName(String name);

    List<Category> findAllByPkIn(List<Long> list);
}