package com.cutegyuseok.freetalk.posting.entity;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.posting.enumType.PostingStatus;
import com.cutegyuseok.freetalk.posting.enumType.PostingType;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posting")
public class Posting {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "community")
    private Community community;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "hashtag", nullable = false)
    private String hashtag;

    @Column(name = "view_count")
    private Long viewCount;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostingStatus status;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostingType type;
    @Builder
    public Posting(User user, Community community, String title, String thumbnail, String contents, String hashtag, PostingStatus status, PostingType type) {
        this.user = user;
        this.community = community;
        this.title = title;
        this.thumbnail = thumbnail;
        this.contents = contents;
        this.hashtag = hashtag;
        this.status = status;
        this.type = type;
    }
}
