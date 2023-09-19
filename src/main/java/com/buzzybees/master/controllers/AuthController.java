package com.buzzybees.master.controllers;

import com.buzzybees.master.security.PasswordUtils;
import com.buzzybees.master.tables.User;
import com.buzzybees.master.users.Mailer;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.UserService;
import com.buzzybees.master.users.settings.Settings;
import com.buzzybees.master.users.settings.SettingsRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SettingsRepository settingsRepository;

    public static final String SSID = "sessionid";

    @GetMapping("/")
    public String home(@CookieValue(value = SSID, defaultValue = "") String ssid) {
        long id = UserService.getUserIdByToken(ssid);
        if (id == 0) return "index";
        else return "redirect:/dashboard";
    }

    @GetMapping("/{path:[^.]*}")
    public String handleGeneralPage(@PathVariable String path, @CookieValue(value = SSID, defaultValue = "") String ssid) {
        long id = UserService.getUserIdByToken(ssid);
        if (id == 0) return "index";
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
                    if (token == null) return "redirect:/login";
                    response.addCookie(new Cookie(SSID, token));
                    return "redirect:/dashboard";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/login?invalid=true";
    }

    @PostMapping(value = "/logoutUser")
    public String logoutUser(@CookieValue(value = SSID, defaultValue = "") String ssid, HttpServletResponse response) {
        Cookie cookie = new Cookie(SSID, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        UserService.logoutUser(ssid);

        return "redirect:/login";
    }

    @PostMapping(value = {"/registerUser"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String register(@ModelAttribute User user) {
        JSONObject response = new JSONObject();

        if (userRepository.getUserByEmail(user.getEmail()) != null) {
            response.put("status", "exists");
            return response.toString();
        }
        //saving newly created user
        User savedUser = userRepository.save(user);

        // creating fresh settings
        Settings.createSettingsIfNonExistent(savedUser.getId(), userRepository, settingsRepository);

        long id = userRepository.getUserByEmail(user.getEmail()).id;
        Mailer.sendVerification(user.getEmail(), id);

        response.put("status", "ok");

        return "redirect:/accountCreated";
    }

    @RequestMapping(value = "/emailExists/{email}", method = RequestMethod.GET)
    public ResponseEntity<String> getUserByEmail(@PathVariable("email") String email) {
        JSONObject response = new JSONObject();
        response.put("exists", userRepository.getUserByEmail(email) != null);
        return ResponseEntity.ok(response.toString());
    }
}
