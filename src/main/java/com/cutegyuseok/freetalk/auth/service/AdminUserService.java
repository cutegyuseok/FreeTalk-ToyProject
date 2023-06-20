package com.cutegyuseok.freetalk.auth.service;

import org.springframework.http.ResponseEntity;

public interface AdminUserService {
    ResponseEntity<?> checkUserList(String userName,String userNickName,String userEmail,String userRole, String userStatus, Long communityId,int page);
}
