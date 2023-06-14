package com.cutegyuseok.freetalk.community.service;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.community.dto.CommunityDTO;
import com.cutegyuseok.freetalk.community.entity.Community;
import org.springframework.http.ResponseEntity;

public interface CommunityService {

    ResponseEntity<?> createCommunity(CommunityDTO.MakeCommunityDTO dto, UserDTO.UserAccessDTO userAccessDTO);
    ResponseEntity<?> showCommunity(Long pk);
    ResponseEntity<?> joinCommunity(UserDTO.UserAccessDTO userAccessDTO,Long pk);
    ResponseEntity<?> disJoinCommunity(UserDTO.UserAccessDTO userAccessDTO,Long pk);
    ResponseEntity<?> showJoinedCommunity(UserDTO.UserAccessDTO userAccessDTO);
    ResponseEntity<?> updateCommunity(Long communityPk,CommunityDTO.UpdateCommunityDTO dto,UserDTO.UserAccessDTO userAccessDTO);
    Community getCommunityEntity(Long id);
    ResponseEntity<?> searchCommunity(String keyword,String sort,int page ,Long categoryPK);
}
