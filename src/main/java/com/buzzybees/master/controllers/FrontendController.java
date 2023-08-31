package com.buzzybees.master.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class FrontendController {

    @GetMapping("/")
    public String handleIndexDashboard() {
        return "index";
    }

    @GetMapping("/{path:[^\\.]*}")
    public String handleAllPaths() {
        System.out.println("cALLED!!!");
        return "index";
    }
}
