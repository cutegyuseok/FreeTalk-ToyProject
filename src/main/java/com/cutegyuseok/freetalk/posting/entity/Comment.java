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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<Vote> voteList = new ArrayList<>();

    public long likeNum() {
        for (Vote vote : voteList) {
            System.out.println("투표");
        }
        return voteList.stream().filter(vote -> vote.getLike() == 1).count();
    }

    public String whetherToVote(User user) {
        String result = "none";
        for (Vote vote : voteList) {
            if (vote.getUser() == user) {
                if (vote.getLike() == 1) {
                    result = "liked";
                } else if (vote.getLike() == -1) {
                    result = "disliked";
                }
            }
        }
        return result;
    }

    @Builder
    public Comment(User user, Posting posting, String contents, PostingStatus status, Comment parent) {
        this.user = user;
        this.posting = posting;
        this.contents = contents;
        this.status = status;
        this.parent = parent;
    }

    public void deletePosting() {
        this.status = PostingStatus.DELETED;
    }
    public void forceDeletePosting() {
        this.status = PostingStatus.FORCE_DELETED;
    }
}
