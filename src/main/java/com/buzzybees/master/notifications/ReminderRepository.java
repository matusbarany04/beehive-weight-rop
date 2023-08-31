package com.buzzybees.master.notifications;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {

    @Query("SELECT r FROM Reminder r WHERE r.userId = :userId")
    Reminder[] getUserReminders(long userId);
}
