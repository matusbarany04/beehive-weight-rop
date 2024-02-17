package com.buzzybees.master.users;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.io.ClassPathResource;

import javax.mail.*;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Mailer {

    public static final String SENDER = "BuzzyBees";
    public static final String ADDRESS = "noreply.buzzybees@azet.sk";
    public static final String HOST = "smtp.azet.sk";
    public static final String USERNAME = "noreply.buzzybees";
    public static final String PASSWORD = "eac2e132";

    public static final String BACKEND_HOST = "http://localhost:5173";

    public static final long KEY_EXPIRE_SECONDS = 3600;
    public static final int CHECK_EXPIRATION_SECONDS = 300;
    public static final int PORT = 465;

    public static ArrayList<SecureKey> secureKeys = new ArrayList<>();

    public static void sendVerification(String email, long id) {
        try {
            String message = getHtmlMessage(id);
            sendMessage(email, "Email Verification", message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String email, String subject, String message) {
        MimeMessage msg = new MimeMessage(getSession());

        try {
            msg.setFrom(new InternetAddress(ADDRESS, SENDER));
            msg.setSubject(subject, "UTF-8");

            msg.setContent(message, "text/html; charset=UTF-8");
            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
            Transport.send(msg);

            System.out.println("Email Sent Successfully!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getHtmlMessage(long id) throws IOException {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[10];
        secureRandom.nextBytes(randomBytes);
        String key = base64Encoder.encodeToString(randomBytes);
        SecureKey secureKey = new SecureKey(key, id, new Date().getTime() + KEY_EXPIRE_SECONDS);
        secureKeys.add(secureKey);

        File file = new ClassPathResource("/templates/verification.html").getFile();
        Document doc = Jsoup.parse(file, "UTF-8");
        Element link = doc.getElementById("verification_url");
        if(link != null) link.attr("href", BACKEND_HOST + "/verify?key=" + key);

        return doc.toString();
    }

    private static Session getSession() {
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.trust", HOST);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        };


        return Session.getInstance(props, authenticator);
    }

    private record SecureKey(String key, long userId, long timeout) {

    }

    public static long getUserId(String key) {
        for(SecureKey secureKey : secureKeys) {
            if(key.equals(secureKey.key)) return secureKey.userId;
        }
        return 0;
    }

    public static void invalidateSecureKey(long userId) {
        secureKeys.removeIf(secureKey -> secureKey.userId == userId);
    }

    public static void checkKeysExpiration() {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(() -> {
            Date date = new Date();
            secureKeys.removeIf(secureKey -> secureKey.timeout < date.getTime());
        }, 0, CHECK_EXPIRATION_SECONDS, TimeUnit.SECONDS);

    }
}
