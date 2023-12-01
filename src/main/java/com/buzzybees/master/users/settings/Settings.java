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

    @Column(name = "user_id", unique = true, nullable = false)
    private long userId;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "dont_disturb")
    private boolean dontDisturb = false;

    @Column(name = "dont_disturb_from")
    private long dontDisturbFrom = 555;

    @Column(name = "dont_disturb_to")
    private long dontDisturbTo = 1500;

    @Column(name = "send_notifications")
    private boolean sendNotifications = false;

    @Column(name = "use_user_login_mail")
    private boolean useUserLoginMail = true;

    @Column(name = "alt_mail")
    private String altMail = "";

    @Column(name = "low_battery")
    private boolean lowBattery = false;

    @Column(name = "battery_low_threshold")
    private int batteryLowThreshold = 20;

    @Column(name = "high_humidity")
    private boolean highHumidity = false;

    @Column(name = "high_humidity_threshold")
    private int highHumidityThreshold = 100;

    @Column(name = "low_humidity")
    private boolean lowHumidity = false;

    @Column(name = "low_humidity_threshold")
    private int lowHumidityThreshold = 30;

    @Column(name = "heavy_weight")
    private boolean heavyWeight = false;

    @Column(name = "heavy_weight_threshold")
    private int heavyWeightThreshold = 100;

    @Column(name = "light_weight")
    private boolean lightWeight = false;

    @Column(name = "light_weight_threshold")
    private int lightWeightThreshold = 0;

    public Settings() {

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
