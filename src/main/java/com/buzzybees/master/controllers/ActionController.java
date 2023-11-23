package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.actions.Action;
import com.buzzybees.master.beehives.actions.ActionRepository;
import com.buzzybees.master.beehives.actions.ActionType;
import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.CookieAuthController;
import com.buzzybees.master.notifications.Notification;
import com.buzzybees.master.notifications.NotificationRepository;
import com.buzzybees.master.notifications.Reminder;
import com.buzzybees.master.notifications.ReminderRepository;
import com.buzzybees.master.users.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/actions")
public class ActionController extends CookieAuthController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    NotificationRepository notificationRepository;

    /**
     * saves new reminder to database
     *
     * @param action created reminder from frontend form
     * @return created reminder
     */
    @PostMapping(value = "/newAction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse newAction(@RequestBody Action action) {
        System.out.println("Received Action: " + action.toString() + " " + action.getExecution_time());

        actionRepository.save(action);


        String title = "Nová akcia " +action.getType();
        Notification notification = new Notification(Notification.Type.WARNING,
                currentUserId,
                title,
                "Pridali ste novú akciu!");


        notificationRepository.save(notification);

        return new ApiResponse("action", action);
    }


    /**
     * Gets pending actions of specific beehive
     * Requires beehiveId
     */

    @GetMapping(value = "/getPending", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<Long, Action> getPending(@RequestParam(value = "beehiveId", defaultValue = "all") String beehiveId) {
        System.out.println("getPending was called " + beehiveId);
        Action[] output = actionRepository.getActionsByBeehiveId(beehiveId);
        Map<Long, Action> actions = new HashMap<>();
        Arrays.stream(output).toList().forEach((act -> actions.put(act.getId(), act)));
        return actions;
    }

    @GetMapping(value = "/getActionOptions", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getValidActions() {
        JSONObject output = new JSONObject();

        output.put("actions",
                ActionType.getNonSystemValues()
        );

        return output.toString();
    }

}