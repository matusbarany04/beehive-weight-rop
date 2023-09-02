package com.buzzybees.master.controllers;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FrontendController {

    private final String BUNDLE_PATH = "src/main/resources/bundle/";

    @GetMapping("/dashboard/{path:[^\\.]*}")
    public String handleDashboardPaths(@PathVariable String path) {
        return "dashboard";
    }

    @GetMapping("/dashboard")
    public String handleDashboardHome() {
        return "dashboard";
    }

    @GetMapping("bundle/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Path file = Paths.get(BUNDLE_PATH + filename);

        Resource resource = new FileSystemResource(file);
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
