package com.cutegyuseok.freetalk.auth.enumType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public enum UserRole {
    @JsonProperty("ROLE_SUPER")
    ROLE_SUPER("ROLE_SUPER", 0),
    @JsonProperty("ROLE_WRITE")
    ROLE_WRITE("ROLE_WRITE", 1),
    @JsonProperty("ROLE_READ")
    ROLE_READ("ROLE_READ", 2),
    @JsonProperty("ROLE_USER")
    ROLE_USER("ROLE_USER", 3),
    @JsonProperty("ROLE_NOT_AUTHORIZED")
    ROLE_NOT_AUTHORIZED("ROLE_NOT_AUTHORIZED", 4);

    @Getter
    private final String value;

    @Getter
    private final int grade;

    UserRole(String value, int grade) {
        this.value = value;
        this.grade = grade;
    }

    @JsonCreator
    public static UserRole from(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.value.equals(role)) {
                return userRole;
            }
        }
        throw new NoSuchElementException();
    }
}
