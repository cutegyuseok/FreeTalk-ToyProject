package com.cutegyuseok.freetalk.community.service.impl;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.repository.UserRepository;
import com.cutegyuseok.freetalk.category.entity.Category;
import com.cutegyuseok.freetalk.category.repository.CategoryRepository;
import com.cutegyuseok.freetalk.community.dto.CommunityDTO;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.entity.CommunityCategory;
import com.cutegyuseok.freetalk.community.enumType.CommunityStatus;
import com.cutegyuseok.freetalk.community.repository.CommunityRepository;
import com.cutegyuseok.freetalk.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    @Override
    public ResponseEntity<?> createCommunity(CommunityDTO.MakeCommunityDTO dto, UserDTO.UserAccessDTO userAccessDTO) {
        if (communityRepository.existsByName(dto.getCommunityName())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
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

}
