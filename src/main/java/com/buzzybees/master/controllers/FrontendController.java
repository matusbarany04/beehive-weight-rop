package com.buzzybees.master.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class FrontendController {

    @GetMapping("/dashboard")
    public String handleDashboardHome() {
        return "dashboard";
    }

    @GetMapping("/dashboard/{path:[^\\.]*}")
    public String handleAllIndexPaths() {
        return "dashboard";
    }

    @GetMapping("")
    public String handleIndexDashboard() {
        return "index";
    }

    @GetMapping("/{path:[^\\.]*}")
<<<<<<< HEAD
=======
    public String handleAllIndexPsths() {
        return "index";
    }
    @GetMapping("/dashboard")
    public String handleDashboardHome() {
        return "dashboard";
    }
    @GetMapping("/dashboard/{path:[^\\.]*}")
>>>>>>> 14d0dafebeddc90ee25cb478d420315cff835ee2
    public String handleAllPaths() {
        return "index";
    }


}
