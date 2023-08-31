package com.buzzybees.master.notifications;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.userId = :userId")
    Notification[] getUserNotifications(long userId);
}
