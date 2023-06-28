package com.cutegyuseok.freetalk.chat.dto;

import com.cutegyuseok.freetalk.auth.dto.UserDTO;
import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.chat.entity.ChatMessage;
import com.cutegyuseok.freetalk.chat.entity.ChatRoom;
import com.cutegyuseok.freetalk.chat.entity.ChatUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class InviteReqDTO{
        @ApiModelProperty(value = "초대 사용자 PK LIST" ,required = true)
        private List<Long> inviteUserList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SendMessageDTO{
        @ApiModelProperty(value = "보낼 메세지" ,required = true)
        private String message;
    }

    @Getter
    public static class MessageResDTO{
        private String message;
        private Long userPK;
        private String date;

        public MessageResDTO(ChatMessage chatMessage){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");
            this.message = chatMessage.getMessage();
            this.userPK = chatMessage.getUser().getPk();
            this.date = chatMessage.getCreatedDate().format(dateTimeFormatter);
        }
    }

    @Getter
    public static class RoomInfoDTO{
        private UserDTO.ShowOwnerWithoutSI viewer;
        private List<UserDTO.ShowOwnerWithoutSI> users;

        public RoomInfoDTO(User user,List<ChatUser> userList){
            this.viewer = new UserDTO.ShowOwnerWithoutSI(user);
            this.users = userList.stream().map(e -> new UserDTO.ShowOwnerWithoutSI(e.getUser())).collect(Collectors.toList());
        }
    }
}
