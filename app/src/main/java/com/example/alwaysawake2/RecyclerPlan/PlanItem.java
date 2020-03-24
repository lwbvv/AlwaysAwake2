package com.example.alwaysawake2.RecyclerPlan;

import java.io.Serializable;

public class PlanItem implements Serializable {

    private String title, bodyText, noticeTime;
    private float importance;
    private int deadlineDay, deadlineMonth, deadlineYear;
    private boolean successPLan;
    private long noticeMilliTime;
    private  int noticeTimeHour, noticeTimeMinute;
    public PlanItem(String title) {
        this.title = title;
    }

    public PlanItem(String title, String bodyText) {
        this.title = title;
        this.bodyText = bodyText;
    }

    public PlanItem() {
    }

    public PlanItem(String title, String bodyText, float importance) {
        this.title = title;
        this.bodyText = bodyText;
        this.importance = importance;
    }

    public PlanItem(String title, String bodyText, String noticeTime, float importance, int deadlineDay, int deadlineMonth, int deadlineYear, boolean successPLan, long noticeMilliTime, int noticeTimeHour, int noticeTimeMinute) {
        this.title = title;
        this.bodyText = bodyText;
        this.noticeTime = noticeTime;
        this.importance = importance;
        this.deadlineDay = deadlineDay;
        this.deadlineMonth = deadlineMonth;
        this.deadlineYear = deadlineYear;
        this.successPLan = successPLan;
        this.noticeMilliTime = noticeMilliTime;
        this.noticeTimeHour = noticeTimeHour;
        this.noticeTimeMinute = noticeTimeMinute;
    }

    public boolean isSuccessPLan() {
        return successPLan;
    }

    public void setSuccessPLan(boolean successPLan) {
        this.successPLan = successPLan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public float getImportance() {
        return importance;
    }

    public void setImportance(float importance) {
        this.importance = importance;
    }

    public int getDeadlineDay() {
        return deadlineDay;
    }

    public void setDeadlineDay(int deadlineDay) {
        this.deadlineDay = deadlineDay;
    }

    public int getDeadlineMonth() {
        return deadlineMonth;
    }

    public void setDeadlineMonth(int deadlineMonth) {
        this.deadlineMonth = deadlineMonth;
    }

    public int getDeadlineYear() {
        return deadlineYear;
    }

    public void setDeadlineYear(int deadlineYear) {
        this.deadlineYear = deadlineYear;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public long getNoticeMilliTime() {
        return noticeMilliTime;
    }

    public void setNoticeMilliTime(long noticeMilliTime) {
        this.noticeMilliTime = noticeMilliTime;
    }


    public int getNoticeTimeHour() {
        return noticeTimeHour;
    }

    public void setNoticeTimeHour(int noticeTimeHour) {
        this.noticeTimeHour = noticeTimeHour;
    }

    public int getNoticeTimeMinute() {
        return noticeTimeMinute;
    }

    public void setNoticeTimeMinute(int noticeTimeMinute) {
        this.noticeTimeMinute = noticeTimeMinute;
    }
}
