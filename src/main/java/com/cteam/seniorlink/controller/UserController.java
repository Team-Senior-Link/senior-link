package com.cteam.seniorlink.controller;

import com.cteam.seniorlink.dto.user.UserCreateRequest;
import com.cteam.seniorlink.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("request", new UserCreateRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        if (!request.getPassword1().equals(request.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup";
        }

        userService.saveUser(request);
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


}
