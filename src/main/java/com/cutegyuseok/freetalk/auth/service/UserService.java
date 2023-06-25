package com.cutegyuseok.freetalk.auth.service;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    ResponseEntity<?> signup(UserDTO.UserSignupReqDTO reqDTO);

    ResponseEntity<?> emailConfirmation(UserDTO.UserAccessDTO dto, String code);

    ResponseEntity<?> emailConfirmationCodeRequest(UserDTO.UserAccessDTO dto);

    ResponseEntity<?> login(UserDTO.LoginReqDTO loginReqDTO);

    ResponseEntity<?> updateUserProfile(UserDTO.UserAccessDTO userAccessDTO, UserDTO.ProfileUpdateReqDTO dto);

    ResponseEntity<?> checkUserInfo(UserDTO.UserAccessDTO userAccessDTO);

    User getUser(UserDTO.UserAccessDTO userAccessDTO);

}
