package com.cutegyuseok.freetalk.community.repository;

import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.entity.CommunityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {

    void deleteAllByCommunity(Community community);

    void deleteAllByCategory(Category category);
}
