package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.Action;
import com.buzzybees.master.beehives.action.ActionRepository;
import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.CookieAuthController;
import com.buzzybees.master.notifications.Reminder;
import com.buzzybees.master.notifications.ReminderRepository;
import com.buzzybees.master.tables.User;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.settings.SettingsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.DataInput;
import java.io.IOException;
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

    /**
     * saves new reminder to database
     *
     * @param action created reminder from frontend form
     * @return created reminder
     */
    @PostMapping(value = "/newAction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse newAction(@RequestBody Action action) {
        System.out.println("Received Action: " + action.toString());

        actionRepository.save(action);

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

}