package com.example.thekaist;

public class battleinfo {
    private Integer battleid;
    private String ask;
    private String accept;
    private String winner;
    private String loser;
    private Integer ask_scr;
    private Integer accept_scr;

    public Integer getBattleid() {
        return battleid;
    }

    public void setBattleid(Integer battleid) {
        this.battleid = battleid;
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

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLoser() {
        return loser;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }

    public Integer getAsk_scr() {
        return ask_scr;
    }

    public void setAsk_scr(Integer ask_scr) {
        this.ask_scr = ask_scr;
    }

    public Integer getAccept_scr() {
        return accept_scr;
    }

    public void setAccept_scr(Integer accept_scr) {
        this.accept_scr = accept_scr;
    }
}
