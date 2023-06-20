package com.cutegyuseok.freetalk.auth.repository;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.enumType.UserRole;
import com.cutegyuseok.freetalk.auth.enumType.UserStatus;
import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.BooleanBuilder;
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

import java.util.List;

import static com.cutegyuseok.freetalk.auth.entity.QUser.user;
import static com.cutegyuseok.freetalk.community.entity.QCommunity.community;
import static com.cutegyuseok.freetalk.community.entity.QJoin.join;

public class UserRepositoryCustomImpl extends QuerydslRepositorySupport implements UserRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl() {
        super(User.class);
    }

    @Override
    public Page<User> checkUserList(String userName, String userNickName, String userEmail, String userRole, String userStatus, Community communityReq,Pageable pageable) {
        JPQLQuery<User> query = queryFactory.selectFrom(user)
                .leftJoin(user.joinedCommunity,join)
                .groupBy(user)
                .where(
                        filterByUserNameContains(userName),
                        filterByUserNickNameContains(userNickName),
                        filterByUserEmailContains(userEmail),
                        filterByUserRole(userRole),
                        filterByUserStatus(userStatus),
                        filterByJoinedCommunity(communityReq)
                )
                .orderBy(new OrderSpecifier<>(Order.ASC,user.pk));
        List<User> userList = this.getQuerydsl().applyPagination(pageable,query).fetch();
        return new PageImpl<User>(userList,pageable,query.fetchCount());
    }

    private BooleanExpression filterByUserNameContains(String userName){
        if (userName == null)return null;
        return user.name.contains(userName);
    }
    private BooleanExpression filterByUserNickNameContains(String userNickName){
        if (userNickName == null)return null;
        return user.nickName.contains(userNickName);
    }
    private BooleanExpression filterByUserEmailContains(String userEmail){
        if (userEmail == null)return null;
        return user.email.contains(userEmail);
    }
    private BooleanExpression filterByUserRole(String userRole){
        if (userRole == null)return null;
        UserRole role = UserRole.from(userRole);
        return user.role.eq(role);
    }
    private BooleanExpression filterByUserStatus(String userStatus){
        if (userStatus == null)return null;
        UserStatus status = UserStatus.from(userStatus);
        return user.status.eq(status);
    }
    private BooleanExpression filterByJoinedCommunity(Community community){
        if (community==null)return null;
        return join.community.eq(community);
    }
}
