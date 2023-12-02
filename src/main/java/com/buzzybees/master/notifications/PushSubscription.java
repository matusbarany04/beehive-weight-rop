package com.buzzybees.master.notifications;

public record PushSubscription(String endpoint, PushSubscriptionKeys keys) {
    record PushSubscriptionKeys(String p256dh, String auth) {
    }
}
