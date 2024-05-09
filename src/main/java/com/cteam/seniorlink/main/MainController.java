package com.cteam.seniorlink.main;

import org.springframework.web.bind.annotation.GetMapping;

public class MainController {
    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
}
