package com.buzzybees.master.controllers;

import com.buzzybees.master.beehive.StatusRepository;
import com.buzzybees.master.notifications.NotificationRepository;
import com.buzzybees.master.tables.Status;
import com.buzzybees.master.users.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@RestController
public class BeeController {

    @Autowired
    StatusRepository statusRepo;

    @GetMapping("/clk_sync")
    public long clkSync() {
        Date date = new Date();
        return date.getTime();
    }

    @PostMapping(value = {"/updateStatus"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateStatus(@RequestBody String data) {
        System.out.println(data);
        Status status = new Status(new JSONObject(data));
        statusRepo.save(status);
        return "DATA HAS BEEN SAVED";
    }
}
