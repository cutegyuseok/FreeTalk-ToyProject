package com.cutegyuseok.freetalk.category.dto;

import com.cutegyuseok.freetalk.category.entity.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "카테고리 생성", description = "이름,단계,부모 입력")
    public static class MakeCategory {
        @ApiModelProperty(value = "생성할 카테고리 이름", required = true)
        private String categoryName;
        @ApiModelProperty(value = "생성할 카테고리 단계 ( 1: 대분류, 2: 중분류, 3: 소분류 )", required = true)
        private int categoryLevel;
        @ApiModelProperty(value = "생성할 카테고리 부모 ID( 생성할 카테고리가 중분류, 소분류인 경우 필요, 대분류의 경우 아무값 넣어도 상관 없음.)", required = true)
        private Long categoryParent;


        public Category toChild(Category categoryParent) {
            return new Category(categoryName, categoryParent, categoryLevel);
        }

        public Category toParent() {
            return Category.builder()
                    .name(categoryName)
                    .level(categoryLevel)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewCategory {
        private Long categoryPk;
        private String categoryName;
        private int categoryLevel;
        private List<ViewCategory> children;

        public static ViewCategory of(Category category) {
            return new ViewCategory(
                    category.getPk(),
                    category.getName(),
                    category.getLevel(),
                    category.getChildren().stream().map(ViewCategory::of).collect(Collectors.toList())
            );
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "카테고리 이름 수정")
    public static class UpdateCategory {
        @ApiModelProperty(value = "카테고리 새 이름", required = true)
        private String categoryName;
    }

    @Getter
    public static class viewCategoryForProduct {
        private String categoryName;
        private Long categoryPk;

        public viewCategoryForProduct(Category category) {
            this.categoryName = category.getName();
            this.categoryPk = category.getPk();
        }
    }

}
