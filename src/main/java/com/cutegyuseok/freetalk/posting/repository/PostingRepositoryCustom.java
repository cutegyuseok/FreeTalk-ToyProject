package com.cutegyuseok.freetalk.posting.repository;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.posting.entity.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

public interface PostingRepositoryCustom {
    Page<Posting> search(String keyword, String keywordType, String sort, PageRequest pageRequest, Community community, User user, Integer likes, Integer viewCount, LocalDate start, LocalDate end, String postingType);
}
