package com.buzzybees.master.beehives.devices;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.actions.Action;
import com.buzzybees.master.beehives.actions.Actions;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.buzzybees.master.websockets.ClientMessage;
import com.buzzybees.master.websockets.ClientSocketHandler;
import com.buzzybees.master.websockets.EspSocketHandler;
import com.buzzybees.master.websockets.MessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

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

            return previous == null || !previous.equals(device.getPort());
        }

        return false;
    }

    public static void sendConfigToClient(String beehiveToken) {
        BeehiveRepository beehiveRepository = DatabaseController.accessRepo(Beehive.class);
        Beehive beehive = beehiveRepository.getBeehiveByToken(beehiveToken);

        List<Device> devices = beehive.getDevices();
        HashMap<String, Object> deviceMap = new HashMap<>();

        for (Device device : devices) {
            String port = device.getPort();
            if(port != null) {
                device.setPort(null);
                deviceMap.put(port, new ObjectMapper().convertValue(device, HashMap.class));
            }
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("devices", deviceMap);
        ClientSocketHandler.sendMessageToUser(beehive.getUserId(), new ClientMessage(MessageType.UPDATE_DEVICE_CONFIG, params));
    }

    public static void updateDeviceConfig(Beehive beehive, HashMap<String, Object> params) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject json = new JSONObject(params.get("config").toString());
            List<Device> devices = objectMapper.readValue(json.getJSONArray("sensors").toString(), new TypeReference<>(){});
            List<Device> notConnectedDevices = beehive.getDevices();
            devices.forEach(device -> {
                if (device.getId() == 0) {
                    long id = createSensor(beehive, device.getType(), device.getPortIndex());
                    Action burnAction = Actions.createBurnAction(beehive, id, device.getPortIndex());
                    EspSocketHandler.sendFlashActionToBeehive(burnAction);
                }
                else updatePort(device.getId(), device.getPortIndex());
                notConnectedDevices.removeIf(oldDevice -> oldDevice.getId() == device.getId());
            });

            DeviceRepository deviceRepository = DatabaseController.accessRepo(Device.class);
            for(Device device : notConnectedDevices) device.setPort(null);
            deviceRepository.saveAll(notConnectedDevices);
            sendConfigToClient(beehive.getToken());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
