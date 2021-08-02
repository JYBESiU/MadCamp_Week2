package com.example.thekaist;

public class UserInfo implements Comparable<UserInfo>{

    private String name;
    private String id;
    private String password;
    private String online;
    private String imgnumber;
    private Integer win;
    private Integer lose;

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public Integer getLose() {
        return lose;
    }

    public void setLose(Integer lose) {
        this.lose = lose;
    }

    public String getImgnumber() {
        return imgnumber;
    }

    public void setImgnumber(String imgnumber) {
        this.imgnumber = imgnumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    @Override
    public int compareTo(UserInfo o) {
        return this.win.compareTo(o.win);
    }
}
