package com.cutegyuseok.freetalk.auth.repository;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<User> checkUserList(String userName, String userNickName, String userEmail, String userRole, String userStatus, Community community, Pageable pageable);
}
