package com.buzzybees.master.notifications;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {

    @Query("SELECT r FROM Reminder r WHERE r.userId = :userId")
    Reminder[] getUserReminders(long userId);


    @Transactional
    @Modifying
    @Query("DELETE FROM Reminder r WHERE r.id = :reminderId")
    void deleteByReminderId( long reminderId);


    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reminder r WHERE r.userId = :userId AND r.id = :reminderId")
    boolean isReminderUsers(long userId, long reminderId);

}
