package com.cutegyuseok.freetalk.entity;

import com.cutegyuseok.freetalk.entity.enumType.CommunityStatus;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "community")
public class Community {

    @Id
    @Column(name = "pk")
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
    private List<CommunityCategory> communityCategoryList = new ArrayList<>();

    @Builder
    public Community(Long pk, String name, String introduce, CommunityStatus status, String mainImage, String backgroundImage) {
        this.pk = pk;
        this.name = name;
        this.introduce = introduce;
        this.status = status;
        this.mainImage = mainImage;
        this.backgroundImage = backgroundImage;
    }
}
