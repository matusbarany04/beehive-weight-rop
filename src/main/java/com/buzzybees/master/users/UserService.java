package com.buzzybees.master.users;

import com.buzzybees.master.tables.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements ApplicationContextAware {

    public static final String USER_CACHE_FILENAME = "users.json";

    private static ApplicationContext context;

    private final UserRepository userRepository;

    private static final HashMap<String, Long> loggedUsers = new HashMap<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        try {
            FileInputStream fis = new FileInputStream(USER_CACHE_FILENAME);
            String content = new String(fis.readAllBytes());
            System.out.println(content);

            JSONObject jsonObject = new JSONObject(content);
            for(String key : jsonObject.keySet()) {
                loggedUsers.put(key, jsonObject.getLong(key));
            }

            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        System.out.println(
                "Callback triggered - @PreDestroy.");
        saveData();
    }
    public static void saveData() {

        try {
            JSONObject jsonObject = new JSONObject(loggedUsers);
            System.out.println(jsonObject.toString());
            FileOutputStream fos = new FileOutputStream(USER_CACHE_FILENAME);
            fos.write(jsonObject.toString().getBytes());
            fos.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static boolean isTokenValid(String token) {
        return loggedUsers.containsKey(token);
    }

    public static long getUserIdByToken(String token) throws NullPointerException {
        Long user = loggedUsers.get(token);
        return user != null ? user : 0;
    }

    public static String getTokenByUserId(long userId) throws NullPointerException {
       return getKeyByValue(loggedUsers, userId);
    }

    public static String loginUser(User user) {
        String ssid = RequestContextHolder.currentRequestAttributes().getSessionId();
        loggedUsers.put(ssid, user.id);
        return ssid;
    }

    public static void logoutUser(String token) {
        loggedUsers.remove(token);
    }

    public ArrayList<User> getAll() {
        Iterable<User> iterable = userRepository.findAll();

        var users = new ArrayList<User>();
        iterable.forEach(users::add);

        return users;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
