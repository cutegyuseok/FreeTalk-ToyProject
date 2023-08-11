package com.cutegyuseok.freetalk.auth.service;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AdminUserService {
    ResponseEntity<?> checkUserList(String userName,String userNickName,String userEmail,String userRole, String userStatus, Long communityId,int page);
    ResponseEntity<?> updateUserByAdmin(UUID userPK, UserDTO.UpdateUserByAdmin dto);
    ResponseEntity<?> getUserInfoByAdmin(UUID userPK);
}
