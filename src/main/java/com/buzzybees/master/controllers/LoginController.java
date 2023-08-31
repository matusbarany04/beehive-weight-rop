package com.buzzybees.master.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class LoginController {

    @GetMapping("/")
    public String home(@CookieValue(value = "sessionid", defaultValue = "") String ssid) {
        if(ssid.length() == 0) return "redirect:/login";
        else return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "";
    }

    @GetMapping("/register")
    public String register() {

        return "";
    }
}
