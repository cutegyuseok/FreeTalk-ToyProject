package com.cutegyuseok.freetalk.posting.entity;

import com.cutegyuseok.freetalk.auth.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "vote")
public class Vote {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting")
    private Posting posting;

    @Column(name = "liked")
    private Integer like;

    @Builder
    public Vote(Long pk, User user, Comment comment, Posting posting, Integer like) {
        this.pk = pk;
        this.user = user;
        this.comment = comment;
        this.posting = posting;
        this.like = like;
    }
}
