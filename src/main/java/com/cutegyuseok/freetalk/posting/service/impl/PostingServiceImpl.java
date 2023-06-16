package com.cutegyuseok.freetalk.posting.service.impl;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.enumType.UserRole;
import com.cutegyuseok.freetalk.auth.repository.UserRepository;
import com.cutegyuseok.freetalk.auth.service.UserService;
import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.community.dto.CommunityDTO;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.repository.CommunityRepository;
import com.cutegyuseok.freetalk.community.repository.JoinRepository;
import com.cutegyuseok.freetalk.community.service.CommunityService;
import com.cutegyuseok.freetalk.global.response.PageResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.cutegyuseok.freetalk.global.config.PageSizeConfig.Community_List_Size;

@RequiredArgsConstructor
@Service
public class PostingServiceImpl implements PostingService {

    private final PostingRepository postingRepository;
    private final CommunityService communityService;
    private final UserService userService;
    private final JoinRepository joinRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;


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
    public ResponseEntity<?> showComments(UserDTO.UserAccessDTO userAccessDTO,Long postPK) {
        try {
            User viewer;
            if (userAccessDTO==null){
                viewer = null;
            }else {
                viewer = userRepository.findByEmail(userAccessDTO.getEmail()).orElse(null);
            }
            Posting posting = postingRepository.findById(postPK).orElseThrow(NoSuchElementException::new);
            List<PostingDTO.ViewComments> list = commentRepository.findAllByPostingAndParentIsNull(posting).stream().map(e-> PostingDTO.ViewComments.of(e,viewer)).collect(Collectors.toList());
            if (list.size() < 1) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> showPosting(UserDTO.UserAccessDTO userAccessDTO,Long postPK) {
        try {
            User viewer;
            if (userAccessDTO==null){
                viewer = null;
            }else {
                viewer = userRepository.findByEmail(userAccessDTO.getEmail()).orElse(null);
            }
            Posting posting = postingRepository.findById(postPK).orElseThrow(NoSuchElementException::new);
            if (!posting.getStatus().equals(PostingStatus.POSTED)){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            PostingDTO.ViewPosting result = new PostingDTO.ViewPosting(posting,posting.whetherToVote(viewer));
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
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

    @Override
    public ResponseEntity<?> searchPosting(String keyword, String keywordType, String sort, int page, Long communityPK, Long userPK, Integer likes, Integer viewCount, String startDate, String endDate, String postingType) {
        try {
            //date
            LocalDate start;
            LocalDate end;
            if (startDate!=null) {
                start = LocalDate.parse(startDate).minusDays(1);
            }else {
                start = null;
            }
            if (endDate!=null){
                end = LocalDate.parse(endDate).plusDays(1);
            }else {
                end = null;
            }

            // pageable
            if (page < 1) {
                throw new IllegalArgumentException();
            }
            PageRequest pageable = PageRequest.of(page - 1, Community_List_Size);

            //community
            Community  community = null;
            if (communityPK!=null){
                community = communityRepository.findById(communityPK).orElse(null);
            }

            //user
            User  user = null;
            if (userPK!=null){
                user = userRepository.findById(userPK).orElse(null);
            }
            Page<Posting> postings = postingRepository.search(keyword,keywordType,sort,pageable,community,user,likes,viewCount,start,end,postingType);
            if (postings.getTotalElements() < 1) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            PageResponseDTO pageResponseDTO = new PageResponseDTO(postings);
            pageResponseDTO.setContent(postings.getContent().stream().map(PostingDTO.PostingListDTO::new).collect(Collectors.toList()));
            return new ResponseEntity<>(pageResponseDTO,HttpStatus.OK);
        }catch (DateTimeParseException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deletePosting(UserDTO.UserAccessDTO userAccessDTO, Long postPK) {
        try {
            User user = userService.getUser(userAccessDTO);
            Posting posting = postingRepository.findById(postPK).orElseThrow(NoSuchElementException::new);
            if (!posting.getUser().equals(user)){
                if (!user.getRole().equals(UserRole.ROLE_SUPER)) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            posting.deletePosting();
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
