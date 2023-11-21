package com.buzzybees.master.beehives.devices;

import com.buzzybees.master.beehives.Beehive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceManager {

    public static final String[] DEVICE_TYPES = {"temp", "temp-humid", "light", "sound"};

    public static long createSensor(DeviceRepository repository, Beehive beehive, int type, int port) {
        Device device = new Device();
        device.setBeehive(beehive);
        device.setType(DEVICE_TYPES[type]);
        device.setPort("S" + (port + 1));
        device.setName(DEVICE_TYPES[type] + " 1");
        Device savedDevice = repository.save(device);
        return savedDevice.getId();
    }
}
