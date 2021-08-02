package com.example.thekaist;

public class history_item {
    private String result, myscr, opponentscr, opponent;

    public history_item(String result, String myscr, String opponentscr, String opponent) {
        this.result = result;
        this.myscr = myscr;
        this.opponentscr = opponentscr;
        this.opponent = opponent;
    }

    public history_item() {
        this.result = "";
        this.myscr = "";
        this.opponentscr = "";
        this.opponent = "";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMyscr() {
        return myscr;
    }

    public void setMyscr(String myscr) {
        this.myscr = myscr;
    }

    public String getOpponentscr() {
        return opponentscr;
    }

    public void setOpponentscr(String opponentscr) {
        this.opponentscr = opponentscr;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }
}
