package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.actions.Action;
import com.buzzybees.master.beehives.actions.ActionRepository;
import com.buzzybees.master.beehives.actions.ActionStatus;
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

import java.util.*;

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
        System.out.println("Received Action: " + action.toString() + " " + action.getExecutionTime());

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
    public Map<Long, Map<String,Object>> getPending(@RequestParam(value = "beehiveId", defaultValue = "all") String beehiveId) {
        System.out.println("getPending was called " + beehiveId);
        Action[] output = actionRepository.getPendingActionsByBeehiveId(beehiveId);
        Map<Long, Map<String, Object>> actions = new HashMap<>();

        Arrays.stream(output).toList().forEach((act -> actions.put(act.getId(), act.jsonifyForFrontend().toMap() )));

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


    /**
     * Deletes an existing reminder from the database.
     *
     * @param actionId ID of the action to be deleted
     * @return ApiResponse indicating the result of the deletion
     */
    @DeleteMapping(value = "/deleteAction/{actionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse deleteAction(@PathVariable Long actionId) {
        // Check if the action exists
        Optional<Action> existingAction = actionRepository.findById(actionId);

        if (existingAction.isPresent()) {
            Action toBeDeletedAction = actionRepository.getActionById(actionId);

            if(toBeDeletedAction.getStatus() == ActionStatus.PENDING){
                // Delete the action
                actionRepository.deleteById(actionId);
                // Create a notification for the deletion
                String title = "Akcia vymazaná " + existingAction.get().getType();
                Notification notification = new Notification(Notification.Type.INFO,
                        currentUserId,
                        title,
                        "Vymazali ste existujúcu akciu!");

                notificationRepository.save(notification);

                return new ApiResponse("success", "Action deleted successfully");

            }else {
                return new ApiResponse("error", "Action status is not pending!");
            }


        } else {
            return new ApiResponse("error", "Action not found with ID: " + actionId);
        }
    }



}