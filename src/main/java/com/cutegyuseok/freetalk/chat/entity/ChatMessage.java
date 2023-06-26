package com.cutegyuseok.freetalk.chat.entity;

import com.cutegyuseok.freetalk.auth.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @Column(name = "pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne
    @JoinColumn(name = "chat_room")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @Column(name = "message", nullable = false)
    private String message;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Builder
    public ChatMessage(ChatRoom chatRoom,User user,String message){
        this.chatRoom = chatRoom;
        this.user = user;
        this.message = message;
    }
}
