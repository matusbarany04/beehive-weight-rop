package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.StatusRepository;
import com.buzzybees.master.tables.Status;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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

    @GetMapping("/testStatuses")
    public String test() {
        String[] beehives = {"ZOEU3GXLC0CQPQ0X", "9XUKIB685I5VMHF6"};
        Status[] lastStatuses = statusRepo.getLastStatuses(Arrays.asList(beehives));
        System.out.println("sql test");
        return Arrays.toString(lastStatuses);
    }

    @PostMapping("/requestConnect")
    public String requestConnect(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        String beehive = json.getString("beehive");
        System.out.println(beehive);
        return "ok";
    }
}
