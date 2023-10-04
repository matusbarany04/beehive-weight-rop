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
    private final Set<Long> timestamps = new HashSet<>();
    private final List<Float> weights = new LinkedList<>();
    private List<DataGroup> temperature;
    private List<DataGroup> humidity;
    private List<DataGroup> light;
    private List<DataGroup> sound;

    public void push(Status status, SensorValue sensorValue) {
        boolean newStatus = timestamps.add(status.getTimestamp());

        if(newStatus) {
            currentStatus = status.getStatus();
            battery = status.getBattery();
            weights.add(status.getWeight());
        }

        ArrayList<Long> timestampList = new ArrayList<>(timestamps);
        List<DataGroup> list = getDataList(sensorValue.getType());

        for (DataGroup dataGroup : list) {
            int emptyPlaces = timestamps.size() - (timestampList.indexOf(dataGroup.from()) + dataGroup.values().size());
            for (int i = 0; i < emptyPlaces; i++) dataGroup.values().add(0f);
            if (emptyPlaces >= 0) {
                dataGroup.values().add(sensorValue.getValue());
                dataGroup.sensorIds().add(sensorValue.getSensorId());
                return;
            }
        }

        ArrayList<Float> values = new ArrayList<>(Collections.singletonList(sensorValue.getValue()));
        Set<Long> sensorIds = new HashSet<>(Collections.singletonList(sensorValue.getSensorId()));
        list.add(new DataGroup(status.getTimestamp(), sensorIds, values));
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

    public Set<Long> getTimestamps() {
        return timestamps;
    }

    public List<Float> getWeights() {
        return weights;
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
