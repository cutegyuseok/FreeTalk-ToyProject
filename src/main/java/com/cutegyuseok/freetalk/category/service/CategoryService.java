package com.cutegyuseok.freetalk.category.service;

import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CategoryService {
    ResponseEntity<?> makeCategory(CategoryDTO.MakeCategory dto);

    ResponseEntity<?> selectCategory();

    ResponseEntity<?> updateCategory(UUID categoryId, CategoryDTO.UpdateCategory dto);

    ResponseEntity<?> deleteCategory(UUID categoryId);

    ResponseEntity<?> viewCategoryDetail(UUID categoryId);
}
