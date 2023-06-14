package com.cutegyuseok.freetalk.posting.service.impl;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.service.UserService;
import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.repository.JoinRepository;
import com.cutegyuseok.freetalk.community.service.CommunityService;
import com.cutegyuseok.freetalk.posting.dto.PostingDTO;
import com.cutegyuseok.freetalk.posting.entity.Comment;
import com.cutegyuseok.freetalk.posting.entity.Posting;
import com.cutegyuseok.freetalk.posting.entity.Vote;
import com.cutegyuseok.freetalk.posting.enumType.PostingStatus;
import com.cutegyuseok.freetalk.posting.enumType.PostingType;
import com.cutegyuseok.freetalk.posting.repository.CommentRepository;
import com.cutegyuseok.freetalk.posting.repository.PostingRepository;
import com.cutegyuseok.freetalk.posting.repository.VoteRepository;
import com.cutegyuseok.freetalk.posting.service.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostingServiceImpl implements PostingService {

    private final PostingRepository postingRepository;
    private final CommunityService communityService;
    private final UserService userService;
    private final JoinRepository joinRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;


    @Override
    public ResponseEntity<?> uploadPosting(UserDTO.UserAccessDTO userAccessDTO, Long communityPK, PostingDTO.UploadPosting postingDTO) {
        try {
            Community community = communityService.getCommunityEntity(communityPK);
            User user = userService.getUser(userAccessDTO);
            if (!joinRepository.existsByCommunityAndUser(community,user)){
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            Posting posting = Posting.builder()
                    .user(user)
                    .community(community)
                    .title(postingDTO.getTitle())
                    .thumbnail(postingDTO.getThumbnail())
                    .contents(postingDTO.getContents())
                    .hashtag(postingDTO.getHashtag())
                    .viewCount(0L)
                    .status(PostingStatus.POSTED)
                    .type(PostingType.NORMAL)
                    .build();
            postingRepository.save(posting);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> uploadComment(UserDTO.UserAccessDTO userAccessDTO, Long postPK, PostingDTO.UploadCommentDTO commentDTO) {
        try {
            Posting posting = postingRepository.findById(postPK).orElseThrow(NoSuchElementException::new);
            User user = userService.getUser(userAccessDTO);
            Comment targetComment = null;
            if (commentDTO.getTargetComment()!=null) {
                targetComment = commentRepository.findById(commentDTO.getTargetComment()).orElseThrow(NoSuchElementException::new);
                if (posting != targetComment.getPosting()) {
                    throw new NotAcceptableStatusException("This comment does not belong to this post.");
                }
            }
            if (!joinRepository.existsByCommunityAndUser(posting.getCommunity(),user)){
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            Comment comment = Comment.builder()
                    .posting(posting)
                    .user(user)
                    .contents(commentDTO.getContents())
                    .status(PostingStatus.POSTED)
                    .parent(targetComment)
                    .build();
            commentRepository.save(comment);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (NoSuchElementException | NotAcceptableStatusException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> showComments(Long postPK) {
        try {
            Posting posting = postingRepository.findById(postPK).orElseThrow(NoSuchElementException::new);
            List<PostingDTO.ViewComments> list = commentRepository.findAllByPostingAndParentIsNull(posting).stream().map(PostingDTO.ViewComments::of).collect(Collectors.toList());
            if (list.size() < 1) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> voteRequest(UserDTO.UserAccessDTO userAccessDTO, Long PK, PostingDTO.VoteReqDTO voteReqDTO) {
        try {
            Posting posting=null;
            Comment comment=null;
            if (voteReqDTO.getType().equalsIgnoreCase("POSTING")) {
                posting = postingRepository.findById(PK).orElseThrow(NoSuchElementException::new);
            } else if (voteReqDTO.getType().equalsIgnoreCase("COMMENT")) {
                comment = commentRepository.findById(PK).orElseThrow(NoSuchElementException::new);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            User user = userService.getUser(userAccessDTO);
            int like;
            if (voteReqDTO.getLike()){
                like=1;
            }else like=-1;

            Vote vote = voteRepository.findByUserAndCommentAndPosting(user,comment,posting).orElse(null);
            if (vote!=null){
                if (like==vote.getLike()){
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }else {
                    vote.updateVote(like);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }else {
                vote = Vote.builder()
                        .user(user)
                        .comment(comment)
                        .posting(posting)
                        .like(like)
                        .build();
                voteRepository.save(vote);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
