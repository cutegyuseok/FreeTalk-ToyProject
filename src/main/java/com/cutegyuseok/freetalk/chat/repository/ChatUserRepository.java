package com.cutegyuseok.freetalk.chat.repository;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.chat.entity.ChatRoom;
import com.cutegyuseok.freetalk.chat.entity.ChatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    List<ChatUser> findAllByUser(User user);

    void deleteByUserAndChatRoom(User user, ChatRoom chatRoom);

    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);
    List<ChatUser> findAllByChatRoomAndUserNot(ChatRoom chatRoom,User user);
}
