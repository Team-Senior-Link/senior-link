package com.cteam.seniorlink.chat.message;

import com.cteam.seniorlink.chat.room.ChatRoomEntity;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private Long roomId;
    private String sender;
    private String message;
    private LocalDateTime timestamp;

    public ChatMessageEntity toEntity(ChatRoomEntity chatRoom) {
        return new ChatMessageEntity(null, chatRoom, sender, message, timestamp != null ? timestamp : LocalDateTime.now());
    }
}
