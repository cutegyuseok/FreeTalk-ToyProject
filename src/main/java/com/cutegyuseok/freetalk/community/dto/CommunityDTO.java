package com.cutegyuseok.freetalk.community.dto;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.enumType.CommunityStatus;
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

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "커뮤니티 단일 조회", description = "이름,소개,상태,")
    public static class ShowCommunityDTO {
        private Long pk;
        private String name;
        private String introduce;
        private CommunityStatus status;
        private String mainImage;
        private String backgroundImage;
        private List<CategoryDTO.viewCategoryForCommunity> categoryList;
        private UserDTO.ShowOwnerDTO owner;

        public ShowCommunityDTO(Community community, List<CategoryDTO.viewCategoryForCommunity> categoryList, UserDTO.ShowOwnerDTO owner) {
            this.pk = community.getPk();
            this.name = community.getName();
            this.introduce = community.getIntroduce();
            this.status = community.getStatus();
            this.mainImage = community.getMainImage();
            this.backgroundImage = community.getBackgroundImage();
            this.categoryList = categoryList;
            this.owner = owner;
        }
    }
}
