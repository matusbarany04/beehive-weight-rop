package com.buzzybees.master.notifications;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.userId = :userId")
    Notification[] getUserNotifications(long userId);

    @Query("SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.timestamp desc ")
    Page<Notification> getUserNotificationsWithPagination(long userId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId")
    long countUserNotifications(long userId);
}
