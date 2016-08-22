package com.victoria.betpro.dbms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.victoria.betpro.BetsData;

import java.util.ArrayList;

public class MchezaDatabase extends SQLiteOpenHelper {

    public MchezaDatabase(Context context)
    {
        super(context, "mcheza.db", null, 2);//modify here
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //modify this query to create your own table
        String sql ="CREATE TABLE IF NOT EXISTS bets "+
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "bet_id TEXT NOT NULL, " +
                    "bet_amount INTEGER NOT NULL, " +
                    "month TEXT NOT NULL, " +
                    "company TEXT NOT NULL, " +
                    "time_int INTEGER NOT NULL, " +
                    "unique_id TEXT unique) ";

        String sql2 ="CREATE TABLE IF NOT EXISTS wins "+
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "bet_id TEXT NOT NULL, " +
                    "amount_won INTEGER NOT NULL, " +
                    "month TEXT NOT NULL, " +
                    "company TEXT NOT NULL, " +
                    "time_int INTEGER NOT NULL, " +
                    "unique_id TEXT unique) ";

        db.execSQL(sql);
        db.execSQL(sql2);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS bets";//modify to suit your table
        db.execSQL(sql);

        String sql2="DROP TABLE IF EXISTS wins";//modify to suit your table
        db.execSQL(sql2);
    }
    /**
     * Saves an item into sqlite database
     */
    public void save_bet(String bet_id, int bet_amount, String month,String company, int time_int, String unique_id)//Modify here to suit what you want to save
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("bet_id", bet_id);//modify here
        values.put("bet_amount",bet_amount);//modify here
        values.put("month", month);//modify here
        values.put("time_int", time_int);//modify here
        values.put("company", company);//modify here
        values.put("unique_id",unique_id);
        try
        {
            db.insert("bets", null, values);//modify here
        }
        catch (SQLiteConstraintException e)
        {
            Log.e("BET", "Error while saving "+e.getMessage());
        }
        Log.d("BET_BET_SAVING ", "SAVE "+bet_amount);
    }
    public void save_win(String bet_id, int amount_won, String month,String company, int time_int, String unique_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("bet_id", bet_id);//modify here
        values.put("amount_won",amount_won);//modify here
        values.put("month", month);//modify here
        values.put("time_int", time_int);//modify here
        values.put("company", company);//modify here
        values.put("unique_id",unique_id);
        try
        {
            db.insert("wins", null, values);//modify here
        }
        catch (SQLiteConstraintException e)
        {
            Log.e("BET_ERROR", "Error while saving win "+e.getMessage());
        }
        Log.d("BET_WIN_SAVING ", "SAVE "+amount_won);
    }
    /**
     * Counts All  Records in sqlite
     * @return
     */
    public int count()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String sql="SELECT COUNT(*) FROM bets";//modify here
        Cursor cursor =db.rawQuery(sql, null);
        int x=0;
        if (cursor.moveToFirst()) {
            x = (cursor.getString(0)==null)? 0 :cursor.getInt(0);
            Log.d("BET_COUNT",""+x);
        }

        return x;

    }
    //(cursor.getString(0)==null)? "0":cursor.getString(0);
    public void retrieve()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String sql="SELECT * FROM bets";//modify here to reflect your table
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst())
        {
            do
            {
                ////modify here to ensure that it matches your data from the table
                int id=cursor.getInt(0);
                int bet_amount=cursor.getInt(2);
                //int amount_won=cursor.getInt(3);
                //Bet x=new Bet(id,date,bet_id,bet_amount,amount_won,company,month,year,time_int,unique_id);
                Log.d("BETS_DATA", id+" : "+bet_amount);
               // data.add(x);
            }while(cursor.moveToNext());
        }
    }
    public String get_bet_count()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String sql = "SELECT count(*) from bets";
        Cursor cursor = db.rawQuery(sql,null);
        String number = "0";
        if(cursor.moveToFirst())
        {
            number=cursor.getString(0);
            Log.d("BET_COUNT",": "+number);
        }
        return  number;
    }
    public  String get_amount_bets()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String sql = "SELECT SUM(bet_amount) from bets";
        Cursor cursor = db.rawQuery(sql,null);
        String number = "0";
        if(cursor.moveToFirst())
        {
            number=(cursor.getString(0)==null)? "0":cursor.getString(0);
            Log.d("BET_TOTAL",": "+number);
        }
        return  number;
    }
    public  String get_win_amount()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String sql = "SELECT SUM(amount_won) from wins";
        Cursor cursor = db.rawQuery(sql,null);
        String number = "0";
        if(cursor.moveToFirst())
        {
            number=(cursor.getString(0)==null)? "0":cursor.getString(0);
            Log.d("BETS_WIN_TOTAL",": "+number);
        }
        return  number;
    }
    public String get_number_wins()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String sql = "SELECT COUNT(*) from wins";//SELECT COUNT(amount_won) from sports WHERE amount_won > 0
        Cursor cursor = db.rawQuery(sql,null);
        String number = "0";
        if(cursor.moveToFirst())
        {
            number=cursor.getString(0);
            Log.d("BETS_WIN_COUNT",": "+number);
        }
        return  number;
    }
    public  String get_loss_amount()//to to joins
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String sql = "SELECT  SUM(bets.bet_amount) FROM bets WHERE bets.bet_id NOT IN (SELECT bet_id FROM wins)";
        Cursor cursor = db.rawQuery(sql,null);
        String number = "0";
        if(cursor.moveToFirst())
        {
            number=(cursor.getString(0)==null)? "0":cursor.getString(0);
            Log.d("BETS_LOSS_AMOUNT",": "+number);
        }
        get_win_amount();
        get_number_wins();
        get_bet_count();

        return  number;
    }
    public String get_number_losses()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String sql = "SELECT COUNT(bets.bet_id) FROM bets WHERE bets.bet_id NOT IN (SELECT bet_id FROM wins)";
        Cursor cursor = db.rawQuery(sql,null);
        String number = "0";
        if(cursor.moveToFirst())
        {
            number=cursor.getString(0);
            Log.d("BET_LOSS_COUNT",": "+number);
        }
        return  number;
    }
    public BetsData getBets(){
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<Entry> win_entries=new ArrayList<>();
        ArrayList<Entry> bet_entries=new ArrayList<>();
        ArrayList<String> months=new ArrayList<>();

        String sql = "SELECT month, SUM(bet_amount) FROM bets GROUP BY month order by _id desc";
        Cursor cursor = db.rawQuery(sql,null);
        int x=0;
        if(cursor.moveToFirst()){
            while(cursor.moveToNext())
            {
                String month= cursor.getString(0);
                int amount_bet =cursor.getInt(1);
                //int amount_won =cursor.getInt(2);
                //Entry win=new Entry((float)amount_won, x);
                Entry bet=new Entry((float)amount_bet,x);
                //win_entries.add(win);
                bet_entries.add(bet);
                months.add(month);
                x++;
            }
        }


        String sql2 = "SELECT SUM(amount_won) FROM wins GROUP BY month order by _id desc";
        Cursor cursor2 = db.rawQuery(sql2,null);
        Log.d("WIN_COUNT",""+cursor2.getCount());
        int y=0;
        if(cursor2.moveToFirst())
        {
            while(cursor2.moveToNext())
            {
                //String month= cursor2.getString(0);

                int amount_won =cursor2.getInt(0);
                Log.d("BETS_WIN_AMOUNT", " : "+amount_won);
                Entry win=new Entry((float)amount_won, y);
                win_entries.add(win);
                y++;
            }
        }
        BetsData data=new BetsData(win_entries,bet_entries,months);
        return data;
    }
    /*SELECT bets.bet_id, bets.bet_amount,bets.month,wins.amount_won FROM bets INNER JOIN wins ON  bets.bet_id=wins.bet_id*/
    /*SELECT bets.bet_id, bets.bet_amount,bets.month FROM bets WHERE bets.bet_id NOT IN (SELECT bet_id FROM wins)*/
    public  BetsData getBarData()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<BarEntry> winbar=new ArrayList<>();
        ArrayList<BarEntry> betbar=new ArrayList<>();
        ArrayList<String> months=new ArrayList<>();

        String sql = "SELECT month, SUM(bet_amount) FROM bets GROUP BY month order by _id desc";
        Cursor cursor = db.rawQuery(sql,null);
        int x=0;
        if(cursor.moveToFirst()){
            while(cursor.moveToNext())
            {
                String month= cursor.getString(0);
                int amount_bet =cursor.getInt(1);
                //int amount_won =cursor.getInt(2);
                //Entry win=new Entry((float)amount_won, x);
                BarEntry bet=new BarEntry((float)amount_bet,x);
                //win_entries.add(win);
                betbar.add(bet);
                months.add(month);
                x++;
            }
        }


        String sql2 = "SELECT SUM(amount_won) FROM wins GROUP BY month order by _id desc";
        Cursor cursor2 = db.rawQuery(sql2,null);
        Log.d("WIN_COUNT",""+cursor2.getCount());
        int y=0;
        if(cursor2.moveToFirst())
        {
            while(cursor2.moveToNext())
            {
                //String month= cursor2.getString(0);
                int amount_won =cursor2.getInt(0);
                Log.d("BETS_WIN_AMOUNT", " : "+amount_won);
                BarEntry win=new BarEntry((float)amount_won, y);
                winbar.add(win);
                y++;
            }
        }
        BetsData data=new BetsData(winbar,betbar,"BarGraph",months);
        return data;
    }
}
