package com.example.alwaysawake2.RecyclerComment;

public class CommentItem {
   private String id, comment, filePath, photoUri, nick;
   private int year, month, day;

    public CommentItem(String id, String comment, String filePath, String photoUri, String nick, int year, int month, int day) {
        this.id = id;
        this.comment = comment;
        this.filePath = filePath;
        this.photoUri = photoUri;
        this.nick = nick;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
