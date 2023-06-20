package com.cutegyuseok.freetalk.community.controller;

import com.cutegyuseok.freetalk.community.service.CommunityService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"관리자 커뮤니티 서비스"}, description = "커뮤니티 생성, 수정, 삭제")
@CrossOrigin(origins = "*")
@RequestMapping("/admin/community")
public class AdminCommunityController {

    private final CommunityService communityService;

}
