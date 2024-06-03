package com.cteam.seniorlink.chat.room;

import com.cteam.seniorlink.user.UserEntity;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
    private Long roomId;
    private String sender;
    private String receiver;

    public ChatRoomEntity toEntity(UserEntity sender, UserEntity receiver) {
        ChatRoomEntity chatRoom = new ChatRoomEntity();
        chatRoom.setId(this.roomId);
        chatRoom.setSender(sender);
        chatRoom.setReceiver(receiver);
        return chatRoom;
    }

    public static ChatRoomDto fromEntity(ChatRoomEntity entity) {
        ChatRoomDto dto = new ChatRoomDto();
        dto.setRoomId(entity.getId());
        dto.setSender(entity.getSender().getUsername());
        dto.setReceiver(entity.getReceiver().getUsername());
        return dto;
    }
}
