package com.cutegyuseok.freetalk.global.config.webSocket;

import com.cutegyuseok.freetalk.chat.dto.ChatDTO;
import com.cutegyuseok.freetalk.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/{roodID}")
    public void sendMessage(@DestinationVariable("roodID") Long roodID, ChatDTO.SendMessageDTO chatDto, SimpMessageHeaderAccessor accessor) {
        if (roodID==chatDto.getRoomPK()) {
            ChatDTO.MessageResDTO res = chatService.sendMessage(chatDto);
            simpMessagingTemplate.convertAndSend("/sub/chat/" + chatDto.getRoomPK(), res);
        }
    }
}