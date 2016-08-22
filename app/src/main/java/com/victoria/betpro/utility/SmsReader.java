package com.victoria.betpro.utility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.victoria.betpro.dbms.BetInDatabase;
import com.victoria.betpro.dbms.BetWayDatabase;
import com.victoria.betpro.dbms.MchezaDatabase;
import com.victoria.betpro.dbms.SportPesaDatabase;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Walter on 7/18/2016.
 */
public class SmsReader {
    Context context;

    public SmsReader(Context context) {
        this.context = context;
    }
    public void readSportPesasms(){
        Uri inboxUri=Uri.parse("content://sms/inbox");
        SportPesaDatabase db=new SportPesaDatabase(context);
        String[] reqCols = new String[] { "_id", "address", "body","date"};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(inboxUri,reqCols,"address='79079'",null,null);
        Log.d("TOTAL_BETS",""+cursor.getCount());
        if(cursor.moveToFirst())
        {
            while (cursor.moveToNext()){
                String unique_id = cursor.getString(0);
                String address =cursor.getString(1);
                String body = cursor.getString(2);
                long date = cursor.getLong(3);
                Date d=new Date(date);
                Calendar c=Calendar.getInstance();
                c.setTimeInMillis(date);
                //int day=c.get(Calendar.DAY_OF_MONTH);
                //int year = c.get(Calendar.YEAR);
                int month= c.get(Calendar.MONTH);
                String month_words = theMonth(month);
                //String date_string= year+"-"+month+"-"+day;
                if(body.contains("You placed BetID"))
                {
                    int start = body.indexOf("Amount: Ksh");
                    int stop = body.indexOf("POSSIBLE");
                    String amount=body.substring(start,stop);
                    amount= amount.replaceAll("[a-zA-z]+","");
                    amount=amount.replaceAll("[,]","");
                    amount=amount.replaceAll("[.]","");
                    amount=amount.replaceAll("[:]","");
                    amount=amount.trim();
                    int amt=Integer.parseInt(amount);


                    int start_bet_id = body.indexOf("BetID");
                    int stop_bet_id = body.indexOf(" on ");
                    String bet_id=body.substring(start_bet_id,stop_bet_id);
                    bet_id= bet_id.replaceAll("[a-zA-Z]+","");
                    bet_id=bet_id.replaceAll("[,]","");
                    bet_id=bet_id.replaceAll("[.]","");
                    bet_id=bet_id.replaceAll("[:]","");
                    bet_id=bet_id.trim();
                    Log.d("SAVE_WIN","AMT: " +amount+" ID:"+bet_id);
                    //String date,String bet_id, int bet_amount,int amount_won, String company, String month, String year,int time_int, String unique_id
                   // db.save_bet(date_string,bet_id,amt,0,"SportPesa",month_words,""+year,(int)date,unique_id);
                    db.save_bet(bet_id,amt,month_words,"SportPesa",date, unique_id);
                }
                else if(body.contains("placed JACKPOT") && body.contains("BetID"))
                {
                    int start = body.indexOf("Ksh");
                    int stop = body.indexOf("Your S-PESA");
                    String amount=body.substring(start,stop);
                    amount= amount.replaceAll("[a-zA-z]+","");
                    amount=amount.replaceAll("[,]","");
                    amount=amount.replaceAll("[.]","");
                    amount=amount.replaceAll("[:]","");
                    amount=amount.trim();
                    int amt=Integer.parseInt(amount);


                    int start_bet_id = body.indexOf("BetID")+6;
                    int stop_bet_id =  body.indexOf("#")-3;
                    String bet_id=body.substring(start_bet_id,stop_bet_id);
                    //bet_id= bet_id.replaceAll("[a-zA-Z]+","");
                    bet_id=bet_id.replaceAll("[,]","");
                    bet_id=bet_id.replaceAll("[.]","");
                    bet_id=bet_id.replaceAll("[:]","");
                    bet_id=bet_id.trim();
                    Log.d("BET_JACKPOT","AMT_MULTI: " +amount+" ID:"+bet_id);
                    db.save_bet(bet_id,amt,month_words,"SportPesa",date, unique_id);
                }
                else if(body.contains("BET:") && body.contains("Pos.WIN"))
                {
                    int start = body.indexOf("Ksh");
                    int stop = body.indexOf("Pos.WIN");
                    String amount=body.substring(start,stop);
                    amount= amount.replaceAll("[a-zA-z]+","");
                    amount=amount.replaceAll("[,]","");
                    amount=amount.replaceAll("[.]","");
                    amount=amount.replaceAll("[:]","");
                    amount=amount.trim();
                    int amt=Integer.parseInt(amount);


                    int start_bet_id = body.indexOf("BET");
                    int stop_bet_id = body.indexOf("BET:")+8;
                    String bet_id=body.substring(start_bet_id,stop_bet_id);
                    bet_id= bet_id.replaceAll("[a-zA-Z]+","");
                    bet_id=bet_id.replaceAll("[,]","");
                    bet_id=bet_id.replaceAll("[.]","");
                    bet_id=bet_id.replaceAll("[:]","");
                    bet_id=bet_id.trim();
                    Log.d("SAVE_BET","AMT_MULTI: " +amount+" ID:"+bet_id);
                    db.save_bet(bet_id,amt,month_words,"SportPesa",date, unique_id);
                }
                else if(body.contains("CONGRATULATIONS!") && body.contains("WON"))
                {
                    int start = body.indexOf("Ksh");
                    int stop = body.indexOf(" on ");
                    String amount=body.substring(start,stop);
                    amount= amount.replaceAll("[a-zA-z]+","");
                    amount=amount.replaceAll("[,]","");
                    amount=amount.replaceAll("[.]","");
                    amount=amount.replaceAll("[:]","");
                    amount=amount.trim();
                    int amt=Integer.parseInt(amount);
                    int start_bet_id = body.indexOf("(BetID");
                    int stop_bet_id = body.indexOf(")");
                    String bet_id=body.substring(start_bet_id,stop_bet_id);
                    bet_id= bet_id.replaceAll("[a-zA-Z]+","");
                    bet_id=bet_id.replaceAll("[)]","");
                    bet_id=bet_id.replaceAll("[(]","");
                   // bet_id=bet_id.replaceAll("[:]","");
                    bet_id=bet_id.trim();
                    Log.d("SAVE_WIN","AMT_WON: " +amount+" ID:"+bet_id);
                   //db.save_bet(date_string,bet_id,amt,0,"SportPesa",month_words,""+year,(int)date,unique_id);
                    // db.save_win(bet_id, amt);
                    db.save_win(bet_id,amt,month_words,"SportPesa",date,unique_id);

                }

            }
        }

    }

