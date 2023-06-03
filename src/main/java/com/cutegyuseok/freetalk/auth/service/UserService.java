package com.cutegyuseok.freetalk.auth.service;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> signup(UserDTO.UserSignupReqDTO reqDTO);

    ResponseEntity<?> emailConfirmation(UserDTO.UserAccessDTO dto, String code);

    ResponseEntity<?> emailConfirmationCodeRequest(UserDTO.UserAccessDTO dto);

    ResponseEntity<?> login(UserDTO.LoginReqDTO loginReqDTO);
    ResponseEntity<?> updateUserProfile(UserDTO.UserAccessDTO userAccessDTO,UserDTO.ProfileUpdateReqDTO dto);
}
