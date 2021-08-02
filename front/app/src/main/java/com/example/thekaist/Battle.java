package com.example.thekaist;

public class Battle {
    private String ask;
    private String accept;

    public Battle(String ask, String accept) {
        this.ask = ask;
        this.accept = accept;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }
}