    public void readMchezaSms()
    {
        Uri inboxUri=Uri.parse("content://sms/inbox");
        MchezaDatabase db=new MchezaDatabase(context);
        String[] reqCols = new String[] { "_id", "address", "body","date"};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(inboxUri,reqCols,"address='29888'",null,null);
        Log.d("TOTAL_BETS",""+cursor.getCount());
        /* Your bet has been placed successfully.The BET ID is 2134. Your possible winnings are Kshs

       200*/
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String unique_id = cursor.getString(0);
                String address = cursor.getString(1);
                String body = cursor.getString(2);
                long date = cursor.getLong(3);
                Date d = new Date(date);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(date);
                //int day=c.get(Calendar.DAY_OF_MONTH);
                //int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                String month_words = theMonth(month);
                //String date_string= year+"-"+month+"-"+day;
                if (body.contains("Your bet has been placed successfully")) {
                    int start = body.indexOf("Kshs");
                    int stop = body.indexOf("and your");
                    String amount = body.substring(start, stop);
                    amount = amount.replaceAll("[a-zA-z]+", "");
                    amount = amount.replaceAll("[,]", "");
                    amount = amount.replaceAll("[.]", "");
                    amount = amount.replaceAll("[:]", "");
                    amount = amount.trim();
                    int amt = Integer.parseInt(amount);


                    int start_bet_id = body.indexOf("The BET ID is");
                    int stop_bet_id = body.indexOf("Your possible");
                    String bet_id = body.substring(start_bet_id, stop_bet_id);
                    bet_id = bet_id.replaceAll("[a-zA-Z]+", "");
                    bet_id = bet_id.replaceAll("[,]", "");
                    bet_id = bet_id.replaceAll("[.]", "");
                    bet_id = bet_id.replaceAll("[:]", "");
                    bet_id = bet_id.trim();
                    Log.d("SAVE_WIN", "AMT: " + amount + " ID:" + bet_id);
                    //String date,String bet_id, int bet_amount,int amount_won, String company, String month, String year,int time_int, String unique_id
                    // db.save_bet(date_string,bet_id,amt,0,"SportPesa",month_words,""+year,(int)date,unique_id);
                    db.save_bet(bet_id, amt, month_words, "Mcheza", (int) date, unique_id);
                }
            }
        }
    }

    public void readBetInSms()
    {
        Uri inboxUri=Uri.parse("content://sms/inbox");
        BetInDatabase db=new BetInDatabase(context);
        String[] reqCols = new String[] { "_id", "address", "body","date"};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(inboxUri,reqCols,"address='29456'",null,null);
        Log.d("TOTAL_BETS",""+cursor.getCount());
       /*Betin 29456

        Confirmation Bet ID 2795. Leicester city-Manchester United, X.Stake Ksh 150. Potential

        winnings 500. Good luc*/
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String unique_id = cursor.getString(0);
                String address = cursor.getString(1);
                String body = cursor.getString(2);
                long date = cursor.getLong(3);
                Date d = new Date(date);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(date);
                //int day=c.get(Calendar.DAY_OF_MONTH);
                //int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                String month_words = theMonth(month);
                //String date_string= year+"-"+month+"-"+day;
                if (body.contains("Confirmation Bet ID")) {
                    int start = body.indexOf("Ksh");
                    int stop = body.indexOf("Potential");
                    String amount = body.substring(start, stop);
                    amount = amount.replaceAll("[a-zA-z]+", "");
                    amount = amount.replaceAll("[,]", "");
                    amount = amount.replaceAll("[.]", "");
                    amount = amount.replaceAll("[:]", "");
                    amount = amount.trim();
                    int amt = Integer.parseInt(amount);


                    int start_bet_id = body.indexOf("Bet ID");
                    int stop_bet_id = body.indexOf(". ");
                    String bet_id = body.substring(start_bet_id, stop_bet_id);
                    bet_id = bet_id.replaceAll("[a-zA-Z]+", "");
                    bet_id = bet_id.replaceAll("[,]", "");
                    bet_id = bet_id.replaceAll("[.]", "");
                    bet_id = bet_id.replaceAll("[:]", "");
                    bet_id = bet_id.trim();
                    Log.d("SAVE_WIN", "AMT: " + amount + " ID:" + bet_id);
                    //String date,String bet_id, int bet_amount,int amount_won, String company, String month, String year,int time_int, String unique_id
                    // db.save_bet(date_string,bet_id,amt,0,"SportPesa",month_words,""+year,(int)date,unique_id);
                    db.save_bet(bet_id, amt, month_words, "BetIn", (int) date, unique_id);
                }
            }
        }
    }

    public void readBetWaySms()
    {
        Uri inboxUri=Uri.parse("content://sms/inbox");
        BetWayDatabase db=new BetWayDatabase(context);
        String[] reqCols = new String[] { "_id", "address", "body","date"};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(inboxUri,reqCols,"address='40444'",null,null);
        Log.d("TOTAL_BETS",""+cursor.getCount());
         /* Betway 40444

         You placed BetID 4056 Amount: Ksh250. POSSIBLE WIN Ksh412.Your betway balance:

           Ksh265. www.betway.co.ke*/
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String unique_id = cursor.getString(0);
                String address = cursor.getString(1);
                String body = cursor.getString(2);
                long date = cursor.getLong(3);
                Date d = new Date(date);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(date);
                //int day=c.get(Calendar.DAY_OF_MONTH);
                //int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                String month_words = theMonth(month);
                //String date_string= year+"-"+month+"-"+day;
                if (body.contains("You placed BetID")) {
                    int start = body.indexOf("Ksh");
                    int stop = body.indexOf("POSSIBLE");
                    String amount = body.substring(start, stop);
                    amount = amount.replaceAll("[a-zA-z]+", "");
                    amount = amount.replaceAll("[,]", "");
                    amount = amount.replaceAll("[.]", "");
                    amount = amount.replaceAll("[:]", "");
                    amount = amount.trim();
                    int amt = Integer.parseInt(amount);


                    int start_bet_id = body.indexOf("BetID");
                    int stop_bet_id = body.indexOf("Amount");
                    String bet_id = body.substring(start_bet_id, stop_bet_id);
                    bet_id = bet_id.replaceAll("[a-zA-Z]+", "");
                    bet_id = bet_id.replaceAll("[,]", "");
                    bet_id = bet_id.replaceAll("[.]", "");
                    bet_id = bet_id.replaceAll("[:]", "");
                    bet_id = bet_id.trim();
                    Log.d("SAVE_WIN", "AMT: " + amount + " ID:" + bet_id);
                    //String date,String bet_id, int bet_amount,int amount_won, String company, String month, String year,int time_int, String unique_id
                    // db.save_bet(date_string,bet_id,amt,0,"SportPesa",month_words,""+year,(int)date,unique_id);
                    db.save_bet(bet_id, amt, month_words, "BetWay", (int) date, unique_id);
                }
            }
        }
    }
    public static String theMonth(int month){
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return monthNames[month];

    }

}
