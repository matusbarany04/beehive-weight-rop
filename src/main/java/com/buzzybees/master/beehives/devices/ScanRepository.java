package com.buzzybees.master.beehives.devices;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ScanRepository extends CrudRepository<Scan, Long> {

    @Query("SELECT s FROM Scan s WHERE s.beehive LIKE :beehive ORDER BY s.date")
    public Scan getLastScan(String beehive);
}
