package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.*;
import com.buzzybees.master.beehives.actions.Action;
import com.buzzybees.master.beehives.actions.ActionRepository;
import com.buzzybees.master.beehives.actions.ActionStatus;
import com.buzzybees.master.beehives.actions.ActionType;
import com.buzzybees.master.beehives.devices.*;
import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.buzzybees.master.tables.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;

import static com.buzzybees.master.beehives.actions.Action.NOW;


@RestController
public class BeeController extends DatabaseController {

    @Autowired
    StatusRepository statusRepo;

    @Autowired
    SensorValueRepository sensorValueRepository;

    @Autowired
    ActionRepository actionRepository;

    @GetMapping("/clk_sync")
    public long clk() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * Checks timestamp and insert new data to the database.
     *
     * @return status whether data is correct and successfully saved.
     */
    @PostMapping(value = {"/updateStatus"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateStatus(@RequestBody Status.Request statusRequest) {
        // actionRepository.removeDoneActions();
        List<Action> actions = new ArrayList<>(List.of(actionRepository.getActionsByBeehiveId(statusRequest.getBeehive())));

        statusRequest.setTimestamp(new Date().getTime());
        Status savedStatus = statusRepo.save(statusRequest.getBase());

        List<SensorValue> sensors = statusRequest.getSensorValues();

        sensors.forEach(sensorValue -> {
            sensorValue.setStatusId(savedStatus.getStatusId());

            if (sensorValue.getSensorId() == 0) {
                BeehiveRepository beehiveRepository = getRepo(Beehive.class);
                Beehive beehive = beehiveRepository.getBeehiveByToken(statusRequest.getBeehive());
                long id = DeviceManager.createSensor(getRepo(Device.class), beehive, sensorValue.getType(), sensorValue.getPort());

                // we create a new wake up call for the beehive to wake up next time ??
                Action action = new Action(ActionType.BURN_SENSOR_ID, NOW,
                        new JSONObject() {{
                            put("sensorId", id);
                            put("port", sensorValue.getPort());
                        }}.toString(),
                        beehive.getToken(),
                        -1
                );
                System.out.println(actions);
                actions.add(action);
                // we save it in case it won't go through??
                actionRepository.save(action);

                sensorValue.setSensorId(id);

            } else {
                DeviceRepository deviceRepository = getRepo(Device.class);
                DeviceManager.updatePort(deviceRepository, sensorValue.getSensorId(), sensorValue.getPort());
            }
        });

        sensorValueRepository.saveAll(sensors);

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


    /**
     * accept data in this format
     * [
     * {
     * id:52313,
     * status: "DONE"
     * }
     * ]
     *
     * @param objects
     * @return
     */
    @PostMapping(value = "/updateActionsStatuses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateActionsStatuses(@RequestBody List<HashMap<String, Object>> objects) {
        ActionRepository actionRepository = getRepo(Action.class);
        boolean invalidActions = false;
        ArrayList<HashMap<String, Object>> invalidActionList = new ArrayList<>();

        for (HashMap<String,Object> actionStatusChange : objects) {
            Integer id = (Integer) actionStatusChange.get("id");

            try {
                Action action = actionRepository.getActionById(id);
                ActionStatus newStatus = ActionStatus.valueOf((String) actionStatusChange.get("status"));
                action.setStatus(newStatus);

                actionRepository.save(action);
            } catch (IllegalArgumentException | NullPointerException ignored){
                invalidActions = true;
                invalidActionList.add(actionStatusChange);
            }
        }
        if(invalidActions)
            return new ApiResponse("invalid", invalidActionList);
        else
            return ApiResponse.OK();
    }
}
