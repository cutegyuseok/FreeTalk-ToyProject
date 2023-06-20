package com.cutegyuseok.freetalk.auth.enumType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public enum UserStatus {
    @JsonProperty("AVAILABLE")
    AVAILABLE,
    @JsonProperty("BANNED")
    BANNED,
    @JsonProperty("WITHDRAWAL")
    WITHDRAWAL,
    @JsonProperty("DORMANT")
    DORMANT,
    @JsonProperty("NOT_AUTHORIZED")
    NOT_AUTHORIZED;

    @JsonCreator
    public static UserStatus from(String status) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.name().equals(status)) {
                return userStatus;
            }
        }
        throw new NoSuchElementException();
    }
}
