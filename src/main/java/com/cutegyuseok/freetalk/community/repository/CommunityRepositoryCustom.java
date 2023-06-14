package com.cutegyuseok.freetalk.community.repository;

import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommunityRepositoryCustom {
    Page<Community> search(Pageable pageable, String keyword, String sort, Category category);
}
