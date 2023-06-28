package com.cutegyuseok.freetalk.chat.repository;

import com.cutegyuseok.freetalk.chat.entity.ChatMessage;
import com.cutegyuseok.freetalk.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findAllByChatRoomOrderByPkDesc(ChatRoom chatRoom, Pageable pageable);
}
