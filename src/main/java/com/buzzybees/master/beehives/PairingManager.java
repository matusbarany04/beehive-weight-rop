package com.buzzybees.master.beehives;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class PairingManager {

    private static final HashMap<String, Long> beehivesInPairingMode = new HashMap<>();
    private static final HashMap<String, Long> startTimes = new HashMap<>();

    private static BeehiveRepository beehiveRepository;

    public static final int TIMEOUT_SECONDS = 30;

    public static final int BEEHIVE_EXIST = -1;
    public static final int NOT_PAIRING_MODE = -2;
    public static final int ERROR_TIMEOUT = -3;
    public static final int PAIRING_SUCCESSFUL = 0;

    @Autowired
    public PairingManager(BeehiveRepository beehiveRepository) {
        PairingManager.beehiveRepository = beehiveRepository;
    }

    public static void init(String beehiveToken, long userId) {
        beehivesInPairingMode.put(beehiveToken, userId);
        startTimes.put(beehiveToken, System.currentTimeMillis());
    }

    public static int requestPair(String beehiveToken, String model) {

        if(beehiveRepository.getBeehiveByToken(beehiveToken) != null) return BEEHIVE_EXIST;
        Long userId = beehivesInPairingMode.get(beehiveToken);

        if(userId == null) return NOT_PAIRING_MODE;
        if(isExpired(beehiveToken)) return ERROR_TIMEOUT;

        Beehive beehive = new Beehive(beehiveToken, model);
        beehive.setUserId(userId);
        beehiveRepository.save(beehive);
        beehivesInPairingMode.remove(beehiveToken);

        return PAIRING_SUCCESSFUL;
    }

    public static boolean isPaired(String beehive) {
        return !beehivesInPairingMode.containsKey(beehive);
    }

    public static boolean isExpired(String beehive) {
        long start = startTimes.getOrDefault(beehive, 0L);
        return System.currentTimeMillis() - start > TIMEOUT_SECONDS * 1000;
    }
}
