package com.buzzybees.master.config;

import nl.martijndwars.webpush.Encoding;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.concurrent.ExecutionException;

@Configuration
public class WebPushConfig {

    // Replace these values with your actual VAPID keys
    private static final String PUBLIC_VAPID_KEY = "BCZCy-snf9UZVm6M74AoNGmmkuSqCs-sWcmCZLiiytyyA8ZCBMSLa3NXhE5AUFwGoqeqs8wCJoIzqcdCOZ6Z8LI";
    private static final String PRIVATE_VAPID_KEY = "NLBYhM380Di7nkjjOwunczARMntaeV7oTy3UqejGs2o";

    private static PushService pushService;

    @Bean
    public PushService pushService() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        pushService = new PushService(PUBLIC_VAPID_KEY, PRIVATE_VAPID_KEY);
        pushService.setSubject("mailto:test@test.com");
        return pushService;
    }

    public static void send(Notification notification) {
        try {
            pushService.send(notification, Encoding.AES128GCM);
        } catch (GeneralSecurityException | IOException | JoseException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}