package com.cutegyuseok.freetalk.posting.entity;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.posting.enumType.PostingStatus;
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
@Table(name = "comment")
public class Comment {

    @Id
    @Column(name = "pk")
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "posting")
    private Posting posting;

    @Column(name = "contents", nullable = false)
    private String contents;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostingStatus status;

    @Column(name = "lelvel")
    private int level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(Long pk, User user, Posting posting, String contents, PostingStatus status, int level, Comment parent) {
        this.pk = pk;
        this.user = user;
        this.posting = posting;
        this.contents = contents;
        this.status = status;
        this.level = level;
        this.parent = parent;
    }
}
