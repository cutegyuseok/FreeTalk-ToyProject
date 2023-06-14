package com.cutegyuseok.freetalk.posting.dto;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.community.dto.CommunityDTO;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.posting.entity.Comment;
import com.cutegyuseok.freetalk.posting.entity.Posting;
import com.cutegyuseok.freetalk.posting.enumType.PostingStatus;
import com.cutegyuseok.freetalk.posting.enumType.PostingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PostingDTO {
    @Getter
    @NoArgsConstructor
    public static class UploadPosting{
        @ApiModelProperty(value = "제목 ", required = true)
        private String title;
        @ApiModelProperty(value = "썸네일 ")
        private String thumbnail;
        @ApiModelProperty(value = "내용 ", required = true)
        private String contents;
        @ApiModelProperty(value = "해쉬테그 ", required = true)
        private String hashtag;
    }
    @Getter
    @NoArgsConstructor
    public static class UploadCommentDTO{
        @ApiModelProperty(value = "내용 ", required = true)
        private String contents;
        @ApiModelProperty(value = "이메일")
        private Long targetComment;
    }

    @Getter
    public static class ViewPosting{
        private Long postingPK;
        private UserDTO.ShowOwnerDTO writer;
        private CommunityDTO.ShowCommunitySimpleDTO community;
        private String title;
        private String thumbnail;
        private String contents;
        private String hashtag;
        private Long viewCount;
        private String updatedDate;
        private PostingStatus status;
        private PostingType type;
        private Long likes;
        private Long dislikes;
        private String whetherToVote;

        public ViewPosting(Posting posting,String whetherToVote){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");
            long likeNum = posting.likeNum();
            this.postingPK = posting.getPk();
            this.writer = new UserDTO.ShowOwnerDTO(posting.getUser());
            this.community = new CommunityDTO.ShowCommunitySimpleDTO(posting.getCommunity());
            this.title = posting.getTitle();
            this.thumbnail = posting.getThumbnail();
            this.contents = posting.getContents();
            this.hashtag = posting.getHashtag();
            this.viewCount = posting.getViewCount();
            this.updatedDate = posting.getUpdatedDate().format(dateTimeFormatter);
            this.status = posting.getStatus();
            this.type = posting.getType();
            this.likes = likeNum;
            this.dislikes = posting.getVoteList().size()-likeNum;
            this.whetherToVote = whetherToVote;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ViewComments {
        private Long commentPK;
        private String contents;
        private UserDTO.ShowOwnerSimpleDTO commentWriter;
        private String updatedDate;
        private PostingStatus status;
        private Long likes;
        private Long dislikes;
        private String whetherToVote;
        private List<PostingDTO.ViewComments> children;


        public static PostingDTO.ViewComments of(Comment comment,User user) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");
            long likeNum = comment.likeNum();
            return new PostingDTO.ViewComments(
                    comment.getPk(),
                    comment.getContents(),
                    new UserDTO.ShowOwnerSimpleDTO(comment.getUser()),
                    comment.getUpdatedDate().format(dateTimeFormatter),
                    comment.getStatus(),
                    likeNum,
                    comment.getVoteList().size()-likeNum,
                    comment.whetherToVote(user),
                    comment.getChildren().stream().map(e -> PostingDTO.ViewComments.of(e,user)).collect(Collectors.toList())
            );
        }
    }

    @Getter
    @NoArgsConstructor
    public static class VoteReqDTO{
        @ApiModelProperty(value = "타입 (POSTING or COMMENT", required = true)
        private String type;
        @ApiModelProperty(value = "좋아요:true or 싫어요:false", required = true)
        private Boolean like;
    }

}
