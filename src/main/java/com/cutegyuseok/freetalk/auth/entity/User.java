package com.cutegyuseok.freetalk.auth.entity;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.enumType.UserRole;
import com.cutegyuseok.freetalk.auth.enumType.UserStatus;
import com.cutegyuseok.freetalk.community.entity.Join;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "pk",columnDefinition = "BINARY(16)")
    private UUID pk;

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
    public User(UUID pk, String email, String name, String nickName, String password, String profileImage, String selfIntroduction, String phone, LocalDate birthday, UserRole role, UserStatus status) {
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
    public void updateUserByAdmin(UserDTO.UpdateUserByAdmin dto) {
        this.email = (dto.getEmail() != null) ? dto.getEmail() : this.email;
        this.name = (dto.getName() != null) ? dto.getName() : this.name;
        this.nickName = (dto.getNickName() != null) ? dto.getNickName() : this.nickName;
        this.password = (dto.getPassword() != null) ? dto.getPassword() : this.password;
        this.profileImage = (dto.getProfileImage() != null) ? dto.getProfileImage() : this.profileImage;
        this.selfIntroduction = (dto.getSelfIntroduction() != null) ? dto.getSelfIntroduction() : this.selfIntroduction;
        this.phone = (dto.getPhone() != null) ? dto.getPhone() : this.phone;
        this.role = (dto.getRole() != null) ? dto.getRole() : this.role;
        this.status = (dto.getStatus() != null) ? dto.getStatus() : this.status;
    }
}
