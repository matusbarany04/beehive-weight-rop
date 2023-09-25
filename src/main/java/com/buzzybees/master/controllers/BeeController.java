package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.PairingManager;
import com.buzzybees.master.beehives.StatusRepository;
import com.buzzybees.master.beehives.devices.Scan;
import com.buzzybees.master.beehives.devices.ScanRepository;
import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.buzzybees.master.exceptions.TimestampException;
import com.buzzybees.master.tables.Status;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@RestController
public class BeeController extends DatabaseController {

    @Autowired
    StatusRepository statusRepo;

    @GetMapping("/clk_sync")
    public long clk() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * Checks timestamp and insert new data to the database.
     *
     * @param status data from ESP
     * @return status whether data is correct and successfully saved.
     */
    @PostMapping(value = {"/updateStatus"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateStatus(@ModelAttribute Status status) throws TimestampException {

        if (status.getTimestamp() < clk()) throw new TimestampException();
        statusRepo.save(status);
        return new ApiResponse();
    }

    /**
     * Pairs beehive with user and insert new beehive to the database.
     *
     * @param data - JSON string data - beehive
     * @return status whether beehive is successfully paired.
     */
    @PostMapping("/requestPair")
    public String requestConnect(@RequestBody String data) {
        JSONObject json = new JSONObject(data);
        String beehive = json.getString("beehive");
        String model = json.getString("model");

        int status = PairingManager.requestPair(beehive, model);

        return switch (status) {
            case PairingManager.PAIRING_SUCCESSFUL -> "SUCCESS";
            case PairingManager.NOT_PAIRING_MODE -> "NOT_PAIRING_MODE";
            case PairingManager.BEEHIVE_EXIST -> "ERROR_ALREADY_PAIRED";
            case PairingManager.ERROR_TIMEOUT -> "ERROR_TIMEOUT";
            default -> "ERROR";
        };
    }

    /**
     * Inserts new scan to database.
     * @param data results
     * @return status whether action was successful.
     */
    @PostMapping("/newScan")
    public ApiResponse newScan(@RequestBody String data) {
        ScanRepository scanRepository = getRepo(Scan.class);

        JSONObject json = new JSONObject(data);
        Scan scan = new Scan();

        scan.setBeehive(json.getString("token"));
        scan.setDevices(json.getJSONObject("devices"));
        scan.setDate(new Date());

        scanRepository.save(scan);
        return new ApiResponse();
    }

    @GetMapping("/test")
    public ApiResponse test() {
        BeehiveRepository beehiveRepository = getRepo(Beehive.class);
        System.out.println(statusRepo.csvSelect(1));


        return new ApiResponse("statuses", null);
    }
}
