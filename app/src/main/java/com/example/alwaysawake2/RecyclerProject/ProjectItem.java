package com.example.alwaysawake2.RecyclerProject;

import android.graphics.Bitmap;

public class ProjectItem{

   private String title;
   private Bitmap image;
    int deadlineYear, deadlineMonth, deadlineDay;
    long deadMilliTimes;

    public ProjectItem(String title, Bitmap image, int deadlineYear, int deadlineMonth, int deadlineDay) {
        this.title = title;
        this.image = image;
        this.deadlineYear = deadlineYear;
        this.deadlineMonth = deadlineMonth;
        this.deadlineDay = deadlineDay;
    }

    public ProjectItem(String title, long deadMilliTimes) {
        this.title = title;
        this.deadMilliTimes = deadMilliTimes;
    }

    public ProjectItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getDeadlineYear() {
        return deadlineYear;
    }

    public void setDeadlineYear(int deadlineYear) {
        this.deadlineYear = deadlineYear;
    }

    public int getDeadlineMonth() {
        return deadlineMonth;
    }

    public void setDeadlineMonth(int deadlineMonth) {
        this.deadlineMonth = deadlineMonth;
    }

    public int getDeadlineDay() {
        return deadlineDay;
    }

    public void setDeadlineDay(int deadlineDay) {
        this.deadlineDay = deadlineDay;
    }

    public long getDeadMilliTimes() {
        return deadMilliTimes;
    }

    public void setDeadMilliTimes(long deadMilliTimes) {
        this.deadMilliTimes = deadMilliTimes;
    }


}
