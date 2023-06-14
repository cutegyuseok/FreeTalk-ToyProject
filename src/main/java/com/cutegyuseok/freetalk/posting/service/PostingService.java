package com.cutegyuseok.freetalk.posting.service;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.posting.dto.PostingDTO;
import org.springframework.http.ResponseEntity;

public interface PostingService {

    ResponseEntity<?> uploadPosting(UserDTO.UserAccessDTO userAccessDTO,Long communityPK,PostingDTO.UploadPosting postingDTO);
    ResponseEntity<?> uploadComment(UserDTO.UserAccessDTO userAccessDTO,Long postPK,PostingDTO.UploadCommentDTO commentDTO);
    ResponseEntity<?> showComments(UserDTO.UserAccessDTO userAccessDTO,Long postPK);
    ResponseEntity<?> showPosting(UserDTO.UserAccessDTO userAccessDTO,Long postPK);
    ResponseEntity<?> voteRequest(UserDTO.UserAccessDTO userAccessDTO,Long PK,PostingDTO.VoteReqDTO voteReqDTO);
}
