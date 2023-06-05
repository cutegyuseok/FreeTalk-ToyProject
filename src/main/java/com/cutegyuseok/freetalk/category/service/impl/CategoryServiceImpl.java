package com.cutegyuseok.freetalk.category.service.impl;


import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.category.repository.CategoryRepository;
import com.cutegyuseok.freetalk.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public ResponseEntity<?> makeCategory(CategoryDTO.MakeCategory dto) {
        try {
            if (categoryRepository.existsByName(dto.getCategoryName())) {
                //중복되는 카테고리 방지
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            if (dto.getCategoryLevel() != 1) {
                Category parent = categoryRepository.findById(dto.getCategoryParent()).orElseThrow(IllegalArgumentException::new);
                //카테고리 부모-자녀 관계가 1촌 이상 일 경우 400
                if (parent.getLevel() != dto.getCategoryLevel() - 1) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                categoryRepository.save(dto.toChild(parent));
            } else {
                categoryRepository.save(dto.toParent());
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> selectCategory() {
        List<CategoryDTO.ViewCategory> list = categoryRepository.findAllByParentIsNull().stream().map(CategoryDTO.ViewCategory::of).collect(Collectors.toList());
        if (list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateCategory(Long categoryId, CategoryDTO.UpdateCategory dto) {
        try {
            if (categoryRepository.existsByName(dto.getCategoryName())) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            Category category = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
            category.setName(dto.getCategoryName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> deleteCategory(Long categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
            if (category.getChildren().size() > 0) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
//            커뮤니티-카테고리 연관 테이블에서 삭제
            categoryRepository.delete(category);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> viewCategoryDetail(Long categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
            return new ResponseEntity<>(CategoryDTO.ViewCategory.of(category), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
