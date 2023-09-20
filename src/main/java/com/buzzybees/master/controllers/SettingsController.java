package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.StatusRepository;
import com.buzzybees.master.controllers.template.CookieAuthController;
import com.buzzybees.master.notifications.NotificationRepository;
import com.buzzybees.master.notifications.ReminderRepository;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.settings.Settings;
import com.buzzybees.master.users.settings.SettingsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.*;

// TODO auth

@RestController
@RequestMapping("/dashboardApi/settings/{userId}")
public class SettingsController extends CookieAuthController {

    public class UserNotValidException extends RuntimeException {
        public UserNotValidException(String message) {
            super(message);
        }
    }
    public class SessionNotSetException extends RuntimeException {
        public SessionNotSetException(String message) {
            super(message);
        }
    }
    @ExceptionHandler({UserNotValidException.class, SessionNotSetException.class})
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleExceptions(RuntimeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());

        if (ex instanceof UserNotValidException) {
            errorResponse.put("error", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } else if (ex instanceof SessionNotSetException) {
            errorResponse.put("error", HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } else {
            errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private SettingsRepository settingsRepository;
    private UserRepository userRepository;


    @ModelAttribute
    public void validateUser(@PathVariable("userId") Long userId) {
        if(currentUserId == 0){
           throw new SessionNotSetException("Session id not set");
        }
        if (!isValidUser(userId)) {
            throw new UserNotValidException("User is not valid");
        }
    }


    private boolean isValidUser(Long userId) {
        return userRepository.findById(userId).isPresent();
    }

    @Autowired
    public void initRepos(SettingsRepository settingsRepository, UserRepository users) {
        this.settingsRepository = settingsRepository;
        this.userRepository = users;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSettingsByUserId(@PathVariable Long userId) {
        Settings settings = Settings.getSettingsByUser(userId, settingsRepository);

        Settings.createSettingsIfNonExistent(userId, userRepository, settingsRepository);

        if (settings != null) {
            System.out.println("settings " + settings.toString());
            return new ResponseEntity<>(settings.toJson().toString(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    /**
     * Updates the high humidity threshold for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the threshold value with key "highHumidity". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     *
     * @example
     * curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/highHumidity" \
     *      -H "Content-Type: application/json" \
     *      -d '{"highHumidity": 1}'
     */
    @PutMapping("/highHumidity")
    public ResponseEntity<Object> setHighHumidity(@PathVariable Long userId, @RequestBody Map<String, Integer> body){
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        if (Objects.nonNull(userSettings)) {
            userSettings.setHighHumidity( 0 != body.getOrDefault("highHumidity", 0));
            settingsRepository.save(userSettings);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Updates the low humidity threshold for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the threshold value with key "lowHumidity". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     *
     * @example
     * curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/highHumidity" \
     *      -H "Content-Type: application/json" \
     *      -d '{"highHumidity": 1}'
     */
    @PutMapping("/lowHumidity")
    public ResponseEntity<Object> setLowHumidity(@PathVariable Long userId, @RequestBody Map<String, Integer> body){
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        if (Objects.nonNull(userSettings)) {
            userSettings.setHighHumidity( 0 != body.getOrDefault("lowHumidity", 0));
            settingsRepository.save(userSettings);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
