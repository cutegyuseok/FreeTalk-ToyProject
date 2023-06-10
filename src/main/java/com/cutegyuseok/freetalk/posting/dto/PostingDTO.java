package com.cutegyuseok.freetalk.posting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

public class PostingDTO {
    @Getter
    @NoArgsConstructor
    public static class UploadPosting{

        private String title;
        private String thumbnail;
        private String contents;
        private String hashtag;
    }
}
