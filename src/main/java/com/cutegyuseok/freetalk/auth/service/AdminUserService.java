package com.cutegyuseok.freetalk.auth.service;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AdminUserService {
    ResponseEntity<?> checkUserList(String userName,String userNickName,String userEmail,String userRole, String userStatus, Long communityId,int page);
    ResponseEntity<?> updateUserByAdmin(Long userPK, UserDTO.UpdateUserByAdmin dto);
    ResponseEntity<?> getUserInfoByAdmin(Long userPK);
}
