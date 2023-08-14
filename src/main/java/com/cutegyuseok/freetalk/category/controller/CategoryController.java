package com.cutegyuseok.freetalk.category.controller;

import com.cutegyuseok.freetalk.category.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Api(tags = {"카테고리 서비스"}, description = "카테고리 조회")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    @ApiOperation(value = "카테고리 목록 조회", notes = "카테고리 목록을 JSON 형태로 조회.\n\n" +
            "code: 200 조회 성공, 204 표시할 내용 없음")
    public ResponseEntity<?> getCategories() {
        return categoryService.selectCategory();
    }

    @GetMapping("/categories/{categoryId}")
    @ApiOperation(value = "카테고리 상세 조회", notes = "카테고리 아이디를 입력하면 상세정보 반환.\n\n" +
            "code: 200 조회 성공, 404 해당 카테고리 ID 가 없음.")
    public ResponseEntity<?> viewCategoryDetail(@PathVariable UUID categoryId) {
        return categoryService.viewCategoryDetail(categoryId);
    }
}
