package com.cutegyuseok.freetalk.posting.dto;

import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.posting.entity.Comment;
import com.cutegyuseok.freetalk.posting.enumType.PostingStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PostingDTO {
    @Getter
    @NoArgsConstructor
    public static class UploadPosting{

        private String title;
        private String thumbnail;
        private String contents;
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewComments {
        private Long commentPK;
        private String contents;
        private String userNickName;
        private String updatedDate;
        private PostingStatus status;
        private List<PostingDTO.ViewComments> children;

        public static PostingDTO.ViewComments of(Comment comment) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");
            return new PostingDTO.ViewComments(
                    comment.getPk(),
                    comment.getContents(),
                    comment.getUser().getNickName(),
                    comment.getUpdatedDate().format(dateTimeFormatter),
                    comment.getStatus(),
                    comment.getChildren().stream().map(PostingDTO.ViewComments::of).collect(Collectors.toList())
            );
        }
    }

}
