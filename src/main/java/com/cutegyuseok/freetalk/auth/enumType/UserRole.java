package com.cutegyuseok.freetalk.auth.enumType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum UserRole {
    @JsonProperty("ROLE_SUPER")
    ROLE_SUPER,
    @JsonProperty("ROLE_ADMIN")
    ROLE_ADMIN,
    @JsonProperty("ROLE_READ")
    ROLE_READ,
    @JsonProperty("ROLE_USER")
    ROLE_USER
}
