package com.cutegyuseok.freetalk.posting.service;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.posting.dto.PostingDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface PostingService {

    ResponseEntity<?> uploadPosting(UserDTO.UserAccessDTO userAccessDTO, Long communityPK, PostingDTO.UploadPosting postingDTO);
    ResponseEntity<?> updatePosting(UserDTO.UserAccessDTO userAccessDTO,Long postingPK, PostingDTO.UploadPosting postingDTO);

    ResponseEntity<?> uploadComment(UserDTO.UserAccessDTO userAccessDTO, Long postPK, PostingDTO.UploadCommentDTO commentDTO);

    ResponseEntity<?> showComments(UserDTO.UserAccessDTO userAccessDTO, Long postPK);

    ResponseEntity<?> showPosting(UserDTO.UserAccessDTO userAccessDTO, Long postPK);

    ResponseEntity<?> voteRequest(UserDTO.UserAccessDTO userAccessDTO, Long PK, PostingDTO.VoteReqDTO voteReqDTO);

    ResponseEntity<?> searchPosting(String keyword, String keywordType, String sort, int page, Long communityPK, UUID userPK, Integer likes, Integer viewCount, String startDate, String endDate, String postingType);

    ResponseEntity<?> deletePosting(UserDTO.UserAccessDTO userAccessDTO, Long postPK);
    ResponseEntity<?> deleteComment(UserDTO.UserAccessDTO userAccessDTO, Long commentPK);

}
