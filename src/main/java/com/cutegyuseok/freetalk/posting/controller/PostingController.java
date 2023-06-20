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
                                           @RequestBody PostingDTO.UploadPosting postingDTO) {
        return postingService.uploadPosting(userAccessDTO, communityPK, postingDTO);
    }

    @GetMapping("/{postPK}")
    @ApiOperation(value = "게시글 단일 조회", notes = "code: 200 조회 성공,204 삭제됨, 404 잘못된 요청, 500 서버에러")
    public ResponseEntity<?> showPosting(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long postPK) {
        return postingService.showPosting(userAccessDTO, postPK);
    }

    @PostMapping("/comment/{postPK}")
    @ApiOperation(value = "댓글 게시", notes = "code: 201 댓글 생성됨, 400 잘못된 요청,406 커뮤니티 가입 필요, 500 서버에러")
    public ResponseEntity<?> uploadComment(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,
                                           @PathVariable Long postPK,
                                           @RequestBody PostingDTO.UploadCommentDTO commentDTO) {
        return postingService.uploadComment(userAccessDTO, postPK, commentDTO);
    }

    @GetMapping("/comment/{postPK}")
    @ApiOperation(value = "댓글 조회", notes = "code: 200 조회 성공, 404 잘못된 요청, 500 서버에러")
    public ResponseEntity<?> showComments(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO, @PathVariable Long postPK) {
        return postingService.showComments(userAccessDTO, postPK);
    }

    @PostMapping("/vote/{PK}")
    @ApiOperation(value = "투표 기능", notes = "type : POSTING || COMMENT \n\n code: 201 요청 성공, 200 수정됨, 409 이미 반영된 의견, 400 옳바르지 않은 TYPE, 404 잘못된 pk 또는 사용자, 500 서버에러")
    public ResponseEntity<?> voteRequest(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,
                                         @PathVariable Long PK,
                                         @RequestBody PostingDTO.VoteReqDTO voteReqDTO) {
        return postingService.voteRequest(userAccessDTO, PK, voteReqDTO);
    }

    @GetMapping("/")
    @ApiOperation(value = "게시글 조회", notes = "" +
            "keyword : 검색어 \n\n" +
            "keywordType : 검색 종류 (title,contents,nickname,tc (title & contents), all)\n\n" +
            "sort : 정렬 (newest,latest,views,likes)\n\n" +
            "page : 페이지 넘버\n\n" +
            "communityPK : 커뮤니티 필터\n\n" +
            "userPK : 사용자 필터\n\n" +
            "likes : 좋아요 필터\n\n" +
            "viewCount : 조회수 필터\n\n" +
            "startDate : 값~게시글 필터\n\n" +
            "endDate : 게시글~값 필터\n\n" +
            "postingType : 게시글 종류 필터(notification,verified,suggest,normal,all)\n\n" +
            "code: 200 조회 성공,500 서버에러")
    public ResponseEntity<?> searchPosting(@RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) String keywordType,
                                           @RequestParam(required = false) String sort,
                                           @RequestParam(required = false, defaultValue = "1") int page,
                                           @RequestParam(required = false) Long communityPK,
                                           @RequestParam(required = false) Long userPK,
                                           @RequestParam(required = false) Integer likes,
                                           @RequestParam(required = false) Integer viewCount,
                                           @RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate,
                                           @RequestParam(required = false) String postingType) {
        return postingService.searchPosting(keyword, keywordType, sort, page, communityPK, userPK, likes, viewCount, startDate, endDate, postingType);
    }

    @DeleteMapping("/{postPK}")
    @ApiOperation(value = "게시글 삭제", notes = "code: 201 게시글 제거됨, 400 잘못된 요청,401 본인 아님, 500 서버에러")
    public ResponseEntity<?> deletePosting(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,
                                           @PathVariable Long postPK) {
        return postingService.deletePosting(userAccessDTO, postPK);
    }

}
