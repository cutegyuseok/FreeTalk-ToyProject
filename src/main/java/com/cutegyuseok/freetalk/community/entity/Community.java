package com.cutegyuseok.freetalk.community.entity;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import com.cutegyuseok.freetalk.community.dto.CommunityDTO;
import com.cutegyuseok.freetalk.community.enumType.CommunityStatus;
import com.cutegyuseok.freetalk.posting.entity.Posting;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "community")
public class Community {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "introduce", nullable = false)
    private String introduce;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CommunityStatus status;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @Column(name = "background_image", nullable = false)
    private String backgroundImage;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<CommunityCategory> communityCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    private final List<Join> joinedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    private final List<Posting> postingList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @Builder
    public Community(String name, String introduce, String mainImage, String backgroundImage, User user, CommunityStatus status) {
        this.name = name;
        this.introduce = introduce;
        this.mainImage = mainImage;
        this.backgroundImage = backgroundImage;
        this.user = user;
        this.status = status;
    }

    public List<CategoryDTO.viewCategoryForCommunity> communityCategoryListDTO() {
        return communityCategoryList
                .stream()
                .map(CategoryDTO.viewCategoryForCommunity::new)
                .collect(Collectors.toList());
    }

    public void updateCommunity(CommunityDTO.UpdateCommunityDTO dto) {
        if (dto.getCommunityName() != null) {
            if (!dto.getCommunityName().isBlank()) {
                this.name = dto.getCommunityName();
            }
        }
        if (dto.getCommunityIntroduce() != null) {
            if (!dto.getCommunityIntroduce().isBlank()) {
                this.introduce = dto.getCommunityIntroduce();
            }
        }
        if (dto.getCommunityMainImage() != null) {
            if (!dto.getCommunityMainImage().isBlank()) {
                this.mainImage = dto.getCommunityMainImage();
            }
        }
        if (dto.getCommunityBackgroundImage() != null) {
            if (!dto.getCommunityBackgroundImage().isBlank()) {
                this.backgroundImage = dto.getCommunityBackgroundImage();
            }
        }
    }
}
