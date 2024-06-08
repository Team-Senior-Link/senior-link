package com.cteam.seniorlink.user;

import com.cteam.seniorlink.chat.ChatService;
import com.cteam.seniorlink.chat.room.ChatRoomDto;
import com.cteam.seniorlink.schedule.ScheduleDto;
import com.cteam.seniorlink.schedule.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ChatService chatService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("request", new UserCreateRequest());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        if (!request.getPassword1().equals(request.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "user/signup";
        }

        userService.saveUser(request);
        return "user/login";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    // 마이페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String getMypageData(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            UserDto userDto = userService.getMember(username);
            List<ScheduleDto> scheduleList = userService.getMypageScheduleByUsername(username);
            List<ChatRoomDto> rooms = chatService.findRoomsByUser(username);
            model.addAttribute("user", userDto);
            model.addAttribute("schedule", scheduleList);
            model.addAttribute("list", rooms);
        }
        return "user/mypage";
    }

    // 마이페이지 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/edit")
    public String editMypageForm(Model model, Principal principal) {
        String username = principal.getName();
        UserEditRequest request = userService.getUserEditRequest(username);
        model.addAttribute("user", request);
        return "user/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage/edit")
    public String editMypageData(@ModelAttribute UserEditRequest request,
                                 @RequestParam("profileImgPath") MultipartFile profileImgFile,
                                 Principal principal) throws IOException {
        userService.editUser(principal.getName(), request, profileImgFile);
        return "redirect:/user/mypage";
    }

    // 자격 인증 상태 (0: 대기중, 1: 인증, 2: 불인증)
    // 자격 인증
    @GetMapping("/approve")
    public String approveStatus(long userId) {
        userService.updateCertificateStatus(userId, 1);
        return "redirect:/certification/list";
    }

    //자격 불인증
    @GetMapping("/disapprove")
    public String disapproveStatus(long userId) {
        userService.updateCertificateStatus(userId, 2);
        return "redirect:/certification/list";
    }


}