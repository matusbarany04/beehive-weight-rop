package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.PairingManager;
import com.buzzybees.master.beehives.StatusRepository;
import com.buzzybees.master.notifications.Notification;
import com.buzzybees.master.notifications.NotificationRepository;
import com.buzzybees.master.notifications.Reminder;
import com.buzzybees.master.notifications.ReminderRepository;
import com.buzzybees.master.tables.Status;
import com.buzzybees.master.tables.User;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/dashboardApi")
public class DashboardController {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    private StatusRepository statusRepository;
    private BeehiveRepository beehiveRepository;
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private ReminderRepository reminderRepository;

    private long currentUserId;

    @Autowired
    public void initRepos(StatusRepository statuses, NotificationRepository notifications, BeehiveRepository beehives, UserRepository users, ReminderRepository reminders) {
        this.statusRepository = statuses;
        this.notificationRepository = notifications;
        this.beehiveRepository = beehives;
        this.userRepository = users;
        this.reminderRepository = reminders;
    }

    @ModelAttribute("dashboard")
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

    @GetMapping("/getBeehives")
    public String getBeehives() {
        JSONObject response = new JSONObject();
        String status = "ERR_NO_PERMISSION";

        if (currentUserId > 0) {
            JSONArray array = new JSONArray();
            Beehive[] beehives = beehiveRepository.getAllByUser(currentUserId);
            for (Beehive beehive : beehives) array.put(beehive.toJSON());

            response.put("beehives", array);
            status = "ok";
        }

        response.put("status", status);
        return response.toString();
    }

