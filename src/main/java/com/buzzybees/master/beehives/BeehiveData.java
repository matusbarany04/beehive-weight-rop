package com.buzzybees.master.beehives;

import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.beehives.devices.SensorValue;
import com.buzzybees.master.tables.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

record DataGroup(int from, Set<Long> sensorIds, ArrayList<Float> values) {

}

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeehiveData {

    public static final float SENSOR_NO_VALUE = -999;

    private String currentStatus;
    private int battery;
    private final LinkedHashSet<Long> timestamps = new LinkedHashSet<>();
    private final List<Float> weights = new LinkedList<>();
    private List<DataGroup> temperature;
    private List<DataGroup> humidity;
    private List<DataGroup> light;
    private List<DataGroup> sound;

    /**
     * pushes new status to dataset
     * @param status status from database
     * @param sensorValue sensor value from database
     */
    public void push(Status status, SensorValue sensorValue) {
        boolean newStatus = timestamps.add(status.getTimestamp());

        if(newStatus) {
            currentStatus = status.getStatus();
            battery = status.getBattery();
            weights.add(status.getWeight());
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
            ArrayList<Long> timestampList = new ArrayList<>(timestamps);
            list.add(new DataGroup(timestampList.indexOf(status.getTimestamp()), sensorIds, values));
        }
    }

    /**
     * adds new sensor value to data group
     * @param dataGroup data group
     * @param sensorValue sensor value
     * @return if operation was performed
     */
    private boolean addToGroup(DataGroup dataGroup, SensorValue sensorValue) {
        int emptyPlaces = timestamps.size() - (dataGroup.from() + dataGroup.values().size());
        for (int i = 0; i < emptyPlaces; i++) dataGroup.values().add(SENSOR_NO_VALUE);
        if (emptyPlaces >= 0) {
            dataGroup.values().add(sensorValue.getValue());
            dataGroup.sensorIds().add(sensorValue.getSensorId());
            return true;
        }

        return false;
    }

    /**
     * @param sensorId which sensor
     * @param list data groups
     * @return data group where the sensor is already added
     */
    private DataGroup getGroupBySensorId(long sensorId, List<DataGroup> list) {
        for (DataGroup dataGroup : list) {
            if(dataGroup.sensorIds().contains(sensorId)) return dataGroup;
        }

        return null;
    }

    /**
     * @param type type of data
     * @return list by type
     */
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
