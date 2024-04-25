package com.cteam.seniorlink.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class MainController {
    @GetMapping("/")
    public String main() {
        return "index";
    }
}
