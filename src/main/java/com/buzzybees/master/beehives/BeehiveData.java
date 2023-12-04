package com.buzzybees.master.beehives;

import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.beehives.devices.SensorValue;
import com.buzzybees.master.tables.Status;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

record DataGroup(int from, Set<Long> sensorIds, ArrayList<Float> values) {

}

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeehiveData {

    public static final float SENSOR_NO_VALUE = -999;

    private String currentStatus;
    private final List<Integer> battery = new LinkedList<>();

//    toto musi ostat timestamp a weight!!!
    private final LinkedHashSet<Long> timestamp = new LinkedHashSet<>();
    private final List<Float> weight = new LinkedList<>();
    HashMap<String, List<DataGroup>> valueLists = new HashMap<>();

    /**
     * pushes new status to dataset
     * @param status status from database
     * @param sensorValue sensor value from database
     */
    public void push(Status status, SensorValue sensorValue) {
        boolean newStatus = timestamp.add(status.getTimestamp());

        if(newStatus) {
            currentStatus = status.getStatus();
            battery.add(status.getBattery());
            weight.add(status.getWeight());
        }

        if(sensorValue != null) {
            String listName = sensorValue.getType().getListName();
            valueLists.putIfAbsent(listName, new LinkedList<>());
            List<DataGroup> list = valueLists.get(listName);

            DataGroup group = getGroupBySensorId(sensorValue.getSensorId(), list);
            if(group != null && addToGroup(group, sensorValue)) return;

            for (DataGroup dataGroup : list) {
                if(addToGroup(dataGroup, sensorValue)) return;
            }

            ArrayList<Float> values = new ArrayList<>(Collections.singletonList(sensorValue.getValue()));
            Set<Long> sensorIds = new HashSet<>(Collections.singletonList(sensorValue.getSensorId()));
            ArrayList<Long> timestampList = new ArrayList<>(timestamp);
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
        int emptyPlaces = timestamp.size() - (dataGroup.from() + dataGroup.values().size());
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

    public String getCurrentStatus() {
        return currentStatus;
    }

    public List<Integer> getBattery() {
        return battery;
    }

    public Set<Long> getTimestamp() {
        return timestamp;
    }

    public List<Float> getWeight() {
        return weight;
    }

    @JsonAnyGetter
    public Map<String, List<DataGroup>> getValueLists() {
        return valueLists;
    }
}
