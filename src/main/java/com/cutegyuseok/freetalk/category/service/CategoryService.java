package com.cutegyuseok.freetalk.category.service;

import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<?> makeCategory(CategoryDTO.MakeCategory dto);

    ResponseEntity<?> selectCategory();

    ResponseEntity<?> updateCategory(Long categoryId, CategoryDTO.UpdateCategory dto);

    ResponseEntity<?> deleteCategory(Long categoryId);

    ResponseEntity<?> viewCategoryDetail(Long categoryId);
}
