package com.buzzybees.master;

import com.buzzybees.master.notifications.Notifications;
import com.buzzybees.master.users.Mailer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class MasterApplication implements WebMvcConfigurer  {

    public static void main(String[] args) {
        SpringApplication.run(MasterApplication.class, args);
        Notifications.scheduleReminders();
        Mailer.checkKeysExpiration();

        Mailer.sendVerification("matus.barany04@gmail.com", 1152);
    }
}
