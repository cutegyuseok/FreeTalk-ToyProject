package com.cutegyuseok.freetalk.entity.enumType;

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
