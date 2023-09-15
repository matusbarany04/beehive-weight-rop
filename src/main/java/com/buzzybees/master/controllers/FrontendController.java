package com.buzzybees.master.controllers;

import com.buzzybees.master.users.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FrontendController {

    @Value("${localhost}")
    boolean localhost;

    private final String BUNDLE_PATH_LOCALHOST = "src/main/resources/bundle/";

    @GetMapping("/dashboard/**/{path:[^\\.]*}")
    public String handleDashboardPaths(@CookieValue(name = AuthController.SSID, defaultValue = "") String ssid, @PathVariable String path) {
        return UserService.isTokenValid(ssid) ? "dashboard" : "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String handleDashboardHome(@CookieValue(name = AuthController.SSID, defaultValue = "") String ssid) {
        return UserService.isTokenValid(ssid) ? "dashboard" : "redirect:/login";
    }

    @GetMapping("bundle/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws FileNotFoundException {
        Path file = Paths.get(BUNDLE_PATH_LOCALHOST + filename);

        Resource resource = localhost ? new FileSystemResource(file) : new ClassPathResource("bundle/" + filename);;
        String contentType = determineContentType(filename);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    private String determineContentType(String filename) {
        if (filename.endsWith(".js")) {
            return "application/javascript";
        } else if (filename.endsWith(".css")) {
            return "text/css";
        }
        return "application/octet-stream";
    }
}
