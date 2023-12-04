package com.buzzybees.master.websockets;

import com.buzzybees.master.beehives.actions.ServerActionType;

import java.util.HashMap;

public record ServerAction(ServerActionType type, HashMap<String, Object> params) {

}
