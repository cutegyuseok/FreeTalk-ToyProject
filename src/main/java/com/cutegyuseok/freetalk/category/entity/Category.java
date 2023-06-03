package com.cutegyuseok.freetalk.category.entity;

import com.cutegyuseok.freetalk.community.entity.CommunityCategory;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<CommunityCategory> communityCategory = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Category parent;

    @Column(name = "level")
    private int level;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    @Builder
    public Category(String name, Category parent, int level) {
        this.name = name;
        this.parent = parent;
        this.level = level;
    }
}
