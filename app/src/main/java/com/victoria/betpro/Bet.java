package com.victoria.betpro;

/**
 * Created by Walter on 8/5/2016.
 */
public class Bet {
    private int id;
    private String bet_id;
    private String amount;
    private String month;
    private String company;
    private  String time;
    private String unique_id;

    public Bet(int id, String bet_id, String amount, String month, String company, String time, String unique_id) {
        this.id = id;
        this.bet_id = bet_id;
        this.amount = amount;
        this.month = month;
        this.company = company;
        this.time = time;
        this.unique_id = unique_id;
    }

    public int getId() {
        return id;
    }

    public String getBet_id() {
        return bet_id;
    }

    public String getAmount() {
        return amount;
    }

    public String getMonth() {
        return month;
    }

    public String getCompany() {
        return company;
    }

    public String getTime() {
        return time;
    }

    public String getUnique_id() {
        return unique_id;
    }

    /*        "CREATE TABLE IF NOT EXISTS bets "+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "bet_id TEXT NOT NULL, " +
                "bet_amount INTEGER NOT NULL, " +
                "month TEXT NOT NULL, " +
                "company TEXT NOT NULL, " +
                "time_int INTEGER NOT NULL, " +
                "unique_id TEXT unique) ";*/
}