    @GetMapping(value = "/getData", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getData(@RequestParam(value = "fromDate", defaultValue = "all") String date) {

        JSONObject response = new JSONObject();
        String responseStatus = "ERR_NO_PERMISSION";
        try {
            System.out.println(date);
            long timestamp = date.equals("all") ? 0 : dateToTimestamp(date);

            if (currentUserId > 0) {
                JSONObject jsonObject = new JSONObject();
                String[] beehives = beehiveRepository.getBeehiveTokens(currentUserId);
                Status[] statuses = statusRepository.getAllStatusesSince(beehives, timestamp);

                for (String token : beehives) {
                    JSONObject beehive = new JSONObject();
                    beehive.put("timestamp", new JSONArray());
                    beehive.put("battery", new JSONArray());
                    beehive.put("weight", new JSONArray());
                    beehive.put("humidity", new JSONArray());
                    beehive.put("temperature", new JSONArray());
                    jsonObject.put(token, beehive);
                }

                for (Status status : statuses) {
                    JSONObject beehive = jsonObject.getJSONObject(status.getBeehive());
                    beehive.getJSONArray("timestamp").put(status.getTimestamp());
                    beehive.getJSONArray("battery").put(status.getBattery());
                    beehive.getJSONArray("weight").put(status.getWeight());
                    beehive.getJSONArray("humidity").put(status.getHumidity());
                    beehive.getJSONArray("temperature").put(status.getTemperature());
                    beehive.put("currentStatus", status.getStatus());
                }

                responseStatus = "ok";

                response.put("data", jsonObject);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            responseStatus = "ERR_WRONG_DATE";
        }

        response.put("status", responseStatus);
        return response.toString();
    }

    @GetMapping(value = "/downloadCSV")
    public ResponseEntity<Resource> downloadCSV() {
        if (currentUserId > 0) {
            Beehive[] beehives = beehiveRepository.getAllByUser(currentUserId);
            String[] tokens = beehiveRepository.getBeehiveTokens(currentUserId);
            Status[] lastStatuses = statusRepository.getLastStatuses(Arrays.asList(tokens));

            StringBuilder csv = new StringBuilder("Ul;Stav;Hmotnost;Bateria;Teplota;Vlhkost;Posledna aktualizacia;\n");
            for(Status status : lastStatuses) {
                Beehive beehive = Beehive.findByToken(beehives, status.getBeehive());
                assert beehive != null;
                csv.append(beehive.getName()).append(";").append(status.toCSV()).append("\n");
            }

            ByteArrayResource resource = new ByteArrayResource(csv.toString().getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ule.csv");
            headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }
        else return null;
    }

    @GetMapping(value = "/getAllBeehiveData", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllBeehiveData(@RequestParam("token") String beehiveId) {
        JSONObject response = new JSONObject();
        String status = "ERR_NO_PERMISSION";
        System.out.println("getAllBeehiveData " + beehiveId);
        Beehive beehive = beehiveRepository.getBeehiveByToken(beehiveId);
        if (beehive.getUserId() != currentUserId) status = "ERR_BEEHIVE_NOT_PERMITTED";
        else if (currentUserId > 0) {
            Status[] statuses = statusRepository.getAllBeehiveStatuses(beehiveId);
            response.put("statuses", Beehive.mergeStatuses(statuses));
            response.put("beehive", beehive.toJSON());
            status = "ok";
        }

        response.put("status", status);
        return response.toString();
    }

    @GetMapping("/getNotifications")
    public String getNotifications() {
        JSONObject response = new JSONObject();
        response.put("status", currentUserId > 0 ? "ok" : "ERR_NO_PERMISSION");

        if (currentUserId > 0) {
            JSONArray jsonArray = new JSONArray();
            Notification[] notifications = notificationRepository.getUserNotifications(currentUserId);

            for (Notification notification : notifications) {
                jsonArray.put(notification.toJSON());
            }

            response.put("notifications", jsonArray);
        }

        return response.toString();
    }

    @GetMapping("/getReminders")
    public String getReminders() {
        JSONObject response = new JSONObject();
        response.put("status", currentUserId > 0 ? "ok" : "ERR_NO_PERMISSION");

        if (currentUserId > 0) {
            JSONArray jsonArray = new JSONArray();
            Reminder[] notifications = reminderRepository.getUserReminders(currentUserId);

            for (Reminder reminder : notifications) {
                jsonArray.put(reminder.toJSON());
            }

            response.put("reminders", jsonArray);
        }

        return response.toString();
    }

    @PostMapping(value = "/newReminder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String newReminder(@ModelAttribute Reminder reminder) {
        System.out.println(reminder.toString());
        JSONObject output = new JSONObject();

        if (currentUserId > 0) {
            reminder.setUserId(currentUserId);
            reminderRepository.save(reminder);
            output.put("reminder", reminder.toJSON());
        }

        output.put("status", currentUserId > 0 ? "ok" : "ERR_NO_PERMISSION");
        return output.toString();
    }

    @PostMapping(value = {"/updateNotification"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateNotification(@RequestBody Map<String, String> data) { //TODO check validity of data
        System.out.println(data);
        long notificationId = Long.parseLong(data.get("id"));
        JSONObject response = new JSONObject();
        Optional<Notification> dbNotification = notificationRepository.findById(notificationId);
        String status = "ERR_NOT_FOUND";

        if (dbNotification.isPresent()) {
            Notification notification = dbNotification.get();
            System.out.println(notification.getUserId());
            System.out.println(currentUserId + " current");
            boolean permitted = notification.getUserId() == currentUserId;

            if (permitted) {
                notification.setSeen(true);
                notificationRepository.save(notification);
            }
            status = permitted ? "ok" : "ERR_NOT_PERMITTED";
        }
        response.put("status", status);

        return response.toString();
    }

    @DeleteMapping("/deleteNotification")
    public String deleteNotification(@RequestParam("id") long notificationId) {
        JSONObject response = new JSONObject();
        Optional<Notification> dbNotification = notificationRepository.findById(notificationId);
        String status = "ERR_NOT_FOUND";

        if (dbNotification.isPresent()) {
            Notification notification = dbNotification.get();
            boolean permitted = notification.getUserId() == currentUserId;
            if (permitted) notificationRepository.delete(notification);
            status = permitted ? "ok" : "ERR_NOT_PERMITTED";
        }

        response.put("status", status);

        return response.toString();
    }


    //TODO zmenit stav uzporonenia, seen -> 1, moznost vymazat upzoornenie


    @PostMapping(value = "/createNewBeehive", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createNewBeehive(@RequestBody Map<String, String> data) {
        Beehive beehive = new Beehive();
        beehive.setToken(data.get("token"));
        beehive.setId(currentUserId);
        beehive.setName(data.get("name"));
        beehive.setLocation(data.get("location"));
        beehive.setInterval(Integer.parseInt(data.get("interval")));
        beehiveRepository.save(beehive);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "ok");
        return jsonObject.toString();
    }


    private long dateToTimestamp(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date datetime = format.parse(date);
        return datetime.getTime();
    }

    @PostMapping(value = "/renameBeehive")
    public String renameBeehive(@RequestBody Map<String, String> data) {
        System.out.println("renaming beehive !!!");
        JSONObject response = new JSONObject();
        String status = "ERR_NO_PERMISSION";
        Beehive beehive = beehiveRepository.getBeehiveByToken(data.get("token"));

        if (beehive.getUserId() != currentUserId) status = "ERR_BEEHIVE_NOT_PERMITTED";
        else if (currentUserId > 0) {
            beehive.setName(data.get("name"));
            beehiveRepository.save(beehive);
            status = "ok";
        }

        response.put("status", status);
        return response.toString();
    }

    @PostMapping(value = "/changeInterval")
    public String changeInterval(@RequestBody Map<String, String> data) {
        JSONObject response = new JSONObject();
        String status = "ERR_NO_PERMISSION";
        Beehive beehive = beehiveRepository.getBeehiveByToken(data.get("token"));

        if (beehive.getUserId() != currentUserId) status = "ERR_BEEHIVE_NOT_PERMITTED";
        else if (currentUserId > 0) {
            beehive.setInterval(Integer.parseInt(data.get("interval")));
            beehiveRepository.save(beehive);
            status = "ok";
        }

        response.put("status", status);
        return response.toString();
    }

//    @PostMapping("/updateSettings")
//    public String updateSettings(@RequestBody Map<String, String> data) {
//        JSONObject response = new JSONObject();
//
//        if (currentUserId > 0) {
//            User user = userRepository.getUserById(currentUserId);
//            user.updateSettings(data.get("settings"));
//            userRepository.save(user);
//            response.put("status", "ok");
//
//        } else response.put("status", "ERR_NO_PERMISSION");
//
//        return response.toString();
//    }
//
//    @GetMapping("/getSettings")
//    public String getSettings() {
//        JSONObject response = new JSONObject();
//
//        if (currentUserId > 0) {
//            User user = userRepository.getUserById(currentUserId);
//            response.put("settings", user.getSettings());
//        }
//
//        response.put("status", currentUserId > 0 ? "ok" : "ERR_NO_PERMISSION");
//
//        return response.toString();
//    }

    @GetMapping("/checkPairingStatus")
    public String checkStatus(@RequestParam("token") String beehive) {
        JSONObject json = new JSONObject();

        String status;
        if(PairingManager.isExpired(beehive)) status = "TIMEOUT";
        else status = PairingManager.isPaired(beehive) ? "PAIRED" : "PENDING";

        json.put("status", status);
        return json.toString();
    }

    @PostMapping("/newPairing")
    public String newPairing(@RequestBody String beehive) {
        JSONObject json = new JSONObject();
        if(currentUserId > 0) {
            PairingManager.init(beehive, currentUserId);
            json.put("status", "ok");

        } else json.put("status", "ERR_NO_PERMISSION");

        return json.toString();
    }
}
