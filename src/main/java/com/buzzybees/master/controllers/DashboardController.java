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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/dashboardApi")
public class DashboardController extends CookieAuthController {

    @Autowired
    BeehiveRepository beehiveRepository;

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * @return all user's beehives based on session id.
     */
    @GetMapping("/getBeehives")
    public ApiResponse getBeehives() {
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

    /**
     * @param date start date from which statuses will be collected.
     * @return all statuses of beehives belonging to user since specific date.
     * @throws ParseException when date format is wrong.
     */
    @GetMapping(value = "/getData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse getData(@RequestParam(value = "fromDate", defaultValue = "all") String date) throws ParseException {
        StatusRepository statusRepository = getRepo(Status.class);

        long timestamp = date.equals("all") ? 0 : dateToTimestamp(date);
        Status[] statuses = statusRepository.getUserStatusesSince(currentUserId, timestamp);

        HashMap<String, HashMap<String, Object>> data = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for(Status status : statuses) {
            HashMap<String, Object> beehive = data.getOrDefault(status.getBeehive(), new HashMap<>());
            Map<String, Object> map = objectMapper.convertValue(status, new TypeReference<>(){});
            map.forEach((key, value) -> {
                List<Object> list = new ArrayList<>((Collection<?>) beehive.getOrDefault(key, new ArrayList<>()));
                list.add(value);
                beehive.put(key, list);
            });

            beehive.put("currentStatus", status.getStatus());
            data.put(status.getBeehive(), beehive);
        }

        return new ApiResponse("data", data);
    }


    /**
     * @param date date to parse.
     * @return timestamp of a date.
     * @throws ParseException when date format is wrong.
     */
    private long dateToTimestamp(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date datetime = format.parse(date);
        return datetime.getTime();
    }

    /**
     * @return CSV file with all statuses of beehives belonging to user.
     */
    @GetMapping(value = "/downloadCSV")
    public ResponseEntity<Resource> downloadCSV() {
        StatusRepository statusRepository = getRepo(Status.class);

        List<Object[]> data = statusRepository.csvSelect(currentUserId);
        StringBuilder csv = new StringBuilder("Ul;Stav;Hmotnost;Bateria;Teplota;Vlhkost;Posledna aktualizacia;\n");

        for(Object[] row : data) {
            Status status = (Status) row[0];
            Beehive beehive = (Beehive) row[1];

            csv.append(beehive.getName()).append(";").append(status.toCSV()).append("\n");
        }

        ByteArrayResource resource = new ByteArrayResource(csv.toString().getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ule.csv");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /**
     * @return user's notifications
     */
    @GetMapping("/getNotifications")
    public ApiResponse getNotifications() {
        NotificationRepository notificationRepository = getRepo(Notification.class);
        Notification[] notifications = notificationRepository.getUserNotifications(currentUserId);

        return new ApiResponse("notifications", notifications);
    }

    /**
     * @return user's reminders
     */
    @GetMapping("/getReminders")
    public ApiResponse getReminders() {
        ReminderRepository reminderRepository = getRepo(Reminder.class);
        Reminder[] reminders = reminderRepository.getUserReminders(currentUserId);

        return new ApiResponse("reminders", reminders);
    }

    /**
     * saves new reminder to database
     * @param reminder created reminder from frontend form
     * @return created reminder
     */
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

    /**
     * delete specific notification
     * @param notificationId determine which notification should be deleted
     * @return status whether action was successful
     * @throws OwnershipException when notification does not belong to user
     */
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

    /**
     * @param beehive determine which beehive to watch
     * @return status whether beehive has been paired or not
     */
    @GetMapping("/checkPairingStatus")
    public ApiResponse checkStatus(@RequestParam("token") String beehive) {
        ApiResponse apiResponse = new ApiResponse();

        if (PairingManager.isExpired(beehive)) apiResponse.setStatus("TIMEOUT");
        else apiResponse.setStatus(PairingManager.isPaired(beehive) ? "PAIRED" : "PENDING");

        return apiResponse;
    }

    /**
     * initialize new pairing (beehive with user)
     * @param beehive determine which beehive to pair
     * @return status whether action was successful
     */
    @PostMapping("/newPairing")
    public ApiResponse newPairing(@RequestBody String beehive) {
        PairingManager.init(beehive, currentUserId);
        return new ApiResponse();
    }

    /**
     * saves beehive settings to database
     * @param formData device settings from frontend form
     * @return status whether action was successful
     */
    @PostMapping(value = "/saveDeviceSettings", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResponse saveDeviceSettings(@RequestBody MultiValueMap<String, String> formData) {
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
