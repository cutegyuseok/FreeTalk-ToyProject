package com.cutegyuseok.freetalk.posting.service;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.posting.dto.PostingDTO;
import org.springframework.http.ResponseEntity;

public interface PostingService {

    ResponseEntity<?> uploadPosting(UserDTO.UserAccessDTO userAccessDTO,Long communityPK,PostingDTO.UploadPosting postingDTO);
    ResponseEntity<?> uploadComment(UserDTO.UserAccessDTO userAccessDTO,Long postPK,PostingDTO.UploadCommentDTO commentDTO);
    ResponseEntity<?> showComments(Long postPK);
}
