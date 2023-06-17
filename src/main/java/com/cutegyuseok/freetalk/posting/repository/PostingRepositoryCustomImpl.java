package com.cutegyuseok.freetalk.posting.repository;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.posting.entity.Posting;
import com.cutegyuseok.freetalk.posting.enumType.PostingStatus;
import com.cutegyuseok.freetalk.posting.enumType.PostingType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;

import static com.cutegyuseok.freetalk.auth.entity.QUser.user;
import static com.cutegyuseok.freetalk.community.entity.QCommunity.community;
import static com.cutegyuseok.freetalk.posting.entity.QPosting.posting;
import static com.cutegyuseok.freetalk.posting.entity.QVote.vote;


public class PostingRepositoryCustomImpl extends QuerydslRepositorySupport implements PostingRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public PostingRepositoryCustomImpl() {
        super(Posting.class);
    }

    @Override
    public Page<Posting> search(String keyword, String keywordType, String sort, PageRequest pageRequest, Community communityReq, User userReq, Integer likes, Integer viewCount, LocalDate start, LocalDate end, String postingType) {
        JPQLQuery<Posting> query = queryFactory
                .selectFrom(posting)
                .leftJoin(posting.community, community)
                .leftJoin(posting.user, user)
                .leftJoin(posting.voteList, vote)
                .groupBy(posting.pk)
                .where(
                        keywordSearch(keyword, keywordType),// 검색어 (제목,내용,해쉬테그,닉네임)
                        filterByCommunity(communityReq),//커뮤니티 필터링
                        filterByUser(userReq),//사용자 필터링
                        filterByLikes(likes),//좋아요 필터링
                        filterByViewCount(viewCount),//조회수 필터링
                        isDateAfter(start),//작성일 필터링
                        isDateBefore(end),
                        filterByPostingType(postingType),//게시글 타입 필터링 ( 공지, 인증, 일반)
                        statusPosted())// 포스팅 상태 정상
                .orderBy(sorting(sort));
        List<Posting> postings = this.getQuerydsl().applyPagination(pageRequest, query).fetch();
        return new PageImpl<Posting>(postings, pageRequest, query.fetchCount());
    }

    private BooleanExpression keywordSearch(String keyword, String keywordType) {
        if (keyword == null) return null;
        if (keywordType == null) {
            keywordType = "TC";
        }
        switch (keywordType.toUpperCase()) {
            case "TITLE":
                return keywordInTitle(keyword);
            case "CONTENTS":
                return keywordInContents(keyword);
            case "NICKNAME":
                return keywordInUserNickName(keyword);
            case "TC"://title and contents
                return keywordInTitle(keyword).or(keywordInContents(keyword));
            case "ALL":
                return keywordInTitle(keyword).or(keywordInContents(keyword)).or(keywordInHashtag(keyword)).or(keywordInUserNickName(keyword));
        }
        return null;
    }

    private BooleanExpression keywordInTitle(String keyword) {
        return posting.title.contains(keyword);
    }

    private BooleanExpression keywordInContents(String keyword) {
        return posting.contents.contains(keyword);
    }

    private BooleanExpression keywordInHashtag(String keyword) {
        return posting.hashtag.contains(keyword);
    }

    private BooleanExpression keywordInUserNickName(String keyword) {
        return posting.user.nickName.contains(keyword);
    }

    private BooleanExpression filterByCommunity(Community communityReq) {
        if (communityReq == null) {
            return null;
        }
        return community.eq(communityReq);
    }

    private BooleanExpression filterByUser(User userReq) {
        if (userReq == null) {
            return null;
        }
        return user.eq(userReq);
    }

    private BooleanExpression filterByLikes(Integer likes) {
        if (likes == null) {
            return null;
        }
        return JPAExpressions
                .select(vote.count())
                .from(vote)
                .where(vote.posting.eq(posting).and(vote.like.eq(1)))
                .goe(Long.valueOf(likes));
    }

    private BooleanExpression filterByViewCount(Integer viewCount) {
        if (viewCount == null) {
            return null;
        }
        return posting.viewCount.goe(viewCount);
    }

    private BooleanExpression isDateAfter(LocalDate start) {
        if (start == null) {
            return null;
        }
        return posting.createdDate.after(start.atStartOfDay());
    }

    private BooleanExpression isDateBefore(LocalDate end) {
        if (end == null) {
            return null;
        }
        return posting.createdDate.before(end.atStartOfDay());
    }

    private BooleanExpression filterByPostingType(String postingType) {
        if (postingType == null) {
            return null;
        }
        switch (postingType.toUpperCase()) {
            case "NOTIFICATION":
                return typeNotification();
            case "VERIFIED":
                return typeVerified();
            case "SUGGEST":
                return typeNotification().or(typeVerified());
            case "NORMAL":
                return typeNormal();
            case "ALL":
                return typeNormal().or(typeVerified()).or(typeNotification());
        }
        return null;
    }

    private BooleanExpression typeNotification() {
        return posting.type.eq(PostingType.NOTIFICATION);
    }

    private BooleanExpression typeVerified() {
        return posting.type.eq(PostingType.VERIFIED);
    }

    private BooleanExpression typeNormal() {
        return posting.type.eq(PostingType.NORMAL);
    }

    private BooleanExpression statusPosted() {
        return posting.status.eq(PostingStatus.POSTED);
    }

    private OrderSpecifier<?> sorting(String sort) {
        if (sort == null) {
            sort = "NEWEST";
        }
        switch (sort.toUpperCase()) {
            case "NEWEST":
                return new OrderSpecifier<>(Order.DESC, posting.pk);
            case "LATEST":
                return new OrderSpecifier<>(Order.ASC, posting.pk);
            case "VIEWS":
                return new OrderSpecifier<>(Order.DESC, posting.viewCount);
            case "LIKES":
                return null; //select p.* from posting p left outer join vote v on p.pk =v.posting order by (select count(*) from vote v2  where v2.posting  =p.pk and v.liked =1 ) desc ;
        }
        return null;
    }
}


