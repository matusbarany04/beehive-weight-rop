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

    /**
     * checks all request whether session id is present
     * @param request http request
     * @throws NoPermissionException when request is unauthorized
     */
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
    public ApiResponse handleException(ApiException e) {
        if(!e.isErrorPayloadEmpty()){
            return new ApiResponse(e.getMessage(), e.getErrorPayload());
        }

        return new ApiResponse(e.getMessage());
    }
}
