package com.buzzybees.master.notifications;

import com.buzzybees.master.beehives.Beehive;
import com.buzzybees.master.beehives.BeehiveRepository;
import com.buzzybees.master.beehives.actions.Action;
import com.buzzybees.master.config.WebPushConfig;
import com.buzzybees.master.controllers.template.DatabaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.SocketHandler;


@Service
public class Notifications {

    private static final HashMap<Long, List<PushSubscription>> subscriptions = new HashMap<>();

    public static void subscribe(long userID, PushSubscription pushSubscription) {
        subscriptions.putIfAbsent(userID, new LinkedList<>());
        List<PushSubscription> userSubscriptions = subscriptions.get(userID);
        userSubscriptions.add(pushSubscription);
    }

    public static void sendPushToUser(long userID, String payload) {
        try {
            List<PushSubscription> userSubscriptions = subscriptions.get(userID);
            if(userSubscriptions != null) {
                for(PushSubscription subscription : userSubscriptions) {
                    System.out.println(subscription);
                    nl.martijndwars.webpush.Notification notification = new nl.martijndwars.webpush.Notification(
                            subscription.endpoint(),
                            subscription.keys().p256dh(),
                            subscription.keys().auth(),
                            payload
                    );
                    WebPushConfig.send(notification);
                }
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public static void errorAlert(Action action) {
        NotificationRepository notificationRepository = DatabaseController.accessRepo(Notification.class);
        BeehiveRepository beehiveRepository = DatabaseController.accessRepo(Beehive.class);
        long userId = beehiveRepository.getOwner(action.getBeehive());
        Notification notification = new Notification(Notification.Type.PROBLEM, userId, action.getStatus().name, "");
        notificationRepository.save(notification);
        notification.sendToUser();
    }
}
