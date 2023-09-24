package com.buzzybees.master.controllers;

import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.CookieAuthController;
import com.buzzybees.master.exceptions.ItemNotFoundException;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.settings.Settings;
import com.buzzybees.master.users.settings.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@RestController
@RequestMapping("/dashboardApi/settings")
public class SettingsController extends CookieAuthController {

    public static class UserNotValidException extends RuntimeException {
        public UserNotValidException(String message) {
            super(message);
        }
    }

    public static class SessionNotSetException extends RuntimeException {
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


    public void validateUser() {
        if (currentUserId == 0) {
            throw new SessionNotSetException("Session id not set");
        }
        if (!isValidUser(currentUserId)) {
            throw new UserNotValidException("User is not valid");
        }
    }
/*
    @ModelAttribute
    public void combinedMethod(HttpServletRequest request) {
        super.getCookies(request);
        validateUser();
    }*/

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
     * @param settings Contains all Setting variables
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/highHumidity" \
     * -H "Content-Type: application/json" \
     * -d '{"dont_disturb_from": 1500}'
     */
    @PutMapping("/updateBatch")
    public ApiResponse updateSettings(@ModelAttribute Settings settings) throws ItemNotFoundException {
        SettingsRepository settingsRepository = getRepo(Settings.class);

        if (settings == null) {
            // Handle the case where no settings exist for the user.
            throw new ItemNotFoundException();
        }

        settingsRepository.save(settings);
        return new ApiResponse();
    }

    @GetMapping(value = "/getData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse getSettingsByUserId() throws ItemNotFoundException {
        Settings settings = Settings.getSettingsByUser(currentUserId, settingsRepository);
        Settings.createSettingsIfNonExistent(currentUserId, userRepository, settingsRepository);

        if(settings == null) throw new ItemNotFoundException();

        return new ApiResponse("settings", settings);
    }


    /**
     * Updates the high humidity threshold for a specific user.
     *
     * @param body Contains the threshold value with key "highHumidity". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/highHumidity" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/highHumidity")
    public ResponseEntity<Object> setHighHumidity(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

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
     * @param body Contains the light weight value with key "lightWeight". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/lightWeight" \
     * -H "Content-Type: application/json" \
     * -d '{"lightWeight": 1}'
     */
    @PutMapping("/lightWeight")
    public ResponseEntity<Object> setLightWeight(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

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
     * @param body Contains the heavy weight value with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/heavyWeight" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/heavyWeight")
    public ResponseEntity<Object> setHeavyWeight(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setHeavyWeight((boolean) value);
        });
    }

    /**
     * Updates the heavy weight threshold for a specific user.
     *
     * @param body Contains the heavy weight threshold with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/heavyWeightTreshold" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/heavyWeightTreshold")
    public ResponseEntity<Object> setHeavyWeightTreshold(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setHeavyWeightThreshold((int) value);
        });
    }


    /**
     * Updates the high humidity threshold for a specific user.
     *
     * @param body Contains the high humidity threshold with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/highHumidityThreshold" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/highHumidityThreshold")
    public ResponseEntity<Object> setHighHumidityThreshold(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setHighHumidityThreshold((int) value);
        });
    }

    /**
     * Updates the low humidity threshold for a specific user.
     *
     * @param body Contains the threshold value with key "lowHumidity". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/highHumidity" \
     * -H "Content-Type: application/json" \
     * -d '{"highHumidity": 1}'
     */
    @PutMapping("/setLowHumidity")
    public ResponseEntity<Object> setLowHumidity(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setLowHumidity((boolean) value);
        });
    }

    /**
     * Updates the "Do Not Disturb From" time setting for a specific user.
     *
     * @param body Contains the "Do Not Disturb From" time with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/dontDisturbFrom" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/dontDisturbFrom")
    public ResponseEntity<Object> setDontDisturbFrom(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setDontDisturbFrom((long) value);
        });
    }

    /**
     * Updates the "Do Not Disturb To" time setting for a specific user.
     *
     * @param body Contains the "Do Not Disturb To" time with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/dontDisturbTo" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/dontDisturbTo")
    public ResponseEntity<Object> setDontDisturbTo(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setDontDisturbTo((long) value);
        });
    }

    /**
     * Updates the "Do Not Disturb" setting for a specific user.
     *
     * @param body Contains the "Do Not Disturb" setting with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/dontDisturb" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/dontDisturb")
    public ResponseEntity<Object> setDontDisturb(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setDontDisturb((boolean) value);
        });
    }

    /**
     * Updates the alternate email address setting for a specific user.
     *
     * @param body Contains the alternate email with key "value". Defaults to the user's primary email if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/altMail" \
     * -H "Content-Type: application/json" \
     * -d '{"value": "alternate@example.com"}'
     */
    @PutMapping("/altMail")
    public ResponseEntity<Object> setAltMail(@RequestBody Map<String, String> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, body.getOrDefault("value", userRepository.getUserById(currentUserId).getEmail()), (Object value) -> {
            userSettings.setAltMail((String) value);
        });
    }

    /**
     * Updates the "Send Notifications" setting for a specific user.
     *
     * @param body Contains the "Send Notifications" setting with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/sendNotifications" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/sendNotifications")
    public ResponseEntity<Object> setSendNotifications(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setSendNotifications((boolean) value);
        });
    }

    /**
     * Updates the setting to use the user's login email.
     *
     * @param body Contains the setting with key "value". Defaults to 1 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/useUserLoginMail" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/useUserLoginMail")
    public ResponseEntity<Object> setUseUserLoginMail(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 1), (Object value) -> {
            userSettings.setUseUserLoginMail((boolean) value);
        });
    }

    /**
     * Updates the low battery notification setting for a specific user.
     *
     * @param body Contains the low battery setting with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/lowBattery" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 1}'
     */
    @PutMapping("/lowBattery")
    public ResponseEntity<Object> setLowBattery(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setLowBattery((boolean) value);
        });
    }

    /**
     * Updates the battery low threshold setting for a specific user.
     *
     * @param body Contains the battery low threshold with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/batteryLowThreshold" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 20}'
     */
    @PutMapping("/batteryLowThreshold")
    public ResponseEntity<Object> setBatteryLowThreshold(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
            userSettings.setBatteryLowThreshold((int) value);
        });
    }

    /**
     * Updates the low humidity threshold setting for a specific user.
     *
     * @param body Contains the low humidity threshold with key "value". Defaults to 0 if not provided.
     * @return OK if updated successfully, otherwise outputs not found.
     * @example curl -X PUT "http://localhost:8080/dashboardApi/settings/lowHumidityThreshold" \
     * -H "Content-Type: application/json" \
     * -d '{"value": 30}'
     */
    @PutMapping("/lowHumidityThreshold")
    public ResponseEntity<Object> setLowHumidityThreshold(@RequestBody Map<String, Integer> body) {
        Settings userSettings = settingsRepository.getSettingsByUserId(currentUserId);

        return saveSettingItem(currentUserId, 0 != body.getOrDefault("value", 0), (Object value) -> {
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
