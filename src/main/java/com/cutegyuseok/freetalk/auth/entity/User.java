package com.cutegyuseok.freetalk.auth.entity;

import com.cutegyuseok.freetalk.auth.enumType.UserRole;
import com.cutegyuseok.freetalk.auth.enumType.UserStatus;
import com.cutegyuseok.freetalk.community.entity.Join;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "self_introduction")
    private String selfIntroduction;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private final List<Join> joinedCommunity = new ArrayList<>();

    @Builder
    public User(Long pk, String email, String name, String nickName, String password, String profileImage, String selfIntroduction, String phone, LocalDate birthday, UserRole role, UserStatus status) {
        this.pk = pk;
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.password = password;
        this.profileImage = profileImage;
        this.selfIntroduction = selfIntroduction;
        this.phone = phone;
        this.birthday = birthday;
        this.role = role;
        this.status = status;
    }

    public void changeUserStatus(UserStatus status) {
        this.status = status;
    }

    public void changeUserRole(UserRole role) {
        this.role = role;
    }

    public void changeUserProfile(String nickName, String profileImage, String selfIntroduction) {
        if (nickName != null) {
            this.nickName = nickName;
        }
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
        if (selfIntroduction != null) {
            this.selfIntroduction = selfIntroduction;
        }
    }
}
