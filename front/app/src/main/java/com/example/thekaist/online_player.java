package com.example.thekaist;

import android.app.Application;

public class online_player implements Comparable<online_player>{
    private String name;
    private String id;
    private String online;

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public online_player(String name, String id, String online) {
        this.name = name;
        this.id = id;
        this.online = online;
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

    @Override
    public int compareTo(online_player o) {
        return this.online.compareTo(o.online);

    }
}
