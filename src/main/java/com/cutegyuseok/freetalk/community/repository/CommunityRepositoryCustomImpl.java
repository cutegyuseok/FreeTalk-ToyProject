package com.cutegyuseok.freetalk.community.repository;

import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.enumType.CommunityStatus;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;
import static com.cutegyuseok.freetalk.community.entity.QCommunity.community;
import static com.cutegyuseok.freetalk.community.entity.QCommunityCategory.communityCategory;
import static com.cutegyuseok.freetalk.community.entity.QJoin.join;
public class CommunityRepositoryCustomImpl extends QuerydslRepositorySupport implements CommunityRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    public CommunityRepositoryCustomImpl() {
        super(Community.class);
    }
    @Override
    public Page<Community> search(Pageable pageable, String keyword, String sort, Category category) {
        JPQLQuery<Community> query = queryFactory
                .selectFrom(community)
                .leftJoin(community.communityCategoryList,communityCategory)
                .leftJoin(community.joinedUsers,join)
                .groupBy(community.pk)
                .where(containsKeyword(keyword),containCategory(category),isAvailableCommunity())
                .orderBy(sort(sort));
        List<Community> communityList = this.getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<Community>(communityList, pageable, query.fetchCount());
    }
    private BooleanExpression containsKeyword(String keyword) {
        if (keyword==null) {
            return null;
        }
        return community.name.contains(keyword)
                .or(community.introduce.contains(keyword));
    }
    private BooleanExpression containCategory(Category category) {
        if (category==null){
            return null;
        }
        return communityCategory.category.in(listOfCategory(category));
    }
    private BooleanExpression isAvailableCommunity() {
        return community.status.eq(CommunityStatus.AVAILABLE);
    }

    private OrderSpecifier<?> sort(String sort) {
        switch (sort.toUpperCase()) {
            case "NEWEST":
                return new OrderSpecifier<>(Order.DESC, community.pk);
            case "LATEST":
                return new OrderSpecifier<>(Order.ASC, community.pk);
            case "PEOPLE":
                return new OrderSpecifier<>(Order.DESC, community.joinedUsers.size());
            case "POSTING":
                return new OrderSpecifier<>(Order.DESC, community.postingList.size());
        }
        return null;
    }
    private List<Category> listOfCategory(Category category) {
        List<Category> categoryList = new ArrayList<>(category.getChildren());
        categoryList.add(category);
        for (int i = 0; i < category.getChildren().size(); i++) {
            Category insideCategory = category.getChildren().get(i);
            if (insideCategory.getChildren().size() > 0)
                categoryList.addAll(listOfCategory(insideCategory));
        }
        return categoryList;
    }
}
