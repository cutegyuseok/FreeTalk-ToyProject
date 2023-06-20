package com.cutegyuseok.freetalk.auth.controller;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"사용자 서비스"}, description = "사용자 정보 관리 서비스")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("/info")
    @ApiOperation(value = "사용자 프로필 수정", notes = "사용자 정보 입력값만 반영\n\n code: 200 OK, 404 없는 사용자, 500 서버에러")
    public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody UserDTO.ProfileUpdateReqDTO profileUpdateReqDTO) {
        return userService.updateUserProfile(userAccessDTO, profileUpdateReqDTO);
    }

    @GetMapping("/info")
    @ApiOperation(value = "사용자 정보 조회", notes = "code: 200 OK, 404 없는 사용자, 500 서버에러")
    public ResponseEntity<?> checkUserInfo(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO) {
        return userService.checkUserInfo(userAccessDTO);
    }
}
