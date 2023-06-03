package com.cutegyuseok.freetalk.posting.enumType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum PostingType {
    @JsonProperty("NOTIFICATION")
    NOTIFICATION,
    @JsonProperty("NORMAL")
    NORMAL,
    @JsonProperty("VERIFIED")
    VERIFIED
}
