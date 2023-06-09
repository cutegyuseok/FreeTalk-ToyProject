package com.cutegyuseok.freetalk.community.service.impl;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.repository.UserRepository;
import com.cutegyuseok.freetalk.auth.service.UserService;
import com.cutegyuseok.freetalk.category.dto.CategoryDTO;
import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.category.repository.CategoryRepository;
import com.cutegyuseok.freetalk.community.dto.CommunityDTO;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.entity.CommunityCategory;
import com.cutegyuseok.freetalk.community.entity.Join;
import com.cutegyuseok.freetalk.community.enumType.CommunityStatus;
import com.cutegyuseok.freetalk.community.repository.CommunityCategoryRepository;
import com.cutegyuseok.freetalk.community.repository.CommunityRepository;
import com.cutegyuseok.freetalk.community.repository.JoinRepository;
import com.cutegyuseok.freetalk.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;
    private final CategoryRepository categoryRepository;
    private final JoinRepository joinRepository;
    private final UserService userService;
    private final CommunityCategoryRepository communityCategoryRepository;


    @Override
    public ResponseEntity<?> createCommunity(CommunityDTO.MakeCommunityDTO dto, UserDTO.UserAccessDTO userAccessDTO) {
        if (communityRepository.existsByName(dto.getCommunityName())){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            User user = userService.getUser(userAccessDTO);
            List<Category> categoryList = categoryRepository.findAllByPkIn(dto.getCategoryIdList());
            if (categoryList.size()!=dto.getCategoryIdList().size()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Community community = Community.builder()
                    .name(dto.getCommunityName())
                    .introduce(dto.getCommunityIntroduce())
                    .mainImage(dto.getCommunityMainImage())
                    .backgroundImage(dto.getCommunityBackgroundImage())
                    .user(user)
                    .status(CommunityStatus.AVAILABLE)
                    .build();
            for (Category category : categoryList){
                community.getCommunityCategoryList().add(CommunityCategory.builder()
                        .community(community)
                        .category(category)
                        .build());
            }
            communityRepository.save(community);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> showCommunity(Long pk) {
        try {
            Community community = communityRepository.findById(pk).orElseThrow(NoSuchElementException::new);
            UserDTO.ShowOwnerDTO owner = new UserDTO.ShowOwnerDTO(community.getUser());
            return new ResponseEntity<>(new CommunityDTO.ShowCommunityDTO(community,owner),HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> joinCommunity(UserDTO.UserAccessDTO userAccessDTO, Long pk) {
        try {
            Community community = communityRepository.findById(pk).orElseThrow(NoSuchElementException::new);
            User user = userService.getUser(userAccessDTO);
            if(joinRepository.existsByCommunityAndUser(community,user)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Join join = Join.builder()
                    .community(community)
                    .user(user)
                    .build();
            joinRepository.save(join);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> disJoinCommunity(UserDTO.UserAccessDTO userAccessDTO, Long pk) {
        try {
            Community community = communityRepository.findById(pk).orElseThrow(NoSuchElementException::new);
            User user = userService.getUser(userAccessDTO);
            joinRepository.deleteByCommunityAndUser(community,user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> showJoinedCommunity(UserDTO.UserAccessDTO userAccessDTO) {
        try {
            User user = userService.getUser(userAccessDTO);
            List<Join> joinList = joinRepository.findAllByUser(user);
            if (joinList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<CommunityDTO.ShowCommunityListDTO> result = joinList.stream()
                    .map(e ->new CommunityDTO.ShowCommunityListDTO(e.getCommunity()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateCommunity(Long communityPk, CommunityDTO.UpdateCommunityDTO dto, UserDTO.UserAccessDTO userAccessDTO) {
        try {
            User user = userService.getUser(userAccessDTO);
            Community community = communityRepository.findById(communityPk).orElseThrow(NoSuchElementException::new);
            if (community.getUser()!=user) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            communityCategoryRepository.deleteAllByCommunity(community);
            List<Category> categoryList = categoryRepository.findAllByPkIn(dto.getCategoryIdList());
            if (categoryList.size()!=dto.getCategoryIdList().size()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            for (Category category : categoryList){
                community.getCommunityCategoryList()
                        .add(CommunityCategory.builder()
                        .community(community)
                        .category(category)
                        .build());
            }
            community.updateCommunity(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
