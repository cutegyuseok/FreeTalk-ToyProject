package com.cutegyuseok.freetalk.auth.controller;

import com.cutegyuseok.freetalk.auth.dto.TokenDTO;
import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.enumType.UserRole;
import com.cutegyuseok.freetalk.auth.service.TokenService;
import com.cutegyuseok.freetalk.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@Api(tags = {"인증 서비스"}, description = "회원가입,로그인, 회원탈퇴, 토큰 리프레시")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final TokenService tokenService;


    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입.\n\n code: 201 회원가입 성공, 409 email 이 존재, 406 email 패턴 불가, 500 서버에러")
    public ResponseEntity<?> signUp(@RequestBody UserDTO.UserSignupReqDTO dto) {
        return userService.signup(dto);
    }

    @GetMapping("/email/confirmation")
    @ApiOperation(value = "이메일 인증", notes = "이메일 인증 코드 보내기.\n\n code: 200 성공, 500 서버 에러")
    public ResponseEntity<?> emailConfirmationCodeRequest(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO) {
        return userService.emailConfirmationCodeRequest(userAccessDTO);
    }

    @PostMapping("/email/confirmation")
    @ApiOperation(value = "이메일 인증 코드 입력", notes = "이메일 인증 코드 입력.\n\n code: 200 성공, 500 서버 에러")
    public ResponseEntity<?> emailConfirmation(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody UserDTO.EmailConfirmationCodeReqDTO dto) {
        return userService.emailConfirmation(userAccessDTO, dto.getCode());
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "이메일과 패스워드를 입력받아 로그인이 가능 성공하면 토큰발급.\n\n code: 200 로그인 성공, 401 탈퇴한 회원, 404 없는회원 또는 비밀번호 불일치")
    public ResponseEntity<?> login(@RequestBody UserDTO.LoginReqDTO loginReqDTO) {
        return userService.login(loginReqDTO);
    }


    @GetMapping("/checkRole")
    @ApiOperation(value = "권한 확인", notes = "로그인 한 사용자의 권한 확인.\n\n code: 200 성공, 403 토근에 권한 없음")
    public ResponseEntity<?> checkRole(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO) {
        return new ResponseEntity<>(userAccessDTO.getRole(), HttpStatus.OK);
    }

    @PostMapping("/checkRole/grade")
    @ApiOperation(value = "접근 권한 확인", notes = "로그인 한 사용자의 접근 가능 여부 확인.\n\n code: 200 접근 가능, 403 접근 불가능")
    public ResponseEntity<?> checkRoleWithGrade(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody UserDTO.RoleCheckDTO dto) {
        UserRole reqRole = dto.getRole();
        UserRole userRole = UserRole.from(userAccessDTO.getRole());
        if (reqRole.getGrade() >= userRole.getGrade()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃 (토큰 O)", notes = "버튼을 누르면 현재 로그인 토큰을 로그아웃 테이블에 저장한다.\n\n " +
            "다음 요청시에 현재 토큰과 요청이 오면 토큰 유효성 검사에 걸려서 로그인을 다시 요청하게 된다.\n\n" +
            "code: 200 로그아웃 성공, 401 이미 로그아웃한 토큰, 500 서버에러-재로그인 유도")
    public ResponseEntity<?> logout(@ApiIgnore @RequestHeader(name = "Authorization") String header, @RequestBody TokenDTO.RefreshTokenReqDTO refreshTokenReqDTO) {
        return tokenService.logout(header, refreshTokenReqDTO);
    }

    @PostMapping("/token/refresh")
    @ApiOperation(value = "토큰 리프레시", notes = "리프레시 토큰을 보내주면 유효성을 확인하고 엑세스토큰을 새로 발급.\n\n" +
            "code: 200 접근토큰 재생성 성공, 401 인증 만료로 인한 재로그인 필요")
    public ResponseEntity<?> validateRefreshToken(@RequestBody TokenDTO.RefreshTokenReqDTO refreshTokenReqDTO) {
        return tokenService.validateRefreshToken(refreshTokenReqDTO.getRefreshToken());
    }


}
