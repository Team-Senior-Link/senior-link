package com.cteam.seniorlink.chat;

import com.cteam.seniorlink.chat.message.ChatMessageDto;
import com.cteam.seniorlink.chat.room.ChatRoomDto;
import com.cteam.seniorlink.chat.room.ChatRoomEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    // 채팅 목록 출력 (마이페이지 -> 채팅방)
    @GetMapping("/list")
    public String rooms(Model model, Principal principal) {
        String username = principal.getName();
        List<ChatRoomDto> rooms = chatService.findRoomsByUser(username);
        model.addAttribute("list", rooms);
        return "chat/list";
    }

    // 새로운 채팅방 생성 or 기존 채팅방으로 리디렉션 (서비스페이지 -> [문의하기])
    @PostMapping("/room")
    public String createOrFindChatRoom(@RequestParam("serviceId") Long serviceId, Principal principal, RedirectAttributes redirectAttributes) {
        String senderUsername = principal.getName();
        ChatRoomDto chatRoom = chatService.createOrFindChatRoom(senderUsername, serviceId);
        redirectAttributes.addAttribute("roomId", chatRoom.getRoomId());
        return "redirect:/chat/room/{roomId}";
    }

    // 채팅방 입장
    @GetMapping("/room/{roomId}")
    public String getroom(@PathVariable Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "chat/room";
    }

    // 채팅 참여 메시지
    @MessageMapping("/chat/enter")
    public void enterChatRoom(ChatMessageDto message){
        message.setMessage(message.getSender() + "님이 채팅에 참여하였습니다.");
        message.setTimestamp(LocalDateTime.now());
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    // 채팅 전송
    @MessageMapping("/chat/message")
    public void sendChatMessage(ChatMessageDto message){
        message.setTimestamp(LocalDateTime.now());
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        chatService.saveMessage(message);
    }
}
