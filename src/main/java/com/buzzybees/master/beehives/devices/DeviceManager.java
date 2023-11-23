package com.buzzybees.master.beehives.devices;

import com.buzzybees.master.beehives.Beehive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceManager {

    public static final String[] DEVICE_TYPES = {"temp", "temp-humid", "light", "sound"};

    public static long createSensor(DeviceRepository repository, Beehive beehive, int type, int port) {
        Device device = new Device();
        device.setBeehive(beehive);
        device.setType(DEVICE_TYPES[type]);
        device.setPort(port);
        device.setName(DEVICE_TYPES[type] + " 1");
        Device savedDevice = repository.save(device);
        return savedDevice.getId();
    }

    public static boolean updatePort(DeviceRepository deviceRepository, long deviceId, int port) {
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
}
