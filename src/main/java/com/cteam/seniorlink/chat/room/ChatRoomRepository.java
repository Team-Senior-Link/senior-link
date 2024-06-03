package com.cteam.seniorlink.chat.room;

import com.cteam.seniorlink.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    List<ChatRoomEntity> findBySenderUsernameOrReceiverUsername(String senderUsername, String receiverUsername);

    Optional<ChatRoomEntity> findBySenderAndReceiver(UserEntity sender, UserEntity receiver);
}