package com.cutegyuseok.freetalk.community.controller;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.community.dto.CommunityDTO;
import com.cutegyuseok.freetalk.community.service.CommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"커뮤니티 서비스"}, description = "커뮤니티 관련 서비스")
@CrossOrigin(origins = "*")
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/")
    @ApiOperation(value = "커뮤니티 생성", notes = "code: 201 커뮤니티 생성됨, 406 이미 존재하는 이름,404 카테고리중 존재하지 않는 PK가 입력됨,401사용자 인증 오류 ,500 서버에러")
    public ResponseEntity<?> makeCommunity(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody CommunityDTO.MakeCommunityDTO dto){
        return communityService.createCommunity(dto,userAccessDTO);
    }
    @PatchMapping("/{communityPk}")
    @ApiOperation(value = "커뮤니티 수정", notes = "code: 200 커뮤니티 수정됨, 406 이미 존재하는 이름,404 카테고리중 존재하지 않는 PK가 입력됨,401사용자 인증 오류 ,500 서버에러")
    public ResponseEntity<?> updateCommunity(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@PathVariable Long communityPk, @RequestBody CommunityDTO.UpdateCommunityDTO dto){
        return communityService.updateCommunity(communityPk,dto,userAccessDTO);
    }

    @GetMapping("/{communityPk}")
    @ApiOperation(value = "커뮤니티 단일 조회", notes = "code: 200 조회 성공,404 존재하지 않는 PK가 입력됨,500 서버에러")
    public ResponseEntity<?> showCommunity(@PathVariable Long communityPk){
        return communityService.showCommunity(communityPk);
    }

    @PostMapping("/join/{communityPk}")
    @ApiOperation(value = "커뮤니티 참여", notes = "code: 201 참여 성공,400 존재하지 않는 PK가 입력됨,500 서버에러")
    public ResponseEntity<?> joinCommunity(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@PathVariable Long communityPk){
        return communityService.joinCommunity(userAccessDTO,communityPk);
    }
    @DeleteMapping("/join/{communityPk}")
    @ApiOperation(value = "커뮤니티 탈퇴", notes = "code: 200 탈퇴 성공,400 존재하지 않는 PK가 입력됨,500 서버에러")
    public ResponseEntity<?> disJoinCommunity(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@PathVariable Long communityPk){
        return communityService.disJoinCommunity(userAccessDTO,communityPk);
    }

    @GetMapping("/join")
    @ApiOperation(value = "참여한 커뮤니티 조회", notes = "code: 200 조회 성공,500 서버에러")
    public ResponseEntity<?> showJoinedCommunity(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO){
        return communityService.showJoinedCommunity(userAccessDTO);
    }

}
