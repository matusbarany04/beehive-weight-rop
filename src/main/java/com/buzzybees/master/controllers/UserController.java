package com.buzzybees.master.controllers;

import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.CookieAuthController;
import com.buzzybees.master.language.Language;
import com.buzzybees.master.notifications.Notification;
import com.buzzybees.master.notifications.Notification.Type;
import com.buzzybees.master.notifications.NotificationRepository;
import com.buzzybees.master.tables.User;
import com.buzzybees.master.users.Message;
import org.hibernate.query.results.complete.ModelPartReference;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.buzzybees.master.users.Mailer;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController extends CookieAuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;


    // toto robi pekny json, skus takto davat api
    @GetMapping("/getUsers")
    public Map<Long, User> getUsers() {
        Map<Long, User> users = new HashMap<>();
        userRepository.findAll().forEach((user -> users.put(user.id, user)));
        return users;
    }

    @GetMapping("/byToken")
    public String getUserByToken(@CookieValue(name = AuthController.SSID, defaultValue = "") String token) {
        User user = userRepository.getUserById(UserService.getUserIdByToken(token));

        JSONObject response = new JSONObject();
        response.put("status", user != null ? "ok" : "ERR_INVALID_TOKEN");
        if (user != null) response.put("user", user.toJSON());
        return response.toString();
    }

    @CrossOrigin
    @GetMapping(value = {"/verify"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String verifyEmail(@RequestParam("key") String secureKey) {
        long id = Mailer.getUserId(secureKey);
        if (id > 0) {
            userRepository.verifyUser(id);
            Mailer.invalidateSecureKey(id);
        }

        JSONObject response = new JSONObject();
        response.put("status", id > 0 ? "ok" : "ERR_INVALID_USER");
        response.put("userId", id);
        return response.toString();
    }

//    @PostMapping(value = "/updateSettings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public String updateSettings(@RequestBody Map<String, String> data) {
//
//        if (currentUserId > 0) {
//            User user = userRepository.getUserById(currentUserId);
//            user.updateSettings(data.get("settings"));
//
//            JSONObject response = new JSONObject();
//            response.put("status", "ok");
//            return response.toString();
//        }
//        JSONObject error = new JSONObject();
//        error.put("status", "error");
//        return error.toString();
//    }

    @GetMapping("/sendMail")
    public String send() {
        Mailer.sendVerification("mbelej100@gmail.com", 1152);
        return "OK";
    }

    @GetMapping("/createNotification")
    public String createNotification() {
        String title = "Slabá batéria v úli číslo 1";
        Notification notification = new Notification(Type.WARNING, 1352, title, "LOW_BATTERY", 26);
        notification.sendByMail();
        //notificationRepository.save(notification);
        return "OK";
    }

    @Deprecated
    @PostMapping(value = {"/getUser/token"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String autoLogin(@RequestBody Map<String, String> data) {

        if (currentUserId > 0) {
            User user = userRepository.getUserById(currentUserId);
            JSONObject response = new JSONObject();
            response.put("status", user != null ? "ok" : "ERR_INVALID_TOKEN");
            if (user != null) response.put("user", user.toJSON());
            return response.toString();
        }

        JSONObject error = new JSONObject();
        error.put("status", "ERR_INVALID_TOKEN");
        return error.toString();
    }

    @PostMapping(value = {"/saveDashboard"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveDashboard(@RequestBody Map<String, String> data) {

        String dashboardData = data.get("data");

        JSONObject response = new JSONObject();

        if (currentUserId > 0) {
            User user = userRepository.getUserById(currentUserId);
            if (user != null) {
                userRepository.saveDashboard(user.id, dashboardData);
                response.put("status", "ok");
            } else {
                response.put("status", "ERR_INVALID_USER_ID");
            }
        } else {
            response.put("status", "ERR_INVALID_TOKEN");
        }

        return response.toString();
    }

    @PostMapping(value = {"/change/username"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateUserName(@RequestBody Map<String, String> updateRequest) {
        String newName = updateRequest.get("name");


        if (currentUserId > 0) {
            User user = userRepository.getUserById(currentUserId);
            if (user  == null) {

                return "User not found";
            }

            user.setName(newName);
            userRepository.save(user);

            return "User name updated successfully";

        }
        return "Not logged in";
    }



    @PostMapping(value = {"/change/language"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateUserLanguage(@RequestBody Map<String, String> updateRequest) {
        System.out.println(updateRequest.keySet().toString());
        String newLanguage = updateRequest.get("language");

        if (currentUserId > 0) {
            User user = userRepository.getUserById(currentUserId);
            if (user == null) {
                return new ApiResponse("error", "User not found");
            }

            // Assuming you have a 'language' property in your User entity
            user.setLanguage(Language.valueOf( newLanguage.toUpperCase()));

            userRepository.save(user);

            return new ApiResponse("ok","User language updated successfully");
        }

        return new ApiResponse("error", "Not logged in");
    }


    @Deprecated
    @GetMapping("/getToken")
    public String getToken() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    @Deprecated
    @GetMapping("/addUser")
    public JSONObject addUser() {
        User user = new User();
        user.setName("jozko@example.com");

        userRepository.save(user);
        return user.toJSON();
    }

    @Deprecated
    @RequestMapping(value = "/isTokenValid", method = RequestMethod.POST)
    @ResponseBody
    public String isTokenValid(@RequestParam("token") String token) {
        return userRepository.getUserById(UserService.getUserIdByToken(token)) != null ? "ok" : "invalid";
    }


    @RequestMapping(method = RequestMethod.GET, path = "/testing/{id}")
    public String retrieve(@PathVariable String id) {
        return "ok " + id;
    }

    public String getEmailByUserId(long id) {
        return userRepository.getEmail(id);
    }
/*
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public Message send(final Message message) throws Exception {
        return message;
    }

    // Mapped as /app/private
    @MessageMapping("/private")
    public void sendToSpecificUser(@Payload Message message) {
        System.out.println(message);
        //   simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
    }

*/
    /**
     * Sends a test message to a specific user using WebSocket.
     * The message is sent to the user's unique destination based on their token.
     */
 /*   @GetMapping("/messageTest")
    public void messageTest() {
        String token = UserService.getTokenByUserId(1);
        simpMessagingTemplate.convertAndSend("/specific/" + token, "message test");
        System.out.println(simpMessagingTemplate.getMessageChannel());
    }
*/



    /**
     * Sends a test message to all subscribers by publishing it to the "/all/messages" destination.
     *
     * @return A Message object containing test information.
     * @throws Exception if there is an issue during message publication.
     */
    @GetMapping("/sendMessage")
    @SendTo("/all/messages")
    public Message test() throws Exception {
        return new Message("test", "user");
    }
}
