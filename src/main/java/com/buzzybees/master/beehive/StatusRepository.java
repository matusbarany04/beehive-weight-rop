package com.buzzybees.master.beehive;

import com.buzzybees.master.tables.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface StatusRepository extends CrudRepository<Status, String> {

    @Query("SELECT s FROM Status s WHERE s.beehive LIKE :beehiveId")
    Status[] getAllBeehiveStatuses(String beehiveId);

    @Query("SELECT s FROM Status s WHERE s.beehive LIKE :beehiveId AND s.timestamp > :timestamp")
    Status[] getAllStatusesSince(String beehiveId, long timestamp);

    @Query("SELECT s FROM Status s where s.beehive in (:beehives) AND s.timestamp > :timestamp")
    Status[] getAllStatusesSince(String[] beehives, long timestamp);

    @Query("SELECT s FROM Status s WHERE s.beehive LIKE :beehiveId ORDER BY s.timestamp DESC LIMIT 1")
    Status[] getLastStatus(String beehiveId);
}
