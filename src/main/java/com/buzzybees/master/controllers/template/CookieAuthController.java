package com.buzzybees.master.controllers.template;

import com.buzzybees.master.controllers.AuthController;
import com.buzzybees.master.users.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
@RestController
public abstract class CookieAuthController {

    protected long currentUserId;

    @ModelAttribute("sessionid")
    public void getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + " " + cookie.getValue());
                if (cookie.getName().equals(AuthController.SSID)) {
                    currentUserId = UserService.getUserIdByToken(cookie.getValue());
                    return;
                }
            }
        }

        currentUserId = 0;
    }

}
