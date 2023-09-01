package com.buzzybees.master.controllers;

import com.buzzybees.master.security.PasswordUtils;
import com.buzzybees.master.tables.User;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    public static final String SSID = "sessionid";

    private final String BASE_PATH = "src/main/resources/public/";


    @GetMapping("/")
    public String home(@CookieValue(value = SSID, defaultValue = "") String ssid) {
        return "dashboard";
        /*if(ssid.length() == 0) return "redirect:/login";
        else return "redirect:/dashboard";*/
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {

        return "";
    }

    @PostMapping(value = {"/loginUser"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String login(@RequestParam("email") String mail, @RequestParam("password") String password, HttpServletResponse response) {

        User user = null;

        try {

            if (password != null) {
                String passwdHash = PasswordUtils.hashPasswd(password);
                user = userRepository.checkUser(mail, passwdHash);

                if (user != null) {
                    String token = UserService.loginUser(user);
                    response.addCookie(new Cookie(SSID, token));
                }
            }

            return "redirect:/dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            return "login";
        }
    }


    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Path file = Paths.get(BASE_PATH + filename);

        Resource resource = new FileSystemResource(file);

        String contentType = determineContentType(filename);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    private String determineContentType(String filename) {
        if (filename.endsWith(".js")) {
            return "application/javascript";
        } else if (filename.endsWith(".css")) {
            return "text/css";
        } // Add more as necessary
        return "application/octet-stream";
    }
}
