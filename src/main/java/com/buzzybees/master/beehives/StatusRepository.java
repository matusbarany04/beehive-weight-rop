package com.buzzybees.master.beehives;

import com.buzzybees.master.tables.Status;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatusRepository extends CrudRepository<Status, Long> {

    @Query("SELECT s FROM Status s WHERE s.beehive LIKE :beehiveId")
    Status[] getAllBeehiveStatuses(String beehiveId);

    @Query("SELECT s FROM Status s WHERE s.beehive LIKE :beehiveId AND s.timestamp > :timestamp")
    Status[] getAllStatusesSince(String beehiveId, long timestamp);

    @Query("SELECT s FROM Status s WHERE s.beehive in (:beehives) AND s.timestamp > :timestamp ORDER BY s.timestamp")
    Status[] getAllStatusesSince(String[] beehives, long timestamp);

    @Query(value = "SELECT * FROM status WHERE (beehive, timestamp) IN (SELECT beehive, MAX(timestamp) FROM status WHERE beehive IN(:tokens) GROUP BY beehive)", nativeQuery = true)
    Status[] getLastStatuses(@Param("tokens") List<String> tokens);

    @Query("SELECT s, MAX(s.timestamp) FROM Status s WHERE s.beehive LIKE :beehiveId")
    Status getLastStatus(String beehiveId);

    @Query("SELECT s FROM Status s INNER JOIN Beehive b ON b.token = s.beehive WHERE b.userId = :id AND s.timestamp > :timestamp ORDER BY s.timestamp")
    Status[] getUserStatusesSince(long id, long timestamp);

    @Query("SELECT s,b FROM Status s INNER JOIN Beehive b ON b.token = s.beehive WHERE b.userId = :id AND s.timestamp = (SELECT MAX(s1.timestamp) FROM Status s1 WHERE s1.beehive = b.token)")
    List<Object[]> csvSelect(long id);
}
