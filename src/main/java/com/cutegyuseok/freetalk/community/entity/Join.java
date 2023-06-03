package com.cutegyuseok.freetalk.community.entity;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.community.entity.Community;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "joining")
public class Join {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community")
    private Community community;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Builder
    public Join(Long pk, User user, Community community) {
        this.pk = pk;
        this.user = user;
        this.community = community;
    }
}
