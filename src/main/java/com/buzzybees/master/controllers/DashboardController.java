package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.*;
import com.buzzybees.master.beehives.actions.*;
import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.beehives.devices.DeviceRepository;
import com.buzzybees.master.beehives.devices.SensorValue;
import com.buzzybees.master.beehives.devices.SensorValueRepository;
import com.buzzybees.master.beehives.export.ExcelService;
import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.CookieAuthController;
import com.buzzybees.master.exceptions.ItemNotFoundException;
import com.buzzybees.master.exceptions.NoPermissionException;
import com.buzzybees.master.exceptions.OwnershipException;
import com.buzzybees.master.notifications.Notification;
import com.buzzybees.master.notifications.NotificationRepository;
import com.buzzybees.master.notifications.Reminder;
import com.buzzybees.master.notifications.ReminderRepository;
import com.buzzybees.master.tables.PairList;
import com.buzzybees.master.tables.Status;
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

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/dashboardApi")
public class DashboardController extends CookieAuthController {


    @Autowired
    private ExcelService excelService;

    @Autowired
    BeehiveRepository beehiveRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    SensorValueRepository sensorValueRepository;

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * @return all user's beehives based on session id.
     */
    @GetMapping("/getBeehives")
    public ApiResponse getBeehives() {
        Beehive[] beehives = beehiveRepository.getAllByUser(currentUserId);
        return new ApiResponse("beehives", beehives);
    }

    /**
     * @param date start date from which statuses will be collected.
     * @return all statuses of beehives belonging to user since specific date.
     * @throws ParseException when date format is wrong.
     */
    @GetMapping(value = "/getData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse getData(@RequestParam(value = "fromDate", defaultValue = "all") String date) throws ParseException {
        StatusRepository statusRepository = getRepo(Status.class);

        long timestamp = date.equals("all") ? 0 : dateToTimestamp(date);
        PairList<Status, SensorValue> data = statusRepository.getUserStatusesSince(currentUserId, timestamp);

        HashMap<String, BeehiveData> map = new HashMap<>();

        data.forEach((status, sensorValue) -> {
            map.putIfAbsent(status.getBeehive(), new BeehiveData());
            BeehiveData beehiveData = map.get(status.getBeehive());
            beehiveData.push(status, sensorValue);
        });

        return new ApiResponse("data", map);
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


    /**
     * @return Excel file stream with all statuses of beehives belonging to user.
     */
    @GetMapping("/downloadExcel")
    public ResponseEntity<byte[]> downloadExcel() {
        StatusRepository statusRepository = getRepo(Status.class);
        Beehive[] beehives = beehiveRepository.getAllByUser(currentUserId);

        ByteArrayInputStream in = excelService.exportToExcel(
                Arrays.stream("Ul;Stav;Hmotnost;Bateria;Teplota;Vlhkost;Posledna aktualizacia;\n".split(";")).toList(),


                Arrays.stream(beehives).map((beehive) -> {
                    List<String> list = new ArrayList<>();
                    list.add(beehive.getName());

                    try {
                        String[] array = statusRepository.getLastStatus(beehive.getToken()).toCSV().split(";");

                        list.addAll(
                                Stream.of(array).toList());
                    } catch (NullPointerException e) {
                        list.addAll(new ArrayList<>());
                    }
                    return list;
                }).toList()
        );

        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=data.xlsx");

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(in.readAllBytes());
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
     *
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


    /**
     * Updates the status of a notification based on the provided JSON data.
     *
     * The input data should contain the "id" field representing the notification to be updated.
     *
     * @param data A Map containing notification data in JSON format.
     * @return ApiResponse indicating the result:
     *         - If the notification is updated successfully, returns ApiResponse with status "OK".
     *         - Throws OwnershipException if the current user does not own the notification.
     *         - Throws ItemNotFoundException if the specified notification is not found.
     */
    @PostMapping(value = {"/updateNotification"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateNotification(@RequestBody Map<String, String> data) throws OwnershipException, ItemNotFoundException {
        System.out.println(data.toString());
        System.out.println(data.get("id"));
        System.out.println(data.keySet());
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
     *
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
            notificationRepository.delete(notification);

            if (notification.getUserId() != currentUserId) throw new OwnershipException();
        } else {
            return new ApiResponse("status", "nok");
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
     *
     * @param beehive determine which beehive to pair
     * @return status whether action was successful
     */
    @PostMapping("/newPairing")
    public ApiResponse newPairing(@RequestBody String beehive) {
        PairingManager.init(beehive, currentUserId);
        return new ApiResponse();
    }


    @DeleteMapping("/deleteBeehive/{beehiveToken}")
    public ApiResponse deleteBeehive(@PathVariable String beehiveToken) {
        try {
            Beehive beehive = beehiveRepository.getBeehiveByToken(beehiveToken);
            // Check if the beehive exists
            if (beehive == null) {
                return new ApiResponse("error", "Beehive not found");
            }



            beehiveRepository.delete(beehive);

            // Logic to delete the beehive
            // Example: beehiveRepository.deleteBeehiveByToken(beehiveToken);

            return new ApiResponse("success", "Beehive deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("error", "An error occurred while deleting the beehive");
        }
    }

    /**
     * Retrieves device configuration based on the provided beehive token.
     *
     * @param beehiveToken The token of the beehive for which device configuration is requested.
     * @return ApiResponse containing device configuration if the current user owns the beehive.
     * @throws OwnershipException if the current user does not own the specified beehive.
     */
    @GetMapping("/getDeviceConfig")
    public ApiResponse getDeviceConfig(@RequestParam("beehive") String beehiveToken) throws OwnershipException {
        Beehive beehive = beehiveRepository.getBeehiveByToken(beehiveToken);
        if(currentUserId != beehive.getUserId()) throw new OwnershipException();
        return new ApiResponse("devices", beehive.getDevices());
    }

    /**
     * saves beehive settings to database
     *
     * @return status whether action was successful
     */
    @PostMapping(value = "/saveDeviceSettings", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResponse saveDeviceSettings(@ModelAttribute Beehive beehive) throws OwnershipException, ItemNotFoundException {
        Beehive targetBeehive = beehiveRepository.getBeehiveByToken(beehive.getToken());

        if (targetBeehive == null) throw new ItemNotFoundException();
        if (targetBeehive.getUserId() != currentUserId) throw new OwnershipException();

        List<Action> actions = Actions.createConfigActions(targetBeehive, beehive);
        ActionRepository actionRepository = getRepo(Action.class);
        actionRepository.saveOrUpdateAll(actions);

        beehive.setUserId(currentUserId);
        beehive.setModel(targetBeehive.getModel());

        Actions.handleResponse(beehive, ActionType.CHANGE_BEEHIVE_CONFIG, action -> {
             if(action.getStatus() == ActionStatus.DONE) beehiveRepository.save(beehive);
        });

        DeviceRepository deviceRepository = getRepo(Device.class);
        deviceRepository.saveAll(beehive.getDevices());

        return new ApiResponse();
    }
}
