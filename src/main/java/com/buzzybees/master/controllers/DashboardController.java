package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.PairingManager;
import com.buzzybees.master.beehives.StatusRepository;
import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.beehives.devices.DeviceRepository;
import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.CookieAuthController;
import com.buzzybees.master.exceptions.ItemNotFoundException;
import com.buzzybees.master.exceptions.OwnershipException;
import com.buzzybees.master.notifications.Notification;
import com.buzzybees.master.notifications.NotificationRepository;
import com.buzzybees.master.notifications.Reminder;
import com.buzzybees.master.notifications.ReminderRepository;
import com.buzzybees.master.tables.Status;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/dashboardApi")
public class DashboardController extends CookieAuthController {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @GetMapping("/getBeehives")
    public ApiResponse getBeehives() {

        BeehiveRepository beehiveRepository = getRepo(Beehive.class);
        Beehive[] beehives = beehiveRepository.getAllByUser(currentUserId);

        return new ApiResponse("beehives", beehives);
    }
/*
    @GetMapping(value = "/getData", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getData(@RequestParam(value = "fromDate", defaultValue = "all") String date) throws ParseException, JsonProcessingException {
        BeehiveRepository beehiveRepository = getRepo(Beehive.class);
        StatusRepository statusRepository = getRepo(Status.class);

            long timestamp = date.equals("all") ? 0 : dateToTimestamp(date);

                JSONObject jsonObject = new JSONObject();
                String[] tokens = beehiveRepository.getBeehiveTokens(currentUserId);
                Status[] statuses = statusRepository.getAllStatusesSince(tokens, timestamp);

                for (String token : tokens) {
                    JSONObject beehive = new JSONObject();
                    jsonObject.put(token, beehive);
                }

                for (Status status : statuses) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JSONObject json = new JSONObject(objectMapper.writeValueAsString(status));

                    for(String key : json.keySet()) {
                        JSONObject beehive = jsonObject.getJSONObject(status.getBeehive());
                        beehive.put("currentStatus", status.getStatus());
                        JSONArray array = Optional.ofNullable(beehive.optJSONArray(key)).orElse(new JSONArray());
                        array.put(json.get(key));
                        beehive.put(key + 's', array);
                    }
                }

        return ApiResponse.json("data", jsonObject);
    }*/

    @GetMapping(value = "/getData", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getData(@RequestParam(value = "fromDate", defaultValue = "all") String date) throws ParseException {
        BeehiveRepository beehiveRepository = getRepo(Beehive.class);
        StatusRepository statusRepository = getRepo(Status.class);

        long timestamp = date.equals("all") ? 0 : dateToTimestamp(date);

        JSONObject response = new JSONObject();
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

        response.put("data", jsonObject);
        response.put("status", "ok");

        return response.toString();
    }


    private long dateToTimestamp(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date datetime = format.parse(date);
        return datetime.getTime();
    }

    @GetMapping(value = "/downloadCSV")
    public ResponseEntity<Resource> downloadCSV() {
        BeehiveRepository beehiveRepository = getRepo(Beehive.class);
        StatusRepository statusRepository = getRepo(Status.class);

        Beehive[] beehives = beehiveRepository.getAllByUser(currentUserId);
        String[] tokens = beehiveRepository.getBeehiveTokens(currentUserId);
        Status[] lastStatuses = statusRepository.getLastStatuses(Arrays.asList(tokens));

        StringBuilder csv = new StringBuilder("Ul;Stav;Hmotnost;Bateria;Teplota;Vlhkost;Posledna aktualizacia;\n");
        for (Status status : lastStatuses) {
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

    @GetMapping("/getNotifications")
    public ApiResponse getNotifications() {
        ApiResponse response = new ApiResponse();

        NotificationRepository notificationRepository = getRepo(Notification.class);
        Notification[] notifications = notificationRepository.getUserNotifications(currentUserId);
        response.putObject("notifications", notifications);

        return response;
    }

    @GetMapping("/getReminders")
    public ApiResponse getReminders() {
        ReminderRepository reminderRepository = getRepo(Reminder.class);
        Reminder[] reminders = reminderRepository.getUserReminders(currentUserId);

        return new ApiResponse("reminders", reminders);
    }

    @PostMapping(value = "/newReminder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse newReminder(@ModelAttribute Reminder reminder) {
        reminder.setUserId(currentUserId);
        ReminderRepository repository = getRepo(Reminder.class);
        repository.save(reminder);

        return new ApiResponse("reminder", reminder);
    }

    @PostMapping(value = {"/updateNotification"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateNotification(@RequestBody Map<String, String> data) throws OwnershipException, ItemNotFoundException {
        long notificationId = Long.parseLong(data.get("id"));

        NotificationRepository notificationRepository = getRepo(Notification.class);
        Optional<Notification> dbNotification = notificationRepository.findById(notificationId);

        if (dbNotification.isPresent()) {
            Notification notification = dbNotification.get();
            if (notification.getUserId() != currentUserId) throw new OwnershipException();

            notification.setSeen(true);
            notificationRepository.save(notification);

        } else throw new ItemNotFoundException();

        return new ApiResponse();
    }

    @DeleteMapping("/deleteNotification")
    public ApiResponse deleteNotification(@RequestParam("id") long notificationId) throws OwnershipException {
        NotificationRepository notificationRepository = getRepo(Notification.class);
        Optional<Notification> dbNotification = notificationRepository.findById(notificationId);

        if (dbNotification.isPresent()) {
            Notification notification = dbNotification.get();
            if (notification.getUserId() != currentUserId) throw new OwnershipException();
        }

        return new ApiResponse();
    }


    @GetMapping("/checkPairingStatus")
    public String checkStatus(@RequestParam("token") String beehive) {
        JSONObject json = new JSONObject();

        String status;
        if (PairingManager.isExpired(beehive)) status = "TIMEOUT";
        else status = PairingManager.isPaired(beehive) ? "PAIRED" : "PENDING";

        json.put("status", status);
        return json.toString();
    }

    @PostMapping("/newPairing")
    public String newPairing(@RequestBody String beehive) {
        JSONObject json = new JSONObject();
        if (currentUserId > 0) {
            PairingManager.init(beehive, currentUserId);
            json.put("status", "ok");

        } else json.put("status", "ERR_NO_PERMISSION");

        return json.toString();
    }

    @PostMapping(value = "/saveDeviceSettings", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResponse saveDeviceSettings(@RequestBody MultiValueMap<String, String> formData) {
        BeehiveRepository beehiveRepository = getRepo(Beehive.class);
        DeviceRepository deviceRepository = getRepo(Device.class);

        Beehive beehive = beehiveRepository.getBeehiveByToken(formData.getFirst("beehive"));
        beehive.setName(formData.getFirst("name"));

        String interval = formData.getFirst("interval");
        if (interval != null) beehive.setInterval(Integer.parseInt(interval));

        beehive.setLocation(formData.getFirst("location"));

        String mode = Objects.requireNonNull(formData.getFirst("connectionMode"));
        beehive.setConnectionMode(Integer.parseInt(mode));

        JSONObject sensors = new JSONObject(formData.getFirst("sensors"));
        for (String port : sensors.keySet()) {
            Device device = Device.fromJSON(sensors.getJSONObject(port), port);
            device.setBeehive(beehive);
            deviceRepository.save(device);
        }

        beehiveRepository.save(beehive);


        return new ApiResponse();
    }
}
