package com.cutegyuseok.freetalk.community.repository;

import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityRepositoryCustom {
    Page<Community> search(Pageable pageable, String keyword, String sort, Category category);
}
