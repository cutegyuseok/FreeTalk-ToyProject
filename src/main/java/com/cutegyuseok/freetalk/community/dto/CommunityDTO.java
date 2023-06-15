package com.cutegyuseok.freetalk.community.dto;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.entity.Join;
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
    @ApiModel(value = "커뮤니티 생성", description = "이름,소개,메인 이미지,배경 이미지,카테고리 PK 리스트")
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
    @ApiModel(value = "커뮤니티 수정", description = "이름,소개,메인 이미지,배경 이미지,카테고리 id 리스트")
    public static class UpdateCommunityDTO {
        @ApiModelProperty(value = "수정할 커뮤니티 이름")
        private String communityName;
        @ApiModelProperty(value = "커뮤니티 소개")
        private String communityIntroduce;
        @ApiModelProperty(value = "커뮤니티 메인 이미지")
        private String communityMainImage;
        @ApiModelProperty(value = "커뮤니티 배경 이미지")
        private String communityBackgroundImage;
        @ApiModelProperty(value = "해당 커뮤니티 카테고리",required = true)
        private List<Long> categoryIdList = new ArrayList<>();

    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "커뮤니티 단일 조회")
    public static class ShowCommunityDTO {
        private Long pk;
        private String name;
        private String introduce;
        private CommunityStatus status;
        private String mainImage;
        private String backgroundImage;
        private List<CategoryDTO.viewCategoryForCommunity> categoryList;
        private UserDTO.ShowOwnerDTO owner;

        public ShowCommunityDTO(Community community, UserDTO.ShowOwnerDTO owner) {
            this.pk = community.getPk();
            this.name = community.getName();
            this.introduce = community.getIntroduce();
            this.status = community.getStatus();
            this.mainImage = community.getMainImage();
            this.backgroundImage = community.getBackgroundImage();
            this.categoryList = community.communityCategoryListDTO();
            this.owner = owner;
        }
    }
    @Getter
    @NoArgsConstructor
    @ApiModel(value = "커뮤니티 리스트 조회")
    public static class ShowCommunityListDTO {
        private Long pk;
        private String name;
        private String mainImage;
        private int joinedPeople;
        private int numberOfPostings;
        private List<CategoryDTO.viewCategoryForCommunity> categoryList;

        public ShowCommunityListDTO(Community community){
            this.pk = community.getPk();
            this.name = community.getName();
            this.mainImage = community.getMainImage();
            this.joinedPeople = community.getJoinedUsers().size();
            this.numberOfPostings = community.getPostingList().size();
            this.categoryList = community.communityCategoryListDTO();
        }
    }
    @Getter
    @ApiModel(value = "커뮤니티 간단 조회")
    public static class ShowCommunitySimpleDTO {
        private Long pk;
        private String name;
        private String mainImage;

        public ShowCommunitySimpleDTO(Community community){
            this.pk = community.getPk();
            this.name = community.getName();
            this.mainImage = community.getMainImage();
        }
    }
    @Getter
    @ApiModel(value = "커뮤니티 초간단 조회")
    public static class ShowCommunitySuperSimpleDTO {
        private Long pk;
        private String name;

        public ShowCommunitySuperSimpleDTO(Community community){
            this.pk = community.getPk();
            this.name = community.getName();
        }
    }
}
