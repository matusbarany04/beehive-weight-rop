package com.buzzybees.master.users.settings;

import com.buzzybees.master.tables.User;
import com.buzzybees.master.users.UserRepository;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.Objects;

/**
 * This class in an Entity that encapsulates sql table that is storing user settings
 */
@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "user_id", unique = true)
    private long userId;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "dont_disturb")
    private boolean dontDisturb;

    @Column(name = "dont_disturb_from")
    private long dontDisturbFrom;

    @Column(name = "dont_disturb_to")
    private long dontDisturbTo;

    @Column(name = "send_notifications")
    private boolean sendNotifications;

    @Column(name = "use_user_login_mail")
    private boolean useUserLoginMail;

    @Column(name = "alt_mail")
    private String altMail;

    @Column(name = "low_battery")
    private boolean lowBattery;

    @Column(name = "battery_low_threshold")
    private int batteryLowThreshold;

    @Column(name = "high_humidity")
    private boolean highHumidity;

    @Column(name = "high_humidity_threshold")
    private int highHumidityThreshold;

    @Column(name = "low_humidity")
    private boolean lowHumidity;

    @Column(name = "low_humidity_threshold")
    private int lowHumidityThreshold;

    @Column(name = "heavy_weight")
    private boolean heavyWeight;

    @Column(name = "heavy_weight_threshold")
    private int heavyWeightThreshold;

    @Column(name = "light_weight")
    private boolean lightWeight;

    @Column(name = "light_weight_threshold")
    private int lightWeightThreshold;

    /**
     * @param json A JSON object containing all Settings fields
     */
    public Settings(JSONObject json) {
        if (json != null) {
            this.id = json.optLong("id");
            this.dontDisturbFrom = json.optLong("dont_disturb_from");
            this.dontDisturb = json.optBoolean("dont_disturb");
            this.dontDisturbTo = json.optLong("dont_disturb_to");
            this.sendNotifications = json.optBoolean("send_notifications");
            this.useUserLoginMail = json.optBoolean("use_user_login_mail");
            this.altMail = json.optString("alt_mail");
            this.lowBattery = json.optBoolean("low_battery");
            this.batteryLowThreshold = json.optInt("battery_low_threshold");
            this.highHumidity = json.optBoolean("high_humidity");
            this.highHumidityThreshold = json.optInt("high_humidity_threshold");
            this.lowHumidity = json.optBoolean("low_humidity");
            this.lowHumidityThreshold = json.optInt("low_humidity_threshold");
            this.heavyWeight = json.optBoolean("heavy_weight");
            this.heavyWeightThreshold = json.optInt("heavy_weight_threshold");
            this.lightWeight = json.optBoolean("light_weight");
            this.lightWeightThreshold = json.optInt("light_weight_threshold");
        }
    }

    public Settings() {

    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("id", this.id);
        json.put("dont_disturb_from", this.dontDisturbFrom);
        json.put("dont_disturb", this.dontDisturb);
        json.put("dont_disturb_to", this.dontDisturbTo);
        json.put("send_notifications", this.sendNotifications);
        json.put("use_user_login_mail", this.useUserLoginMail);
        json.put("alt_mail", this.altMail);
        json.put("low_battery", this.lowBattery);
        json.put("battery_low_threshold", this.batteryLowThreshold);
        json.put("high_humidity", this.highHumidity);
        json.put("high_humidity_threshold", this.highHumidityThreshold);
        json.put("low_humidity", this.lowHumidity);
        json.put("low_humidity_threshold", this.lowHumidityThreshold);
        json.put("heavy_weight", this.heavyWeight);
        json.put("heavy_weight_threshold", this.heavyWeightThreshold);
        json.put("light_weight", this.lightWeight);
        json.put("light_weight_threshold", this.lightWeightThreshold);
        return json;
    }

    public static Settings getSettingsByUser(Long userId, SettingsRepository settingsRepository) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);
        return Objects.requireNonNullElseGet(userSettings, Settings::new);
    }

    public static void createSettingsIfNonExistent(Long userId, UserRepository userRepository, SettingsRepository settingsRepository) {
        Settings userSettings = settingsRepository.getSettingsByUserId(userId);
        if(Objects.isNull(userSettings) && userRepository.findById(userId).isPresent()){
            //create new default settings
            Settings defaultSettings = new Settings();
            defaultSettings.setUserId(userId);
            settingsRepository.save(defaultSettings);
        }
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDontDisturb() {
        return dontDisturb;
    }

    public void setDontDisturb(boolean dontDisturb) {
        this.dontDisturb = dontDisturb;
    }

    public void setDontDisturbFrom(long dontDisturbFrom) {
        this.dontDisturbFrom = dontDisturbFrom;
    }

    public void setDontDisturbTo(long dontDisturbTo) {
        this.dontDisturbTo = dontDisturbTo;
    }

    public void setSendNotifications(boolean sendNotifications) {
        this.sendNotifications = sendNotifications;
    }

    public void setUseUserLoginMail(boolean useUserLoginMail) {
        this.useUserLoginMail = useUserLoginMail;
    }

    public void setAltMail(String altMail) {
        this.altMail = altMail;
    }

    public void setLowBattery(boolean lowBattery) {
        this.lowBattery = lowBattery;
    }

    public void setBatteryLowThreshold(int batteryLowThreshold) {
        this.batteryLowThreshold = batteryLowThreshold;
    }

    public void setHighHumidity(boolean highHumidity) {
        this.highHumidity = highHumidity;
    }

    public void setHighHumidityThreshold(int highHumidityThreshold) {
        this.highHumidityThreshold = highHumidityThreshold;
    }

    public void setLowHumidity(boolean lowHumidity) {
        this.lowHumidity = lowHumidity;
    }

    public void setLowHumidityThreshold(int lowHumidityThreshold) {
        this.lowHumidityThreshold = lowHumidityThreshold;
    }

    public void setHeavyWeight(boolean heavyWeight) {
        this.heavyWeight = heavyWeight;
    }

    public void setHeavyWeightThreshold(int heavyWeightThreshold) {
        this.heavyWeightThreshold = heavyWeightThreshold;
    }

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getDontDisturbFrom() {
        return dontDisturbFrom;
    }

    public long getDontDisturbTo() {
        return dontDisturbTo;
    }

    public boolean isSendNotifications() {
        return sendNotifications;
    }

    public boolean isUseUserLoginMail() {
        return useUserLoginMail;
    }

    public String getAltMail() {
        return altMail;
    }

    public boolean isLowBattery() {
        return lowBattery;
    }

    public int getBatteryLowThreshold() {
        return batteryLowThreshold;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + id +
                ", userId=" + userId +
                ", timestamp=" + timestamp +
                ", dontDisturb=" + dontDisturb +
                ", dontDisturbFrom=" + dontDisturbFrom +
                ", dontDisturbTo=" + dontDisturbTo +
                ", sendNotifications=" + sendNotifications +
                ", useUserLoginMail=" + useUserLoginMail +
                ", altMail='" + altMail + '\'' +
                ", lowBattery=" + lowBattery +
                ", batteryLowThreshold=" + batteryLowThreshold +
                ", highHumidity=" + highHumidity +
                ", highHumidityThreshold=" + highHumidityThreshold +
                ", lowHumidity=" + lowHumidity +
                ", lowHumidityThreshold=" + lowHumidityThreshold +
                ", heavyWeight=" + heavyWeight +
                ", heavyWeightThreshold=" + heavyWeightThreshold +
                ", lightWeight=" + lightWeight +
                ", lightWeightThreshold=" + lightWeightThreshold +
                '}';
    }

    public boolean isLightWeight() {
        return lightWeight;
    }

    public void setLightWeight(boolean lightWeight) {
        this.lightWeight = lightWeight;
    }

    public int getLightWeightThreshold() {
        return lightWeightThreshold;
    }

    public void setLightWeightThreshold(int lightWeightThreshold) {
        this.lightWeightThreshold = lightWeightThreshold;
    }

    public boolean isHighHumidity() {
        return highHumidity;
    }

    public int getHighHumidityThreshold() {
        return highHumidityThreshold;
    }

    public boolean isLowHumidity() {
        return lowHumidity;
    }

    public int getLowHumidityThreshold() {
        return lowHumidityThreshold;
    }

    public boolean isHeavyWeight() {
        return heavyWeight;
    }

    public int getHeavyWeightThreshold() {
        return heavyWeightThreshold;
    }
}
