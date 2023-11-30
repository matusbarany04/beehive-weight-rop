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

    /**
     * Handles user login through a POST request with form-urlencoded data.
     *
     * @param mail     Email parameter from the login form.
     * @param password Password parameter from the login form.
     * @param response HttpServletResponse for setting cookies and redirects.
     * @return Redirect path based on login result: "/dashboard" if successful, "/login?invalid=true" if failed.
     */
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


    /**
     * Handles user logout through a POST request to "/logoutUser".
     *
     * @param ssid     Session ID obtained from the SSID cookie.
     * @param response HttpServletResponse for clearing the SSID cookie and handling redirects.
     * @return Redirect path to "/login" after successfully logging out.
     */
    @PostMapping(value = "/logoutUser")
    public String logoutUser(@CookieValue(value = SSID, defaultValue = "") String ssid, HttpServletResponse response) {
        Cookie cookie = new Cookie(SSID, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        UserService.logoutUser(ssid);

        return "redirect:/login";
    }


    /**
     * Handles user registration through a POST request with form-urlencoded data.
     *
     * @param user The User object containing registration details.
     * @return JSON response indicating registration status:
     *         - {"status": "exists"} if the user already exists.
     *         - {"status": "ok"} if registration is successful, followed by a redirect to "/accountCreated".
     */
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

    /**
     * Handles user password change through a POST request with form-urlencoded data.
     *
     * @param email            The email of the user whose password is being changed.
     * @param currentPassword  The current password for verification.
     * @param newPassword      The new password to be set.
     * @param response         HttpServletResponse for handling redirects.
     * @return Redirect path to "/dashboard/settings/newpassword?passwordChanged=true" if successful,
     *         otherwise "/dashboard/settings/newpassword?passwordChanged=false".
     */
    @PostMapping(value = {"/changePassword"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String changePassword(
            @RequestParam("email") String email,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            HttpServletResponse response) {

        try {
            // Verify the current password
            String currentPasswdHash = PasswordUtils.hashPasswd(currentPassword);
            User user = userRepository.checkUser(email, currentPasswdHash);

            if (user != null) {
                // Hash the new password and update in the repository
                String newPasswdHash = PasswordUtils.hashPasswd(newPassword);
                userRepository.updateUserPassword(user.getId(), newPasswdHash);

                return "redirect:/dashboard/settings/newpassword?passwordChanged=true";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard/settings/newpassword?passwordChanged=false";
    }


    /**
     * Handles a GET request to check if a user with the given email exists.
     *
     * @param email The email to check for existence.
     * @return ResponseEntity with a JSON response indicating existence:
     *         - {"exists": true} if a user with the email exists.
     *         - {"exists": false} if no user with the email is found.
     */
    @RequestMapping(value = "/emailExists/{email}", method = RequestMethod.GET)
    public ResponseEntity<String> getUserByEmail(@PathVariable("email") String email) {
        JSONObject response = new JSONObject();
        response.put("exists", userRepository.getUserByEmail(email) != null);
        return ResponseEntity.ok(response.toString());
    }
}
