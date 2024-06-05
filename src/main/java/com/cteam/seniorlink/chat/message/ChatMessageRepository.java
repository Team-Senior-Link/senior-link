package com.cteam.seniorlink.chat.message;

import com.cteam.seniorlink.chat.room.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByChatRoomId(Long roomId);
}
