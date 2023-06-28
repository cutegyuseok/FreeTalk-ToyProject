package com.cutegyuseok.freetalk.chat.service.impl;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.auth.repository.UserRepository;
import com.cutegyuseok.freetalk.auth.service.UserService;
import com.cutegyuseok.freetalk.chat.dto.ChatDTO;
import com.cutegyuseok.freetalk.chat.entity.ChatMessage;
import com.cutegyuseok.freetalk.chat.entity.ChatRoom;
import com.cutegyuseok.freetalk.chat.entity.ChatUser;
import com.cutegyuseok.freetalk.chat.repository.ChatMessageRepository;
import com.cutegyuseok.freetalk.chat.repository.ChatRoomRepository;
import com.cutegyuseok.freetalk.chat.repository.ChatUserRepository;
import com.cutegyuseok.freetalk.chat.service.ChatService;
import com.cutegyuseok.freetalk.global.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.cutegyuseok.freetalk.global.config.PageSizeConfig.Chat_List_Size;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatUserRepository chatUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> findRooms(UserDTO.UserAccessDTO userAccessDTO){
        User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
        List<ChatUser> rooms = chatUserRepository.findAllByUser(user);
        if (rooms.isEmpty())return null;
        List<ChatDTO.ChatRoomListDTO> roomList =  rooms.stream().map(e -> new ChatDTO.ChatRoomListDTO(e.getChatRoom())).collect(Collectors.toList());
        return new ResponseEntity<>(roomList,HttpStatus.OK);
    }
    @Override
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
    @Override
    @Transactional
    public ResponseEntity<?> inviteRoom(UserDTO.UserAccessDTO userAccessDTO, ChatDTO.InviteReqDTO dto,Long roomPK){
        ChatRoom chatRoom = chatRoomRepository.findById(roomPK).orElseThrow(NoSuchElementException::new);
        User invitor = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
        List<User> userList = userRepository.findAllByPkIn(dto.getInviteUserList());
        if (userList == null || userList.size()!= dto.getInviteUserList().size()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (User user: userList){
            chatRoom.getChatUsers().add(new ChatUser(user,chatRoom));
        }
        chatRoom.getMessageList()
                .add(ChatMessage.builder()
                        .chatRoom(chatRoom)
                        .message(makeInvitedMessage(invitor,userList))
                        .build());
        chatRoomRepository.save(chatRoom);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Override
    @Transactional
    public ResponseEntity<?> leaveRoom(UserDTO.UserAccessDTO userAccessDTO,Long roomPK){
        ChatRoom chatRoom = getChatRoom(roomPK);
        User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
        chatUserRepository.deleteByUserAndChatRoom(user,chatRoom);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> sendMessage(ChatDTO.SendMessageDTO dto){
        ChatRoom chatRoom = getChatRoom(dto.getRoomPK());
        User user = userRepository.findById(dto.getWriterPK()).orElseThrow(NoSuchElementException::new);
        chatRoom.getMessageList().add(ChatMessage.builder()
                .message(dto.getMessage())
                .user(user)
                .build());
        return null;
    }
    @Override
    public ResponseEntity<?> getMessage(UserDTO.UserAccessDTO userAccessDTO,Long roomPK,int page){
        ChatRoom chatRoom = getChatRoom(roomPK);
        User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
        PageRequest pageable = PageRequest.of(page - 1, Chat_List_Size);
        if (!chatUserRepository.existsByChatRoomAndUser(chatRoom,user)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Page<ChatMessage> messages = chatMessageRepository.findAllByChatRoomOrderByPkDesc(chatRoom,pageable);
        PageResponseDTO responseDTO = new PageResponseDTO(messages);
        List<ChatDTO.MessageResDTO> list = messages.stream().map(ChatDTO.MessageResDTO::new).collect(Collectors.toList());
        responseDTO.setContent(list);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> getRoomInfo(UserDTO.UserAccessDTO userAccessDTO,Long roomPK){
        ChatRoom chatRoom = getChatRoom(roomPK);
        User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(NoSuchElementException::new);
        if (!chatUserRepository.existsByChatRoomAndUser(chatRoom,user)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<ChatUser> list = chatUserRepository.findAllByChatRoomAndUserNot(user);
        return new ResponseEntity<>(new ChatDTO.RoomInfoDTO(user,list),HttpStatus.OK);
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
    public ChatRoom getChatRoom(Long roomPK){
        return chatRoomRepository.findById(roomPK).orElseThrow(NoSuchElementException::new);
    }
}
