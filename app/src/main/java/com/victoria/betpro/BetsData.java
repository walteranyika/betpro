package com.victoria.betpro;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by Walter on 8/5/2016.
 */
public class BetsData {
    private ArrayList<Entry> win_entries;
    private ArrayList<Entry> bet_entries;
    private ArrayList<String> months;

    private ArrayList<BarEntry> barwins;
    private ArrayList<BarEntry> barbets;
    private ArrayList<String> barmonths;

    public BetsData(ArrayList<BarEntry> barwins, ArrayList<BarEntry> barbets,String type, ArrayList<String> barmonths) {
        this.barwins = barwins;
        this.barbets = barbets;
        this.barmonths = barmonths;

    }

    public BetsData(ArrayList<Entry> win_entries, ArrayList<Entry> bet_entries, ArrayList<String> months) {
        this.win_entries = win_entries;
        this.bet_entries = bet_entries;
        this.months=months;
    }

    public ArrayList<Entry> getWin_entries() {
        return win_entries;
    }

    public ArrayList<Entry> getBet_entries() {
        return bet_entries;
    }

    public ArrayList<String> getMonths() {
        return months;
    }

    public ArrayList<BarEntry> getBarwins() {
        return barwins;
    }

    public ArrayList<BarEntry> getBarbets() {
        return barbets;
    }

    public ArrayList<String> getBarmonths() {
        return barmonths;
    }
}
