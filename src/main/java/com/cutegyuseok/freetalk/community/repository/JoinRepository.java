package com.cutegyuseok.freetalk.community.repository;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.entity.Join;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinRepository extends JpaRepository<Join, Long> {
    Boolean existsByCommunityAndUser(Community community, User user);
    void deleteByCommunityAndUser(Community community,User user);
    List<Join> findAllByUser(User user);
}
