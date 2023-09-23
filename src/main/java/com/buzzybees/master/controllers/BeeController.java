package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.PairingManager;
import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.beehives.devices.DeviceRepository;
import com.buzzybees.master.beehives.devices.Scan;
import com.buzzybees.master.beehives.devices.ScanRepository;
import com.buzzybees.master.beehives.StatusRepository;
import com.buzzybees.master.tables.Status;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class BeeController {

    @Autowired
    StatusRepository statusRepo;

    @Autowired
    ScanRepository scanRepository;

    @Autowired
    BeehiveRepository beehiveRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @GetMapping("/clk_sync")
    public long clk() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * Checks timestamp and insert new data to the database.
     * @param data - JSON string data
     *
     * @return status whether data is correct and successfully saved.
     */
    @PostMapping(value = {"/updateStatus"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateStatus(@RequestBody String data) {
        JSONObject response = new JSONObject();
        Status status = new Status(new JSONObject(data));

        if(status.getTimestamp() > clk()) {
            statusRepo.save(status);
            response.put("status", "ok");

        } else response.put("status", "ERR_WRONG_TIMESTAMP");

        return response.toString();
    }

    @GetMapping("/testStatuses")
    public String test() {
        String[] beehives = {"ZOEU3GXLC0CQPQ0X", "9XUKIB685I5VMHF6"};
        Status[] lastStatuses = statusRepo.getLastStatuses(Arrays.asList(beehives));
        System.out.println("sql test");
        return Arrays.toString(lastStatuses);
    }

    /**
     * Pairs beehive with user and insert new beehive to the database.
     * @param data - JSON string data - beehive
     *
     * @return status whether beehive is successfully paired.
     */
    @PostMapping("/requestPair")
    public String requestConnect(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        String beehive = json.getString("beehive");
        String model = json.getString("model");

        if(PairingManager.isExpired(beehive)) return "ERROR_TIMEOUT";

        int status = PairingManager.requestPair(beehive, model);

        return switch (status) {
            case PairingManager.PAIRING_SUCCESSFUL -> "SUCCESS";
            case PairingManager.NOT_PAIRING_MODE -> "NOT_PAIRING_MODE";
            case PairingManager.BEEHIVE_EXIST -> "ERROR_ALREADY_PAIRED";

            default -> "ERROR";
        };
    }

    @PostMapping("/newScan")
    public String newScan(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        Scan scan = new Scan();
        scan.setBeehive(json.getString("token"));
        scan.setDevices(json.getJSONObject("devices"));
        scan.setDate(new Date());
        scanRepository.save(scan);

        return "{\"status\":\"ok\"";
    }

    @PostMapping("/test")
    public String testTable() {
        Beehive[] beehives = beehiveRepository.getAllByUser(1);

        for(Beehive beehive : beehives) {
            System.out.println(beehive.getName());
            System.out.println(beehive.getDevices());
        }

        return "OK";
    }
}
