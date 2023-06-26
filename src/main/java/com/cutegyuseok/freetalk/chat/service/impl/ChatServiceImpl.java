package com.cutegyuseok.freetalk.chat.service.impl;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.repository.UserRepository;
import com.cutegyuseok.freetalk.auth.service.UserService;
import com.cutegyuseok.freetalk.chat.dto.ChatDTO;
import com.cutegyuseok.freetalk.chat.entity.ChatMessage;
import com.cutegyuseok.freetalk.chat.entity.ChatRoom;
import com.cutegyuseok.freetalk.chat.entity.ChatUser;
import com.cutegyuseok.freetalk.chat.repository.ChatRoomRepository;
import com.cutegyuseok.freetalk.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl {

    private final ChatUserRepository chatUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> findRooms(UserDTO.UserAccessDTO userAccessDTO){
        User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
        List<ChatUser> rooms = chatUserRepository.findAllByUser(user);
        if (rooms.isEmpty())return null;
        List<ChatDTO.ChatRoomListDTO> roomList =  rooms.stream().map(e -> new ChatDTO.ChatRoomListDTO(e.getChatRoom())).collect(Collectors.toList());
        return new ResponseEntity<>(roomList,HttpStatus.OK);
    }

    public ResponseEntity<?> createRoom(UserDTO.UserAccessDTO userAccessDTO, ChatDTO.CreateRoomReqDTO dto){
        User creator = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
        List<User> userList = userRepository.findAllByPkIn(dto.getInviteUserList());
        if (userList.isEmpty() || dto.getInviteUserList().size()!=userList.size()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (dto.getRoomName()==null){
            StringBuilder title = new StringBuilder(creator.getNickName());
            for (User user : userList){
                title.append(", ").append(user.getNickName());
            }
            dto.setRoomName(title.toString());
        }
        ChatRoom chatRoom = new ChatRoom(dto.getRoomName());
        chatRoom.getChatUsers().add(new ChatUser(creator,chatRoom));
        for (User user: userList){
            chatRoom.getChatUsers().add(new ChatUser(user,chatRoom));
        }
        chatRoom.getMessageList()
                .add(ChatMessage.builder()
                .chatRoom(chatRoom)
                .message(makeInvitedMessage(creator,userList))
                .build());
        chatRoomRepository.save(chatRoom);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private String makeInvitedMessage(User invitor,List<User> userList){
        StringBuilder sb = new StringBuilder();
        sb.append(invitor.getNickName()).append("님이 ");
        for (int i =0;i<userList.size();i++){
            sb.append(userList.get(i).getNickName()).append("님");
            if (i!=userList.size()-1){
                sb.append(", ");
            }
        }sb.append("을 초대하였습니다.");
        return sb.toString();
    }
}
