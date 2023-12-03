package com.buzzybees.master.beehives.devices;

import com.buzzybees.master.beehives.Beehive;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceRepository extends CrudRepository<Device, Long> {

    @Query("SELECT d FROM Device d WHERE d.beehive = :beehive AND d.port = :port ")
    Device getDeviceOnPort(Beehive beehive, String port);

    @Query("SELECT d FROM Device d WHERE d.beehive.token = :beehive")
    List<Device> getBeehiveDevices(String beehive);
}
