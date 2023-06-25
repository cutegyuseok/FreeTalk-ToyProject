package com.cutegyuseok.freetalk.chat.service.impl;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.service.UserService;
import com.cutegyuseok.freetalk.chat.dto.ChatDTO;
import com.cutegyuseok.freetalk.chat.entity.ChatRoom;
import com.cutegyuseok.freetalk.chat.entity.ChatUser;
import com.cutegyuseok.freetalk.chat.repository.ChatRoomRepository;
import com.cutegyuseok.freetalk.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl {

    private final ChatUserRepository chatUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    public List<ChatDTO.ChatRoomListDTO> findRooms(UserDTO.UserAccessDTO userAccessDTO){
        User user = userService.getUser(userAccessDTO);
        List<ChatUser> rooms = chatUserRepository.findAllByUser(user);
        if (rooms.isEmpty())return null;

    }
}
