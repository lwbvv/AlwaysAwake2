package com.example.alwaysawake2.RecyclerHabit;


import java.io.Serializable;

public class HabitItem  implements Serializable {
    private String title;
    private boolean check, mon, tue, wed, thu, fri, sat, sun;
    private int checkTimeHour, checkTimeMinute, hourLimit, minuteLimit;
    private double latitude, longitude, radius;
    private String preNoticeTime, placeNameOrAddress, timerStart;
    private int noticeHour, noticeMinute;
    String timerFinish;
    int alarmId;

    public HabitItem(String title, boolean check, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun, int checkTimeHour, int checkTimeMinute, int hourLimit, int minuteLimit, double latitude, double longitude, double radius, String preNoticeTime, String placeNameOrAddress, String timerStart, int noticeHour, int noticeMinute, String timerFinish, int alarmId) {
        this.title = title;
        this.check = check;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.checkTimeHour = checkTimeHour;
        this.checkTimeMinute = checkTimeMinute;
        this.hourLimit = hourLimit;
        this.minuteLimit = minuteLimit;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.preNoticeTime = preNoticeTime;
        this.placeNameOrAddress = placeNameOrAddress;
        this.timerStart = timerStart;
        this.noticeHour = noticeHour;
        this.noticeMinute = noticeMinute;
        this.timerFinish = timerFinish;
        this.alarmId = alarmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public int getCheckTimeHour() {
        return checkTimeHour;
    }

    public void setCheckTimeHour(int checkTimeHour) {
        this.checkTimeHour = checkTimeHour;
    }

    public int getCheckTimeMinute() {
        return checkTimeMinute;
    }

    public void setCheckTimeMinute(int checkTimeMinute) {
        this.checkTimeMinute = checkTimeMinute;
    }

    public int getHourLimit() {
        return hourLimit;
    }

    public void setHourLimit(int hourLimit) {
        this.hourLimit = hourLimit;
    }

    public int getMinuteLimit() {
        return minuteLimit;
    }

    public void setMinuteLimit(int minuteLimit) {
        this.minuteLimit = minuteLimit;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getPreNoticeTime() {
        return preNoticeTime;
    }

    public void setPreNoticeTime(String preNoticeTime) {
        this.preNoticeTime = preNoticeTime;
    }

    public String getPlaceNameOrAddress() {
        return placeNameOrAddress;
    }

    public void setPlaceNameOrAddress(String placeNameOrAddress) {
        this.placeNameOrAddress = placeNameOrAddress;
    }

    public String getTimerStart() {
        return timerStart;
    }

    public void setTimerStart(String timerStart) {
        this.timerStart = timerStart;
    }

    public int getNoticeHour() {
        return noticeHour;
    }

    public void setNoticeHour(int noticeHour) {
        this.noticeHour = noticeHour;
    }

    public int getNoticeMinute() {
        return noticeMinute;
    }

    public void setNoticeMinute(int noticeMinute) {
        this.noticeMinute = noticeMinute;
    }

    public String getTimerFinish() {
        return timerFinish;
    }

    public void setTimerFinish(String timerFinish) {
        this.timerFinish = timerFinish;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }
}
