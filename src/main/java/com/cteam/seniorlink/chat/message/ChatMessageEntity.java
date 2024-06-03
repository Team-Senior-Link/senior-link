package com.cteam.seniorlink.chat.message;

import com.cteam.seniorlink.chat.room.ChatRoomEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_message")
@Entity
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoomEntity chatRoom;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String message;

    private LocalDateTime timestamp;

}
