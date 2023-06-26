package com.cutegyuseok.freetalk.chat.dto;

import com.cutegyuseok.freetalk.chat.entity.ChatRoom;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.List;
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

        public ChatRoomListDTO(ChatRoom chatRoom){
            this.roomPK = chatRoom.getPk();
            this.roomName = chatRoom.getRoomName();
            this.lastMessage = chatRoom.getMessageList().get(chatRoom.getMessageList().size()-1).getMessage();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateRoomReqDTO{
        @ApiModelProperty(value = "초대 사용자 PK LIST" ,required = true)
        private List<Long> inviteUserList;
        @ApiModelProperty(value = "초대 사용자 PK LIST")
        private String roomName;
    }
}
