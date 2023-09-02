package com.buzzybees.master.controllers;

import com.buzzybees.master.security.PasswordUtils;
import com.buzzybees.master.tables.User;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    public static final String SSID = "sessionid";

    @GetMapping("/")
    public String home(@CookieValue(value = SSID, defaultValue = "") String ssid) {
        long id = UserService.getUserIdByToken(ssid);
        if(id == 0) return "index";
        else return "redirect:/dashboard";
    }

    @GetMapping("/{path:[^.]*}")
    public String handleGeneralPage(@PathVariable String path, @CookieValue(value = SSID, defaultValue = "") String ssid) {
        long id = UserService.getUserIdByToken(ssid);
        if(id == 0) return "index";
        else return "redirect:/dashboard";
    }

    @PostMapping(value = {"/loginUser"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(@RequestParam("email") String mail, @RequestParam("password") String password, HttpServletResponse response) {

        try {
            if (password != null) {
                String passwdHash = PasswordUtils.hashPasswd(password);
                User user = userRepository.checkUser(mail, passwdHash);

                if (user != null) {
                    String token = UserService.loginUser(user);
                    response.addCookie(new Cookie(SSID, token));
                    return "redirect:/dashboard";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/login";
    }

    @PostMapping(value = "/logoutUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie(SSID, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        JSONObject output = new JSONObject();
        output.put("status", "ok");
        return ResponseEntity.ok(output.toString());
    }
}
