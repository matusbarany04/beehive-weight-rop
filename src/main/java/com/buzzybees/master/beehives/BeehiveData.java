package com.buzzybees.master.beehives;

import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.beehives.devices.SensorValue;
import com.buzzybees.master.tables.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.lang.ref.Reference;
import java.util.*;

record DataGroup(long from, Set<Long> sensorIds, ArrayList<Float> values) {

}

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeehiveData {

    private String currentStatus;
    private int battery;
    private final LinkedHashSet<Long> timestamp = new LinkedHashSet<>();
    private final List<Float> weight = new LinkedList<>();
    private List<DataGroup> temperature;
    private List<DataGroup> humidity;
    private List<DataGroup> light;
    private List<DataGroup> sound;

    public void push(Status status, SensorValue sensorValue) {
        boolean newStatus = timestamp.add(status.getTimestamp());

        if(newStatus) {
            currentStatus = status.getStatus();
            battery = status.getBattery();
            weight.add(status.getWeight());
        }

        if(sensorValue != null) {

            List<DataGroup> list = getDataList(sensorValue.getType());

            DataGroup group = getGroupBySensorId(sensorValue.getSensorId(), list);
            if(group != null && addToGroup(group, sensorValue)) return;

            for (DataGroup dataGroup : list) {
                if(addToGroup(dataGroup, sensorValue)) return;
            }

            ArrayList<Float> values = new ArrayList<>(Collections.singletonList(sensorValue.getValue()));
            Set<Long> sensorIds = new HashSet<>(Collections.singletonList(sensorValue.getSensorId()));
            list.add(new DataGroup(status.getTimestamp(), sensorIds, values));
        }
    }

    private boolean addToGroup(DataGroup dataGroup, SensorValue sensorValue) {
        ArrayList<Long> timestampList = new ArrayList<>(timestamp);
        int emptyPlaces = timestamp.size() - (timestampList.indexOf(dataGroup.from()) + dataGroup.values().size());
        for (int i = 0; i < emptyPlaces; i++) dataGroup.values().add(0f);
        if (emptyPlaces >= 0) {
            dataGroup.values().add(sensorValue.getValue());
            dataGroup.sensorIds().add(sensorValue.getSensorId());
            return true;
        }

        return false;
    }

    private DataGroup getGroupBySensorId(long sensorId, List<DataGroup> list) {
        for (DataGroup dataGroup : list) {
            if(dataGroup.sensorIds().contains(sensorId)) return dataGroup;
        }

        return null;
    }

    private List<DataGroup> getDataList(int type) {
        List<DataGroup> list = switch (type) {
            case Device.TEMPERATURE -> temperature;
            case Device.LIGHT -> light;
            case Device.TEMP_HUMID -> humidity;
            case Device.SOUND -> sound;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        if (list == null) {
            list = new LinkedList<>();
            switch (type) {
                case Device.TEMPERATURE -> temperature = list;
                case Device.LIGHT -> light = list;
                case Device.TEMP_HUMID -> humidity = list;
                case Device.SOUND -> sound = list;
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }
        }

        return list;
    }


    public String getCurrentStatus() {
        return currentStatus;
    }

    public int getBattery() {
        return battery;
    }

    public Set<Long> getTimestamp() {
        return timestamp;
    }

    public List<Float> getWeight() {
        return weight;
    }

    public List<DataGroup> getTemperature() {
        return temperature;
    }

    public List<DataGroup> getHumidity() {
        return humidity;
    }

    public List<DataGroup> getLight() {
        return light;
    }

    public List<DataGroup> getSound() {
        return sound;
    }
}
