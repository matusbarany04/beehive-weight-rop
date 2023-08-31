package com.buzzybees.master.controllers;

import com.buzzybees.master.notifications.Notification;
import com.buzzybees.master.notifications.Notification.Type;
import com.buzzybees.master.notifications.NotificationRepository;
import com.buzzybees.master.tables.User;
import com.buzzybees.master.security.PasswordUtils;
import com.buzzybees.master.users.Mailer;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

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

    //@RequestMapping(value = "/getUser/token", method = RequestMethod.POST)
    @PostMapping(value = {"/getUser/token"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    public String autoLogin(@RequestBody Map<String, String> data) {
        User user = userRepository.getUserById(UserService.getUserIdByToken(data.get("token")));
//        System.out.println("autoLogin");
//        System.out.println(data.get("token"));
        JSONObject response = new JSONObject();
        response.put("status", user != null ? "ok" : "ERR_INVALID_TOKEN");
        if (user != null) response.put("user", user.toJSON());
        return response.toString();
    }

    @PostMapping(value = {"user/saveDashboard"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveDashboard(@RequestBody Map<String, String> data) { //TODO check validity of data
        String dashboardData = data.get("data");
        String token = data.get("token");

        JSONObject response = new JSONObject();

        User user = userRepository.getUserById(UserService.getUserIdByToken(token));
        if (user != null) {
            userRepository.saveDashboard(user.id, dashboardData);
            response.put("status", "ok");
        } else {
            response.put("status", "ERR_INVALID_TOKEN");
        }

        return response.toString();
    }


    @Deprecated
  //  @PostMapping(value = {"/login"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody Map<String, String> data) {
        String password = data.get("password");
        String mail = data.get("email");

        JSONObject response = new JSONObject();
        User user = null;

        try {
            //   String password = formData.getFirst("password");

            if (password != null) {
                String passwdHash = PasswordUtils.hashPasswd(password);

//                System.out.println(passwdHash);
                user = userRepository.checkUser(mail, passwdHash);

                if (user != null) {
                    response.put("user", user.toJSON());
                    String token = UserService.loginUser(user);
                    response.put("token", token);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.put("status", user != null ? "ok" : "ERR_INVALID_USER");
        return response.toString();
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


    @GetMapping("/user/byToken/")
    public JSONObject getUserByToken(@RequestParam("token") String token) {
        return userRepository.getUserById(UserService.getUserIdByToken(token)).toJSON();
    }

    //http://localhost:8080/user/emailExists/admin@admin.com
    @RequestMapping(value = "/user/emailExists/{email}", method = RequestMethod.GET)
    public JSONObject getUserByEmail(@PathVariable("email") String email) {
        JSONObject response = new JSONObject();
        response.put("exists", userRepository.getUserByEmail(email) != null);
        return response;
    }
   /*
     curl -X POST \
     -H "Content-Type: application/json" \
     -d  '{
        "name": "John Doe",
        "email": "johndoe@example.com",
        "password": "hardPassword123"
      }' \
      http://localhost:8080/register
    */

    /*curl -X POST -d "name=John Doe" -d "email=john@example" -d "password=hello" http://localhost:8080/register */

    @PostMapping(value = {"/register"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String register(@RequestBody Map<String, String> data) {
        String password = data.get("password");
        String email = data.get("email");
        String name = data.get("name");

        JSONObject response = new JSONObject();

        if (userRepository.getUserByEmail(email) != null) {
            response.put("status", "exists");
            return response.toString();
        }

        User person = new User();
        person.setEmail(email);
        person.setName(name);

        try {
            person.setPassword(password);
            userRepository.save(person);
            long id = userRepository.getUserByEmail(email).id;
            Mailer.sendVerification(email, id);

            response.put("status", "ok");
            return response.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            response.put("status", "failed");
        }

        return response.toString();
    }

    @PostMapping(value = {"/logout"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String logout(@RequestBody Map<String, String> data) {
        UserService.logoutUser(data.get("token"));

        JSONObject response = new JSONObject();
        response.put("status", "ok");
        return response.toString();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/testing/{id}")
    public String retrieve(@PathVariable String id) {
        return "ok " + id;
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

    @PostMapping(value = "/user/updateSettings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateSettings(@RequestBody Map<String, String> data) {
        long userId = UserService.getUserIdByToken(data.get("token"));
        User user = userRepository.getUserById(userId);
        user.updateSettings(data.get("settings"));

        JSONObject response = new JSONObject();
        response.put("status", "ok");
        return response.toString();
    }

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

    public String getEmailByUserId(long id) {
        return userRepository.getEmail(id);
    }
}
