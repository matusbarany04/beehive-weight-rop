package com.buzzybees.master.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class FrontendController {

    @GetMapping("")
    public String handleIndexDashboard() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String handleDashboardHome() {
        return "dashboard";
    }
    @GetMapping("/dashboard/{path:[^\\.]*}")
    public String handleAllPaths() {
        return "dashboard";
    }
}
