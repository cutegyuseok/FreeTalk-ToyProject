package com.cutegyuseok.freetalk.posting.enumType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum PostingStatus {
    @JsonProperty("POSTED")
    POSTED,
    @JsonProperty("DELETED")
    DELETED,
    @JsonProperty("FORCE_DELETED")
    FORCE_DELETED,
    @JsonProperty("HIDING")
    HIDING
}
