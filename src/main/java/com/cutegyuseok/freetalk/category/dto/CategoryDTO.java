package com.cutegyuseok.freetalk.category.dto;

import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.community.entity.CommunityCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
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
        private UUID categoryParent;


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
        private UUID categoryPk;
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
    @ApiModel(value = "카테고리 리스트 출력 DTO")
    public static class viewCategoryForCommunity {
        private final String categoryName;
        private final UUID categoryPk;

        public viewCategoryForCommunity(CommunityCategory communityCategory) {
            this.categoryName = communityCategory.getCategory().getName();
            this.categoryPk = communityCategory.getCategory().getPk();
        }
    }

}
