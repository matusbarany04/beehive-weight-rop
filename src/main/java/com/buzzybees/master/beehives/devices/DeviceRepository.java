package com.buzzybees.master.beehives.devices;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DeviceRepository extends CrudRepository<Device, Long> {

    @Query("SELECT d FROM Device d WHERE d.port = :port")
    Device getDeviceOnPort(String port);
}
