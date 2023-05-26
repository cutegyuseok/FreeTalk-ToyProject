package com.cutegyuseok.freetalk.entity.enumType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum CommunityStatus {
    @JsonProperty("AVAILABLE")
    AVAILABLE,
    @JsonProperty("DELETED")
    DELETED,
    @JsonProperty("HIDING")
    HIDING
}
