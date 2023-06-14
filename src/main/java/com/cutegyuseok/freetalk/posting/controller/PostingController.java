package com.cutegyuseok.freetalk.posting.controller;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.posting.dto.PostingDTO;
import com.cutegyuseok.freetalk.posting.service.PostingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"게시글 서비스"}, description = "게시글 관련 서비스")
@CrossOrigin(origins = "*")
@RequestMapping("/posting")
public class PostingController {
    private final PostingService postingService;

    @PostMapping("/{communityPK}")
    @ApiOperation(value = "게시글 게시", notes = "code: 201 게시글 생성됨, 400 잘못된 요청,406 커뮤니티 가입 필요, 500 서버에러")
    public ResponseEntity<?> uploadPosting(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,
                                           @PathVariable Long communityPK,
                                           @RequestBody PostingDTO.UploadPosting postingDTO){
        return postingService.uploadPosting(userAccessDTO,communityPK,postingDTO);
    }
    @GetMapping("/{postPK}")
    @ApiOperation(value = "게시글 조회", notes = "code: 200 조회 성공, 404 잘못된 요청, 500 서버에러")
    public ResponseEntity<?> showPosting(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@PathVariable Long postPK){
        return postingService.showPosting(userAccessDTO,postPK);
    }

    @PostMapping("/comment/{postPK}")
    @ApiOperation(value = "댓글 게시", notes = "code: 201 댓글 생성됨, 400 잘못된 요청,406 커뮤니티 가입 필요, 500 서버에러")
    public ResponseEntity<?> uploadComment(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,
                                           @PathVariable Long postPK,
                                           @RequestBody PostingDTO.UploadCommentDTO commentDTO){
        return postingService.uploadComment(userAccessDTO,postPK,commentDTO);
    }
    @GetMapping("/comment/{postPK}")
    @ApiOperation(value = "댓글 조회", notes = "code: 200 조회 성공, 404 잘못된 요청, 500 서버에러")
    public ResponseEntity<?> showComments(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@PathVariable Long postPK){
        return postingService.showComments(userAccessDTO,postPK);
    }

    @PostMapping("/vote/{PK}")
    @ApiOperation(value = "투표 기능", notes = "type : POSTING || COMMENT \n\n code: 201 요청 성공, 200 수정됨, 409 이미 반영된 의견, 400 옳바르지 않은 TYPE, 404 잘못된 pk 또는 사용자, 500 서버에러")
    public ResponseEntity<?> voteRequest(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,
                                           @PathVariable Long PK,
                                           @RequestBody PostingDTO.VoteReqDTO voteReqDTO){
        return postingService.voteRequest(userAccessDTO,PK,voteReqDTO);
    }

}
