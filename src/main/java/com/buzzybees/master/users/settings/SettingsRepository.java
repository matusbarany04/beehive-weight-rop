package com.buzzybees.master.users.settings;

import com.buzzybees.master.tables.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface SettingsRepository extends CrudRepository<Settings, Long> {


    @Query("SELECT s FROM Settings s WHERE s.id = :id")
    Settings getSettingsById(Long id);

    @Query("SELECT s FROM Settings s WHERE s.userId = :id")
    Settings getSettingsByUserId(Long id);

    @Query("SELECT s FROM Settings s WHERE s.userId = :id")
    Settings[] getAllSettingsByUserId(Long id);

    @Modifying
    @Query("UPDATE Settings s SET s = :settings WHERE s.id = :settingsId")
    void updateSettings(Long settingsId, Settings settings);


}
