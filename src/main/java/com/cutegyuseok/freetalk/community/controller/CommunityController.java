package com.cutegyuseok.freetalk.community.controller;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.community.dto.CommunityDTO;
import com.cutegyuseok.freetalk.community.service.CommunityService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"커뮤니티 서비스"}, description = "커뮤니티 생성, 수정, 삭제")
@CrossOrigin(origins = "*")
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/")
    public ResponseEntity<?> makeCommunity(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @RequestBody CommunityDTO.MakeCommunityDTO dto){
        return communityService.createCommunity(dto,userAccessDTO);
    }

    @GetMapping("/{pk}")
    public ResponseEntity<?> showCommunity(@PathVariable Long pk){
        return communityService.showCommunity(pk);
    }
}
