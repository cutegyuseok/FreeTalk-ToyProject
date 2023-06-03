package com.cutegyuseok.freetalk.community.entity;

import com.cutegyuseok.freetalk.category.entity.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_category")
public class CommunityCategory {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @Builder
    public CommunityCategory(Community community, Category category) {
        this.community = community;
        this.category = category;
    }
}
