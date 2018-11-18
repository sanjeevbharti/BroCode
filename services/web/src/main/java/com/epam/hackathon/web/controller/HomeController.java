package com.epam.hackathon.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {

    @GetMapping("/")
    public String serveHomePage() {
        return "/home.html";
    }



}
