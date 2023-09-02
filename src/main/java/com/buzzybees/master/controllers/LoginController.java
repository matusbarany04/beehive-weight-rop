package com.buzzybees.master.controllers;

import com.buzzybees.master.security.PasswordUtils;
import com.buzzybees.master.tables.User;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    public static final String SSID = "sessionid";



    @GetMapping("/")
    public String home(@CookieValue(value = SSID, defaultValue = "") String ssid) {
        if(ssid.length() == 0) return "index";
        else return "redirect:/dashboard";
    }

    @GetMapping("/{path:[^\\.]*}")
    public String handleGeneralPage() {
        return "index";
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




}
