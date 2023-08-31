package com.buzzybees.master.users;

import com.buzzybees.master.tables.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserService implements ApplicationContextAware {

    private static ApplicationContext context;

    private final UserRepository userRepository;

    private static final HashMap<String, Long> loggedUsers = new HashMap<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    public static long getUserIdByToken(String token) throws NullPointerException {
        Long user = loggedUsers.get(token);
        return user != null ? user : 0;
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

}
