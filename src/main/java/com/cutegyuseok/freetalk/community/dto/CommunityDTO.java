package com.cutegyuseok.freetalk.community.dto;

import com.cutegyuseok.freetalk.category.entity.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class CommunityDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "커뮤니티 생성", description = "이름,소개,상태,")
    public static class MakeCommunityDTO {
        @ApiModelProperty(value = "생성할 커뮤니티 이름", required = true)
        private String communityName;
        @ApiModelProperty(value = "커뮤니티 소개", required = true)
        private String communityIntroduce;
        @ApiModelProperty(value = "커뮤니티 메인 이미지", required = true)
        private String communityMainImage;
        @ApiModelProperty(value = "커뮤니티 배경 이미지", required = true)
        private String communityBackgroundImage;
        @ApiModelProperty(value = "해당 커뮤니티 카테고리", required = true)
        private List<Long> categoryIdList = new ArrayList<>();

    }
}
