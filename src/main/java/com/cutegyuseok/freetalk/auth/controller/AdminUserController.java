package com.cutegyuseok.freetalk.auth.controller;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.service.AdminUserService;
import com.cutegyuseok.freetalk.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"관리자의 사용자 관리 서비스"}, description = "")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/admin/user")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/")
    @ApiOperation(value = "사용자 조회", notes = "code: 200 OK, 404 없는 사용자, 500 서버에러")
    public ResponseEntity<?> checkUserList(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) String nickName,
                                           @RequestParam(required = false) String email,
                                           @RequestParam(required = false) String role,
                                           @RequestParam(required = false) String status,
                                           @RequestParam(required = false) Long communityId,
                                           @RequestParam(required = false, defaultValue = "1") int page) {
        return adminUserService.checkUserList(name,nickName,email,role,status, communityId, page);
    }

    @PatchMapping("/{userPK}")
    @PreAuthorize("hasAnyRole('ROLE_WRITE','ROLE_SUPER')")
    @ApiOperation(value = "관리자의 사용자 정보 수정 ")
    public ResponseEntity<?> updateUserByAdmin(@PathVariable Long userPK,@RequestBody UserDTO.UpdateUserByAdmin dto) {
        return adminUserService.updateUserByAdmin(userPK,dto);
    }
    @GetMapping("/{userPK}")
    @PreAuthorize("hasAnyRole('ROLE_WRITE','ROLE_SUPER')")
    @ApiOperation(value = "관리자의 사용자 정보 수정 ", notes = "code: 200 OK, 404 없는 사용자, 500 서버에러")
    public ResponseEntity<?> getUserInfoByAdmin(@PathVariable Long userPK) {
        return adminUserService.getUserInfoByAdmin(userPK);
    }
}
