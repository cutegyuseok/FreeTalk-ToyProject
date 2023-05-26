package com.cutegyuseok.freetalk.entity.enumType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum UserStatus {
    @JsonProperty("AVAILABLE")
    AVAILABLE,
    @JsonProperty("BANNED")
    BANNED,
    @JsonProperty("WITHDRAWAL")
    WITHDRAWAL,
    @JsonProperty("DORMANT")
    DORMANT
}
