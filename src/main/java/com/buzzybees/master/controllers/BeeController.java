package com.buzzybees.master.controllers;

import com.buzzybees.master.beehives.*;
import com.buzzybees.master.beehives.actions.*;
import com.buzzybees.master.beehives.devices.*;
import com.buzzybees.master.websockets.EspSocketHandler;
import com.buzzybees.master.controllers.template.ApiResponse;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.buzzybees.master.notifications.Notifications;
import com.buzzybees.master.tables.Status;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;


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

    @GetMapping("/sendSocketESP")
    public void sendSocket() {
        EspSocketHandler.sendFlashActionToBeehive(new Action());
    }

    /**
     * Checks timestamp and insert new data to the database.
     *
     * @return status whether data is correct and successfully saved.
     */
    @PostMapping(value = {"/updateStatus"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateStatus(@RequestBody Status.Request statusRequest) {
        List<Action> actions = new ArrayList<>(List.of(actionRepository.getPendingActionsByBeehiveId(statusRequest.getBeehive())));
        DeviceRepository deviceRepository = DatabaseController.accessRepo(Device.class);

        statusRequest.setTimestamp(new Date().getTime());
        Status savedStatus = statusRepo.save(statusRequest.getBase());

        List<SensorValue> sensors = statusRequest.getSensorValues();
        List<Device> notConnectedDevices = deviceRepository.getBeehiveDevices(savedStatus.getBeehive());

        sensors.forEach(sensorValue -> {
            sensorValue.setStatusId(savedStatus.getStatusId());

            if (sensorValue.getSensorId() == 0) {
                BeehiveRepository beehiveRepository = getRepo(Beehive.class);
                Beehive beehive = beehiveRepository.getBeehiveByToken(statusRequest.getBeehive());
                long id = DeviceManager.createSensor(beehive, sensorValue.getType(), sensorValue.getPort());

                actions.add(Actions.createBurnAction(beehive, id, sensorValue.getPort()));
                sensorValue.setSensorId(id);

            } else DeviceManager.updatePort(sensorValue.getSensorId(), sensorValue.getPort());

            notConnectedDevices.removeIf(device -> device.getId() == sensorValue.getSensorId());
        });

        for(Device device : notConnectedDevices) device.setPort(null);
        deviceRepository.saveAll(notConnectedDevices);

        for(Action action : actions) action.setStatus(ActionStatus.SENT);

        actionRepository.saveAll(actions);
        sensorValueRepository.saveAll(sensors);
        DeviceManager.sendConfigToClient(statusRequest.getBeehive());

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


    /**
     * Handles a POST request to "/test".
     *
     * @return A String response "TEST".
     */
    @PostMapping("/test")
    public String test() {
        return "TEST";
    }


    /**
     * Updates the statuses of actions based on input data in the specified JSON format.
     *
     * The input data should be an array of objects, each containing "id" and "status" fields.
     * Example:
     * [
     *   {
     *     "id": 52313,
     *     "status": "DONE"
     *   }
     * ]
     *
     * @param objects A list of HashMaps representing action status changes.
     * @return ApiResponse indicating the result:
     *         - If all actions are updated successfully, returns ApiResponse with status "OK".
     *         - If there are invalid actions, returns ApiResponse with status "invalid" and a list of invalid actions.
     */
    @PostMapping(value = "/updateActionsStatuses", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse updateActionsStatuses(@RequestBody List<HashMap<String, Object>> objects) {
        boolean invalidActions = false;
        ArrayList<HashMap<String, Object>> invalidActionList = new ArrayList<>();

        for (HashMap<String,Object> actionStatusChange : objects) {
            Integer id = (Integer) actionStatusChange.get("id");

            try {
                ActionStatus newStatus = ActionStatus.valueOf((String) actionStatusChange.get("status"));
                Actions.updateActionStatus(id, newStatus);

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
