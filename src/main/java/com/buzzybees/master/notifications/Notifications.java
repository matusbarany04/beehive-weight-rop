package com.buzzybees.master.notifications;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.actions.Action;
import com.buzzybees.master.controllers.template.DatabaseController;

public class Notifications {

    public static void errorAlert(Action action) {
        NotificationRepository notificationRepository = DatabaseController.accessRepo(Notification.class);
        BeehiveRepository beehiveRepository = DatabaseController.accessRepo(Beehive.class);
        long userId = beehiveRepository.getOwner(action.getBeehive());
        Notification notification = new Notification(Notification.Type.PROBLEM, userId, action.getStatus().name, "");
        notificationRepository.save(notification);
    }
}
