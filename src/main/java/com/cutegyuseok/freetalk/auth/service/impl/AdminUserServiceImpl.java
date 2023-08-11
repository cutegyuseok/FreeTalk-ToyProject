package com.cutegyuseok.freetalk.auth.service.impl;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.repository.UserRepository;
import com.cutegyuseok.freetalk.auth.service.AdminUserService;
import com.cutegyuseok.freetalk.community.entity.Community;
import com.cutegyuseok.freetalk.community.service.CommunityService;
import com.cutegyuseok.freetalk.global.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.cutegyuseok.freetalk.global.config.PageSizeConfig.User_List_Size;

@RequiredArgsConstructor
@Service
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final CommunityService communityService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> checkUserList(String userName, String userNickName, String userEmail, String userRole, String userStatus, Long communityId, int page) {
        try {
            if (page < 1) {
                throw new IllegalArgumentException();
            }
            PageRequest pageable = PageRequest.of(page - 1, User_List_Size);
            Community community = null;
            if (communityId != null) {
                community = communityService.getCommunityEntity(communityId);
            }
            Page<User> users = userRepository.checkUserList(userName, userNickName, userEmail, userRole, userStatus, community, pageable);
            if (users.getTotalElements() < 1) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            PageResponseDTO pageResponseDTO = new PageResponseDTO(users);
            pageResponseDTO.setContent(users.getContent().stream().map(UserDTO.ShowUserListByAdmin::new).collect(Collectors.toList()));
            return new ResponseEntity<>(pageResponseDTO, HttpStatus.OK);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateUserByAdmin(UUID userPK, UserDTO.UpdateUserByAdmin dto) {
        try {
            User user = userRepository.findById(userPK).orElseThrow(NoSuchElementException::new);
            if (dto.getPassword() != null) {
                dto.setPassword(encodingPassword(dto.getPassword()));
            }
            user.updateUserByAdmin(dto);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getUserInfoByAdmin(UUID userPK) {
        try {
            User user = userRepository.findById(userPK).orElseThrow(NoSuchElementException::new);
            return new ResponseEntity<>(new UserDTO.ShowUserByAdmin(user),HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }
}