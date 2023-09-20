package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.StatusRepository;
import com.buzzybees.master.controllers.template.CookieAuthController;
import com.buzzybees.master.notifications.NotificationRepository;
import com.buzzybees.master.notifications.ReminderRepository;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.settings.Settings;
import com.buzzybees.master.users.settings.SettingsRepository;
import com.sun.mail.imap.protocol.BODY;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

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

        HttpStatus error = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof UserNotValidException) {
            error = HttpStatus.NOT_FOUND;
        } else if (ex instanceof SessionNotSetException) {
            error = HttpStatus.UNAUTHORIZED;
        }

        errorResponse.put("error", error.value());
        return new ResponseEntity<>(errorResponse, error);
    }

    private SettingsRepository settingsRepository;
    private UserRepository userRepository;


    @ModelAttribute
    public void validateUser(@PathVariable("userId") Long userId) {
        if (currentUserId == 0) {
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

    /**
     * Updates settings in batch
     *
     * @param userId       The user's ID.
     * @param settingsJson Contains all Setting variables
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/highHumidity" \
     * -H "Content-Type: application/json" \
     * -d '{"dont_disturb_from": 1500}'
     */
    @PutMapping("/updateBatch")
    public ResponseEntity<Object> updateSettings(@PathVariable Long userId, @RequestBody String settingsJson) {
        JSONObject json = new JSONObject(settingsJson);
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        if (userSettings == null) {
            // Handle the case where no settings exist for the user.
            return ResponseEntity.notFound().build();
        }

        if (json.has("dont_disturb_from")) userSettings.setDontDisturbFrom(json.getLong("dont_disturb_from"));
        if (json.has("dont_disturb")) userSettings.setDontDisturb(json.getBoolean("dont_disturb"));
        if (json.has("dont_disturb_to")) userSettings.setDontDisturbTo(json.getLong("dont_disturb_to"));
        if (json.has("send_notifications")) userSettings.setSendNotifications(json.getBoolean("send_notifications"));
        if (json.has("use_user_login_mail")) userSettings.setUseUserLoginMail(json.getBoolean("use_user_login_mail"));
        if (json.has("alt_mail")) userSettings.setAltMail(json.getString("alt_mail"));
        if (json.has("low_battery")) userSettings.setLowBattery(json.getBoolean("low_battery"));

        if (json.has("battery_low_threshold"))
            userSettings.setBatteryLowThreshold(json.getInt("battery_low_threshold"));
        if (json.has("high_humidity")) userSettings.setHighHumidity(json.getBoolean("high_humidity"));
        if (json.has("high_humidity_threshold"))
            userSettings.setHighHumidityThreshold(json.getInt("high_humidity_threshold"));
        if (json.has("low_humidity")) userSettings.setLowHumidity(json.getBoolean("low_humidity"));
        if (json.has("low_humidity_threshold"))
            userSettings.setLowHumidityThreshold(json.getInt("low_humidity_threshold"));
        if (json.has("heavy_weight")) userSettings.setHeavyWeight(json.getBoolean("heavy_weight"));
        if (json.has("heavy_weight_threshold"))
            userSettings.setHeavyWeightThreshold(json.getInt("heavy_weight_threshold"));
        if (json.has("light_weight")) userSettings.setLightWeight(json.getBoolean("light_weight"));
        if (json.has("light_weight_threshold"))
            userSettings.setLightWeightThreshold(json.getInt("light_weight_threshold"));

        settingsRepository.save(userSettings);
        return ResponseEntity.ok().build();
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
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/highHumidity" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/highHumidity")
    public ResponseEntity<Object> setHighHumidity(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        if (Objects.nonNull(userSettings)) {
            userSettings.setHighHumidity(0 != body.getOrDefault("value", 0));
            settingsRepository.save(userSettings);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Updates the light weight setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the light weight value with key "lightWeight". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/lightWeight" \
     * -H "Content-Type: application/json" \
     * -d '{"lightWeight": 1}'
     */
    @PutMapping("/lightWeight")
    public ResponseEntity<Object> setLightWeight(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        if (Objects.nonNull(userSettings)) {
            userSettings.setLightWeight(0 != body.getOrDefault("lightWeight", 0));
            settingsRepository.save(userSettings);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Updates the heavy weight setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the heavy weight value with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/heavyWeight" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/heavyWeight")
    public ResponseEntity<Object> setHeavyWeight(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setHeavyWeight((boolean) value);
        });
    }

    /**
     * Updates the heavy weight threshold for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the heavy weight threshold with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/heavyWeightTreshold" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/heavyWeightTreshold")
    public ResponseEntity<Object> setHeavyWeightTreshold(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setHeavyWeightThreshold((int) value);
        });
    }


    /**
     * Updates the high humidity threshold for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the high humidity threshold with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/highHumidityThreshold" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/highHumidityThreshold")
    public ResponseEntity<Object> setHighHumidityThreshold(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setHighHumidityThreshold((int) value);
        });
    }

    /**
     * Updates the low humidity threshold for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the threshold value with key "lowHumidity". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/highHumidity" \
     * -H "Content-Type: application/json" \
     * -d '{"highHumidity": 1}'
     */
    @PutMapping("/setLowHumidity")
    public ResponseEntity<Object> setLowHumidity(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setLowHumidity((boolean) value);
        });
    }

    /**
     * Updates the "Do Not Disturb From" time setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the "Do Not Disturb From" time with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/dontDisturbFrom" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/dontDisturbFrom")
    public ResponseEntity<Object> setDontDisturbFrom(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setDontDisturbFrom((long) value);
        });
    }

    /**
     * Updates the "Do Not Disturb To" time setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the "Do Not Disturb To" time with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/dontDisturbTo" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/dontDisturbTo")
    public ResponseEntity<Object> setDontDisturbTo(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setDontDisturbTo((long) value);
        });
    }

    /**
     * Updates the "Do Not Disturb" setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the "Do Not Disturb" setting with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/dontDisturb" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/dontDisturb")
    public ResponseEntity<Object> setDontDisturb(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setDontDisturb((boolean) value);
        });
    }

    /**
     * Updates the alternate email address setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the alternate email with key "value". Defaults to the user's primary email if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/altMail" \
     * -H "Content-Type: application/json" \
     * -d '{"value": "alternate@example.com"}'
     */
    @PutMapping("/altMail")
    public ResponseEntity<Object> setAltMail(@PathVariable Long userId, @RequestBody Map<String, String> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, body.getOrDefault("value", userRepository.getUserById(userId).getEmail()), (Object value) -> {
            userSettings.setAltMail((String) value);
        });
    }

    /**
     * Updates the "Send Notifications" setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the "Send Notifications" setting with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/sendNotifications" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/sendNotifications")
    public ResponseEntity<Object> setSendNotifications(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setSendNotifications((boolean) value);
        });
    }

    /**
     * Updates the setting to use the user's login email.
     *
     * @param userId The user's ID.
     * @param body   Contains the setting with key "value". Defaults to 1 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/useUserLoginMail" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/useUserLoginMail")
    public ResponseEntity<Object> setUseUserLoginMail(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 1), (Object value) -> {
            userSettings.setUseUserLoginMail((boolean) value);
        });
    }

    /**
     * Updates the low battery notification setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the low battery setting with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/lowBattery" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/lowBattery")
    public ResponseEntity<Object> setLowBattery(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setLowBattery((boolean) value);
        });
    }

    /**
     * Updates the battery low threshold setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the battery low threshold with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/batteryLowThreshold" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 20}'
     */
    @PutMapping("/batteryLowThreshold")
    public ResponseEntity<Object> setBatteryLowThreshold(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setBatteryLowThreshold((int) value);
        });
    }

    /**
     * Updates the low humidity threshold setting for a specific user.
     *
     * @param userId The user's ID.
     * @param body   Contains the low humidity threshold with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/{USER_ID}/lowHumidityThreshold" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 30}'
     */
    @PutMapping("/lowHumidityThreshold")
    public ResponseEntity<Object> setLowHumidityThreshold(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        return saveSettingItem(userId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setLowHumidityThreshold((int) value);
        });
    }

    /**
     * Helper function to bundle boilerplate to one function.
     *
     * @param userId The ID of the users settings
     * @param value  The new value of the setting to be saved.
     * @param action A {@link Consumer} applies given value to the user's settings.
     * @return {@link ResponseEntity} with OK status if the setting was updated successfully; NOT_FOUND status otherwise.
     */
    public ResponseEntity<Object> saveSettingItem(Long userId, Object value, Consumer<Object> action) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);

        if (Objects.nonNull(userSettings)) {
            action.accept(value);
            settingsRepository.save(userSettings);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
