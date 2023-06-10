package com.cutegyuseok.freetalk.posting.service.impl;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.service.UserService;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.service.CommunityService;
import com.cutegyuseok.freetalk.posting.dto.PostingDTO;
import com.cutegyuseok.freetalk.posting.entity.Posting;
import com.cutegyuseok.freetalk.posting.enumType.PostingStatus;
import com.cutegyuseok.freetalk.posting.enumType.PostingType;
import com.cutegyuseok.freetalk.posting.repository.PostingRepository;
import com.cutegyuseok.freetalk.posting.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class PostingServiceImpl implements PostingService {

    private final PostingRepository postingRepository;
    private final CommunityService communityService;
    private final UserService userService;


    @Override
    public ResponseEntity<?> uploadPosting(UserDTO.UserAccessDTO userAccessDTO, Long communityPK, PostingDTO.UploadPosting postingDTO) {
        try {
            Community community = communityService.getCommunityEntity(communityPK);
            User user = userService.getUser(userAccessDTO);
            Posting posting = Posting.builder()
                    .user(user)
                    .community(community)
                    .title(postingDTO.getTitle())
                    .thumbnail(postingDTO.getThumbnail())
                    .contents(postingDTO.getContents())
                    .hashtag(postingDTO.getHashtag())
                    .status(PostingStatus.POSTED)
                    .type(PostingType.NORMAL)
                    .build();
            postingRepository.save(posting);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
