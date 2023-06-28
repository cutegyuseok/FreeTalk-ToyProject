package com.cutegyuseok.freetalk.chat.service;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.chat.dto.ChatDTO;
import com.cutegyuseok.freetalk.chat.entity.ChatMessage;
import com.cutegyuseok.freetalk.chat.entity.ChatRoom;
import com.cutegyuseok.freetalk.chat.entity.ChatUser;
import com.cutegyuseok.freetalk.global.response.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.cutegyuseok.freetalk.global.config.PageSizeConfig.Chat_List_Size;

public interface ChatService {

    public ResponseEntity<?> findRooms(UserDTO.UserAccessDTO userAccessDTO);

    public ResponseEntity<?> createRoom(UserDTO.UserAccessDTO userAccessDTO, ChatDTO.CreateRoomReqDTO dto);


    public ResponseEntity<?> inviteRoom(UserDTO.UserAccessDTO userAccessDTO, ChatDTO.InviteReqDTO dto,Long roomPK);

    @Transactional
    public ResponseEntity<?> leaveRoom(UserDTO.UserAccessDTO userAccessDTO,Long roomPK);

    public ResponseEntity<?> sendMessage(ChatDTO.SendMessageDTO dto);
    public ResponseEntity<?> getMessage(UserDTO.UserAccessDTO userAccessDTO,Long roomPK,int page);
    public ResponseEntity<?> getRoomInfo(UserDTO.UserAccessDTO userAccessDTO,Long roomPK);
}
