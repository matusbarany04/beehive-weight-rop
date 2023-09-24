package com.buzzybees.master.controllers.template;

import com.buzzybees.master.controllers.AuthController;
import com.buzzybees.master.exceptions.ApiException;
import com.buzzybees.master.exceptions.NoPermissionException;
import com.buzzybees.master.users.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class CookieAuthController extends DatabaseController {

    protected long currentUserId;


    @ModelAttribute
    public void authorize(HttpServletRequest request) throws NoPermissionException {
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

        throw new NoPermissionException();
    }

    @ExceptionHandler({ ApiException.class })
    public ApiResponse handleException(Exception e) {
        return new ApiResponse(e.getMessage());
    }
}
