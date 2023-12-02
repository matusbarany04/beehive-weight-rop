package com.buzzybees.master;

import com.buzzybees.master.notifications.Notifications;
import com.buzzybees.master.users.Mailer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class MasterApplication implements WebMvcConfigurer  {

    public static void main(String[] args) {
        SpringApplication.run(MasterApplication.class, args);
        Notifications.scheduleReminders();
        Mailer.checkKeysExpiration();
    }

/*
    @Bean
    public ApiResponseHandler apiResponseHandler() {
        return new ApiResponseHandler();
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(new ApiResponseHandler());
    }*/

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/dashboard").allowedOrigins("http://localhost:5173");
//            }
//        };
//    }
}
