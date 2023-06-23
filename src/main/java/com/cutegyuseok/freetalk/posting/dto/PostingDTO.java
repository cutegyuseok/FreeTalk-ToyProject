package com.cutegyuseok.freetalk.posting.dto;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.community.dto.CommunityDTO;
import com.cutegyuseok.freetalk.posting.entity.Comment;
import com.cutegyuseok.freetalk.posting.entity.Posting;
import com.cutegyuseok.freetalk.posting.enumType.PostingStatus;
import com.cutegyuseok.freetalk.posting.enumType.PostingType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PostingDTO {
    @Getter
    @NoArgsConstructor
    public static class UploadPosting {
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
    public static class UploadCommentDTO {
        @ApiModelProperty(value = "내용 ", required = true)
        private String contents;
        @ApiModelProperty(value = "이메일")
        private Long targetComment;
    }

    @Getter
    public static class ViewPosting {
        private final Long postingPK;
        private final UserDTO.ShowOwnerDTO writer;
        private final CommunityDTO.ShowCommunitySimpleDTO community;
        private final String title;
        private final String thumbnail;
        private final String contents;
        private final String hashtag;
        private final Long viewCount;
        private final String updatedDate;
        private final PostingStatus status;
        private final PostingType type;
        private final Long likes;
        private final Long dislikes;
        private final String whetherToVote;

        public ViewPosting(Posting posting, String whetherToVote) {
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
            this.dislikes = posting.getVoteList().size() - likeNum;
            this.whetherToVote = whetherToVote;
        }
    }

    @Getter
    @Setter
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


        public static PostingDTO.ViewComments of(Comment comment, User user) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");
            long likeNum = comment.likeNum();
            String tempContents = comment.getContents();
            if (comment.getStatus()!=PostingStatus.POSTED){
                tempContents = "삭제되거나 숨겨진 댓글 입니다.";
            }
            return new PostingDTO.ViewComments(
                    comment.getPk(),
                    tempContents,
                    new UserDTO.ShowOwnerSimpleDTO(comment.getUser()),
                    comment.getUpdatedDate().format(dateTimeFormatter),
                    comment.getStatus(),
                    likeNum,
                    comment.getVoteList().size() - likeNum,
                    comment.whetherToVote(user),
                    comment.getChildren().stream().map(e -> PostingDTO.ViewComments.of(e, user)).collect(Collectors.toList())
            );
        }
    }

    @Getter
    @NoArgsConstructor
    public static class VoteReqDTO {
        @ApiModelProperty(value = "타입 (POSTING or COMMENT", required = true)
        private String type;
        @ApiModelProperty(value = "좋아요:true or 싫어요:false", required = true)
        private Boolean like;
    }

    @Getter
    public static class PostingListDTO {
        private final Long postingPk;
        private final UserDTO.ShowOwnerSimpleDTO postingWriter;
        private final CommunityDTO.ShowCommunitySuperSimpleDTO postedCommunity;
        private final String title;
        private final String thumbnail;
        private final Long viewCount;
        private final String createdDate;
        private final PostingType postingType;
        private final Long likes;
        private final Long dislikes;

        public PostingListDTO(Posting posting) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");
            long likeNum = posting.likeNum();
            this.postingPk = posting.getPk();
            this.postingWriter = new UserDTO.ShowOwnerSimpleDTO(posting.getUser());
            this.postedCommunity = new CommunityDTO.ShowCommunitySuperSimpleDTO(posting.getCommunity());
            this.title = posting.getTitle();
            this.thumbnail = posting.getThumbnail();
            this.viewCount = posting.getViewCount();
            this.createdDate = posting.getCreatedDate().format(dateTimeFormatter);
            this.postingType = posting.getType();
            this.likes = likeNum;
            this.dislikes = posting.getVoteList().size() - likeNum;
        }
    }


}
