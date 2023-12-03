package com.buzzybees.master.beehives.devices;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.actions.Action;
import com.buzzybees.master.beehives.actions.ActionRepository;
import com.buzzybees.master.beehives.actions.Actions;
import com.buzzybees.master.config.EspSocketHandler;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.common.COSArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceManager {

    public static long createSensor(Beehive beehive, DeviceType type, int port) {
        DeviceRepository repository = DatabaseController.accessRepo(Device.class);
        Device device = new Device();
        device.setBeehive(beehive);
        device.setType(type);
        device.setPort(port);
        device.setName(type.name() + " 1");
        Device savedDevice = repository.save(device);
        return savedDevice.getId();
    }

    public static boolean updatePort(long deviceId, int port) {
        DeviceRepository deviceRepository = DatabaseController.accessRepo(Device.class);
        Optional<Device> result = deviceRepository.findById(deviceId);
        if(result.isPresent()) {
            Device device = result.get();
            String previous = device.getPort();
            device.setPort(port);
            deviceRepository.save(device);

            return !previous.equals(device.getPort());
        }

        return false;
    }

    public static void updateDeviceConfig(Beehive beehive, HashMap<String, Object> params) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(params.get("devices"));
            List<Device> devices = objectMapper.readValue(json, new TypeReference<>(){});
            devices.forEach(device -> {
                if (device.getId() == 0) {
                    long id = createSensor(beehive, device.getType(), device.getPortIndex());
                    Action burnAction = Actions.createBurnAction(beehive, id, device.getPortIndex());
                    EspSocketHandler.sendFlashActionToBeehive(beehive.getToken(), burnAction);
                }
                else updatePort(device.getId(), device.getPortIndex());
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
