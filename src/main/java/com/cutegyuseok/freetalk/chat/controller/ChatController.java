package com.cutegyuseok.freetalk.chat.controller;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.chat.dto.ChatDTO;
import com.cutegyuseok.freetalk.chat.service.ChatService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    @GetMapping("/rooms")
    @ApiOperation(value = "채팅방 리스트 가져오기 ")
    public ResponseEntity<?> findRooms(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO) {
        return chatService.findRooms(userAccessDTO);
    }
    @PostMapping("/rooms")
    @ApiOperation(value = "채팅방 만들기")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@RequestBody ChatDTO.CreateRoomReqDTO dto){
        return chatService.createRoom(userAccessDTO,dto);
    }
    @PostMapping("/rooms/{roomPK}")
    @ApiOperation(value = "채팅방에 초대하기")
    public ResponseEntity<?> inviteRoom(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@RequestBody ChatDTO.InviteReqDTO dto,@PathVariable Long roomPK){
        return chatService.inviteRoom(userAccessDTO,dto,roomPK);
    }
    @DeleteMapping("/rooms/{roomPK}")
    @ApiOperation(value = "채팅방 떠나기")
    public ResponseEntity<?> leaveRoom(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@PathVariable Long roomPK){
        return chatService.leaveRoom(userAccessDTO,roomPK);
    }
    @GetMapping("/{roomPK}")
    @ApiOperation(value = "채팅 가져오기")
    public ResponseEntity<?> getMessage(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@PathVariable Long roomPK,@RequestParam(required = false, defaultValue = "1") int page){
        return chatService.getMessage(userAccessDTO,roomPK,page);
    }
    @GetMapping("/rooms/{roomPK}")
    @ApiOperation(value = "채팅방 정보 가져오기")
    public ResponseEntity<?> getRoomInfo(@AuthenticationPrincipal UserDTO.UserAccessDTO userAccessDTO,@PathVariable Long roomPK){
        return chatService.getRoomInfo(userAccessDTO,roomPK);
    }
}
