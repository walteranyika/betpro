package com.victoria.betpro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.victoria.betpro.dbms.SportPesaDatabase;

import java.util.Calendar;

public class SmsReceiver extends BroadcastReceiver {
    public SmsReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        Object[] pdus=(Object[])bundle.get("pdus");
        SmsMessage message=SmsMessage.createFromPdu((byte[])pdus[0]);
        Log.d("BET_MESSAGE", message.getDisplayMessageBody());


        String body=message.getDisplayMessageBody();
        String origin=message.getDisplayOriginatingAddress();
        long date=message.getTimestampMillis();

        int rand = 1000+(int)(Math.random()*20000);//unique ID

        if (origin.equals("79079"))
        {
            Calendar c=Calendar.getInstance();
            c.setTimeInMillis(date);
            int month= c.get(Calendar.MONTH);
            String month_words = theMonth(month);
            SportPesaDatabase db=new SportPesaDatabase(context);
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
                bet_id=bet_id.replaceAll("[a-zA-Z]+","");
                bet_id=bet_id.replaceAll("[,]","");
                bet_id=bet_id.replaceAll("[.]","");
                bet_id=bet_id.replaceAll("[:]","");
                bet_id=bet_id.trim();
                Log.d("SAVE_WIN","AMT: " +amount+" ID:"+bet_id);
                //String date,String bet_id, int bet_amount,int amount_won, String company, String month, String year,int time_int, String unique_id
                // db.save_bet(date_string,bet_id,amt,0,"SportPesa",month_words,""+year,(int)date,unique_id);
                db.save_bet(bet_id,amt,month_words,"SportPesa",(int)date, ""+rand);
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
                db.save_bet(bet_id,amt,month_words,"SportPesa",(int)date, ""+rand);
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
                db.save_win(bet_id,amt,month_words,"SportPesa",(int)date,""+rand);
            }
        }

    }
    public static String theMonth(int month){
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return monthNames[month];

    }
}
