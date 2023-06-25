package com.cutegyuseok.freetalk.chat.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChatDTO {
//    public static class ChatMessageDTO{
//        public enum MessageType{
//            ENTER, TALK
//        }
//
//        private MessageType type;
//        private Long roomPK;
//        private Long sender;
//        private String message;
//    }
//    public static class ChatRoomDTO{
//        private Long roomPK;
//        private String roomName;
//        private Set<WebSocketSession> sessions = new HashSet<>();
//    }

    public static class ChatRoomListDTO{
        private Long roomPK;
        private String roomName;
        private String lastMessage;
    }
}
