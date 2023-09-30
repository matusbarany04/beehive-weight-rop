package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.*;
import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.beehives.devices.DeviceManager;
import com.buzzybees.master.beehives.devices.SensorValue;
import com.buzzybees.master.beehives.devices.SensorValueRepository;
import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.buzzybees.master.exceptions.TimestampException;
import com.buzzybees.master.tables.Status;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@RestController
public class BeeController extends DatabaseController {

    @Autowired
    StatusRepository statusRepo;

    @Autowired
    SensorValueRepository sensorValueRepository;

    @GetMapping("/clk_sync")
    public long clk() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * Checks timestamp and insert new data to the database.
     *
     * @param status - JSON parsed data
     * @return status whether data is correct and successfully saved.
     */
    @PostMapping(value = {"/updateStatus"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateStatus(@RequestBody Status status) {
        List<SensorValue> valueList = status.getSensorValues();
        List<Action> actions = new LinkedList<>();

        valueList.forEach(sensorValue -> {
            sensorValue.setStatus(status);

            if(sensorValue.getSensorId() == 0) {
                BeehiveRepository beehiveRepository = getRepo(Beehive.class);
                Beehive beehive = beehiveRepository.getBeehiveByToken(status.getBeehive());
                long id = DeviceManager.createSensor(getRepo(Device.class), beehive, sensorValue.getType(), sensorValue.getPort());
                actions.add(new Action("BURN_SENSOR_ID", id));
                sensorValue.setSensorId(id);
            }
        });

        status.setTimestamp(new Date().getTime());

        sensorValueRepository.saveAll(status.getSensorValues());
        statusRepo.save(status);

        return new ApiResponse("actions", actions);
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

    @PostMapping("/test")
    public String test() {
        return "TEST";
    }
}
