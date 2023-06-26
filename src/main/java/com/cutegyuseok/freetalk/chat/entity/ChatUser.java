package com.cutegyuseok.freetalk.chat.entity;

import com.cutegyuseok.freetalk.auth.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_user")
public class ChatUser {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_room")
    private ChatRoom chatRoom;

    public ChatUser(User user,ChatRoom chatRoom){
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
