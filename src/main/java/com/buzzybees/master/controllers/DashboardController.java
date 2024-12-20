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
import com.buzzybees.master.utils.json.JSONForm;
import com.buzzybees.master.websockets.EspSocketHandler;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    @GetMapping("/getAllNotifications")
    public ApiResponse getNotifications() {
        NotificationRepository notificationRepository = getRepo(Notification.class);
        Notification[] notifications = notificationRepository.getUserNotifications(currentUserId);

        return new ApiResponse("notifications", notifications);
    }


    /**
     * Get user's notifications with pagination.
     *
     * @param page     Page number (starting from 0)
     * @param pageSize Number of notifications per page
     * @return ApiResponse containing paginated user's notifications
     */
    @GetMapping("/getNotifications")
    public ApiResponse getNotifications(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int pageSize) {
        NotificationRepository notificationRepository = getRepo(Notification.class);

        Pageable pageable = PageRequest.of(page, pageSize);
        Notification[] response = notificationRepository.getUserNotificationsWithPagination(currentUserId, pageable).toList().toArray(new Notification[0]);

        return new ApiResponse("notifications",
                response
        );
    }

    /**
     * Get the total number of pages for user's notifications.
     *
     * @param pageSize Number of notifications per page
     * @return ApiResponse containing the total number of pages
     */
    @GetMapping("/getNotificationPageCount")
    public ApiResponse getNotificationPageCount(@RequestParam(defaultValue = "10") int pageSize) {
        NotificationRepository notificationRepository = getRepo(Notification.class);

        long totalNotifications = notificationRepository.countUserNotifications(currentUserId);

        int totalPages = (int) Math.ceil((double) totalNotifications / pageSize);

        return new ApiResponse("pageCount", totalPages);
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
        if (reminder.isUpcoming()) reminder.activate();
        repository.save(reminder);


        return new ApiResponse("reminder", reminder);
    }

    /**
     * deletes  reminder from database
     *
     * @param body with id -> reminderId
     * @return ApiResponse status
     */
    @PostMapping(value = "/deleteReminder", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse newReminder(@RequestBody HashMap<String, String> body) {
        int id = Integer.parseInt(body.get("id"));
        ReminderRepository repository = getRepo(Reminder.class);

        if (repository.isReminderUsers(currentUserId, id)) {

            repository.deleteByReminderId(id);

            return ApiResponse.OK();
        } else {
            return ApiResponse.ERROR("message", "You don't have any notification with that id");
        }
    }

    /**
     * Updates the status of a notification based on the provided JSON data.
     * <p>
     * The input data should contain the "id" field representing the notification to be updated.
     *
     * @param data A Map containing notification data in JSON format.
     * @return ApiResponse indicating the result:
     * - If the notification is updated successfully, returns ApiResponse with status "OK".
     * - Throws OwnershipException if the current user does not own the notification.
     * - Throws ItemNotFoundException if the specified notification is not found.
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
        System.out.println("pairing");
        PairingManager.init(beehive, currentUserId);
        return new ApiResponse();
    }

    @DeleteMapping("/deleteAllBeehiveData")
    public ApiResponse deleteData(@RequestParam String token) {
        statusRepository.deleteByToken(token);
        return ApiResponse.OK();
    }


    @DeleteMapping("/deleteBeehive/{beehiveToken}")
    public ApiResponse deleteBeehive(@PathVariable String beehiveToken) throws ItemNotFoundException {
        Beehive beehive = beehiveRepository.getBeehiveByToken(beehiveToken);
        // Check if the beehive exists
        if (beehive == null) {
            throw new ItemNotFoundException();
        }

        deleteData(beehiveToken);

        beehiveRepository.delete(beehive);

        Action action = new Action(ActionType.UNPAIR, "{}", beehiveToken);
        ActionRepository actionRepository = getRepo(Action.class);
        actionRepository.saveOrUpdate(action);
        EspSocketHandler.sendFlashActionToBeehive(action);

        return ApiResponse.OK();
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
        if (currentUserId != beehive.getUserId()) throw new OwnershipException();
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
        for (Action action : actions) EspSocketHandler.sendFlashActionToBeehive(action);

        beehive.setUserId(currentUserId);
        beehive.setModel(targetBeehive.getModel());

        Beehive backup = beehive.createBackup(targetBeehive);
        beehiveRepository.save(backup);
        Actions.handleResponse(beehive, ActionType.CHANGE_BEEHIVE_CONFIG, action -> {
            Notification.Type type = action.getStatus() == ActionStatus.DONE ? Notification.Type.INFO : Notification.Type.PROBLEM;
            String message = action.getStatus() == ActionStatus.DONE ? "Úspešne aktualizovaná" : "Vyskytla sa chyba: " + action.getStatus();
            Notification notification = new Notification(type, currentUserId, "Zmena konfigurácie zariadenia", message);
            notification.sendToUser();
            beehiveRepository.save(beehive);
        });

        DeviceRepository deviceRepository = getRepo(Device.class);
        deviceRepository.saveAll(beehive.getDevices());

        if (actions.size() > 0) return new ApiResponse("actionId", actions.get(0).getId());
        else return new ApiResponse();
    }
}
