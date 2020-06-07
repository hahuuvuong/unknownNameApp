package com.example.sach.model;

public class history {
    private String userName;
    private int idTruyen;
    private int idChapter;

    public history() {
    }

    public history(String userName, int idTruyen, int idChapter) {
        this.userName = userName;
        this.idTruyen = idTruyen;
        this.idChapter = idChapter;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIdTruyen() {
        return idTruyen;
    }

    public void setIdTruyen(int idTruyen) {
        this.idTruyen = idTruyen;
    }

    public int getIdChapter() {
        return idChapter;
    }

    public void setIdChapter(int idChapter) {
        this.idChapter = idChapter;
    }
}
