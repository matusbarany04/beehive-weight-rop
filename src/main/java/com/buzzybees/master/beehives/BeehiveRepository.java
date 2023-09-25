package com.buzzybees.master.beehives;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BeehiveRepository extends CrudRepository<Beehive, Long> {

    @Query("SELECT b FROM Beehive b WHERE b.userId = :userId")
    Beehive[] getAllByUser(long userId);

    @Query("SELECT b.token FROM Beehive b WHERE b.userId = :userId")
    String[] getBeehiveTokens(long userId);

    @Query("SELECT b FROM Beehive b WHERE b.token = :token")
    Beehive getBeehiveByToken(String token);

    @Query("SELECT b FROM Beehive b WHERE b.userId = :userId")
    Beehive[] getAllWithSensors(long userId);

}
