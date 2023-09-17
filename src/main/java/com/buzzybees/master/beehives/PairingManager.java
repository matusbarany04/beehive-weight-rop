package com.buzzybees.master.beehives;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

record Request(String beehive, long userId, boolean paired) {

}

@Component
public class PairingManager {

    @Autowired
    private static BeehiveRepository beehiveRepository;

    private static ArrayList<Request> pairingRequests = new ArrayList<>();

    public PairingManager(String beehive, long userId) {
        Request request = new Request(beehive, userId, false);
        pairingRequests.add(request);
    }

    public static void requestPair(String beehiveId) {
        Beehive beehive = new Beehive();
        beehiveRepository.save()
    }

    public static boolean isPaired(String beehive) {

    }
}
