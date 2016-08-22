package com.victoria.betpro;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.victoria.betpro.dbms.BetInDatabase;
import com.victoria.betpro.dbms.BetWayDatabase;
import com.victoria.betpro.dbms.MchezaDatabase;
import com.victoria.betpro.dbms.SportPesaDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class GraphsActivity extends AppCompatActivity {
    LineChart chart;
    BetsData data=null;
    String company="sportpesa";
    long fromDefault=0;
    long toDefault=0;
    MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityraphs);
        chart= (LineChart) findViewById(R.id.chart);
        company = getIntent().getStringExtra("company");



        if (company.equals("sportpesa")) {
            SportPesaDatabase db = new SportPesaDatabase(this);
            data=db.getBets();
        }else if(company.equals("mcheza")){
            MchezaDatabase db = new MchezaDatabase(this);
            data=db.getBets();
        }
        else if(company.equals("betway")){
            BetWayDatabase db = new BetWayDatabase(this);
            data=db.getBets();
        }
        else if(company.equals("betin")){
            BetInDatabase db = new BetInDatabase(this);
            data=db.getBets();
        }
        ArrayList<Entry> dataset1=data.getWin_entries();

        ArrayList<Entry> dataset_2=data.getBet_entries();
        String[] xAxis=data.getMonths().toArray(new String[data.getMonths().size()]);
        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
        LineDataSet lDataSet1 = new LineDataSet(dataset1, "Wins");
        lDataSet1.setColor(Color.GREEN);
        lDataSet1.setCircleColor(Color.GREEN);
        lines.add(lDataSet1);

        LineDataSet lDataSet2=new LineDataSet(dataset_2, "Bets");
        lDataSet2.setColor(Color.RED);
        lDataSet2.setCircleColor(Color.RED);

        lines.add(lDataSet2);

        chart.setData(new LineData(xAxis, lines));
        chart.setDescription("Wins Vs Bets");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.graphs_activity_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        this.item = item;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bar_graph) {
            Intent x=new Intent(GraphsActivity.this, BarChartActivity.class);
            x.putExtra("company",company);
            startActivity(x);
            //return true;
        }else if(id == R.id.action_bar_from){
            showDialog(999);
        }
        else if(id == R.id.action_bar_to){
            showDialog(888);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (id==999)
        {
            DatePickerDialog dialog=new DatePickerDialog(this, fromDateListener,2016,8,1);
            dialog.setTitle("From");
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000*60*60*24*365);//one year behind
            return  dialog;
        }
        else if(id==888)
        {
            DatePickerDialog dialog=new DatePickerDialog(this, toDateListener,2016,8,1);
            dialog.setTitle("To");
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
            return  dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener fromDateListener =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //do what you want with  year, month, day
            Log.d("TAREHE_FROM",year+" : "+monthOfYear+" : "+dayOfMonth);
            Calendar c=Calendar.getInstance();
            c.set(year,monthOfYear,dayOfMonth);
            long mills = c.getTimeInMillis();
            fromDefault=mills;
            Log.d("TAREHE_MILLS",""+mills);
            item.setTitle(year+"-"+theMonth(monthOfYear));
        }
    };

   private DatePickerDialog.OnDateSetListener toDateListener =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //do what you want with  year, month, day
            Log.d("TAREHE_TO",year+" : "+monthOfYear+" : "+dayOfMonth);
            Calendar c=Calendar.getInstance();
            c.set(year,monthOfYear,dayOfMonth);
            long mills = c.getTimeInMillis();
            toDefault=mills;
            Log.d("TAREHE_MILLS",""+mills);
            item.setTitle(year+"-"+theMonth(monthOfYear));
            SportPesaDatabase db=new SportPesaDatabase(getApplicationContext());
            BetsData data=db.getBets(fromDefault,toDefault);

            if(data.getMonths().size()<=0 )
            {
                Toast.makeText(getApplicationContext(),"No records for specified perioid",Toast.LENGTH_LONG).show();
            }
            else
            {
                chart.clear();
                chart.notifyDataSetChanged();
                chart.invalidate();

                ArrayList<Entry> dataset1=data.getWin_entries();

                ArrayList<Entry> dataset_2=data.getBet_entries();
                String[] xAxis=data.getMonths().toArray(new String[data.getMonths().size()]);
                ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
                LineDataSet lDataSet1 = new LineDataSet(dataset1, "Wins");
                lDataSet1.setColor(Color.GREEN);
                lDataSet1.setCircleColor(Color.GREEN);
                lines.add(lDataSet1);

                LineDataSet lDataSet2=new LineDataSet(dataset_2, "Bets");
                lDataSet2.setColor(Color.RED);
                lDataSet2.setCircleColor(Color.RED);

                lines.add(lDataSet2);

                chart.setData(new LineData(xAxis, lines));
                chart.notifyDataSetChanged();
                chart.invalidate();
                chart.setDescription("");
            }
        }
    };

    public static String theMonth(int month){
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return monthNames[month];

    }
}
