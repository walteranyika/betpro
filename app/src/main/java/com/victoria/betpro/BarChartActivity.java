package com.victoria.betpro;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.victoria.betpro.dbms.BetInDatabase;
import com.victoria.betpro.dbms.BetWayDatabase;
import com.victoria.betpro.dbms.MchezaDatabase;
import com.victoria.betpro.dbms.SportPesaDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class BarChartActivity extends AppCompatActivity {
    BarChart bar;
    BetsData betsData=null;
    MenuItem item;
    long fromDefault, toDefault=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        bar= (BarChart) findViewById(R.id.barChart);
        String company=getIntent().getStringExtra("company");
       // SportPesaDatabase db = new SportPesaDatabase(this);
        //betsData=db.getBarData();
        //SportPesaDatabase db=new SportPesaDatabase(this);
        if (company.equals("sportpesa")) {
            SportPesaDatabase db = new SportPesaDatabase(this);
            betsData=db.getBarData();
        }else if(company.equals("mcheza")){
            MchezaDatabase db = new MchezaDatabase(this);
            betsData=db.getBets();
        }
        else if(company.equals("betway")){
            BetWayDatabase db = new BetWayDatabase(this);
            betsData=db.getBarData();
        }
        else if(company.equals("betin")){
            BetInDatabase db = new BetInDatabase(this);
            betsData=db.getBarData();
        }

        Log.d("COMPANY",company);
        //betsData= db.getBarData();
        if(betsData!=null)
        {
            BarDataSet barDataSet1 = new BarDataSet(betsData.getBarwins(), "Wins");
            barDataSet1.setColor(Color.parseColor("#30487a"));

            BarDataSet barDataSet2 = new BarDataSet(betsData.getBarbets(), "Bets");
            barDataSet2.setColor(Color.parseColor("#cc0000"));


            ArrayList<BarDataSet> dataSets = new ArrayList<>();

            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);

            BarData data = new BarData(betsData.getBarmonths(), dataSets);
            bar.setData(data);
            bar.setDescription("Wins Vs Bets");
        }else{
            Toast.makeText(getApplicationContext(),"Something Wrong happened",Toast.LENGTH_LONG).show();
        }
/*        ArrayList<BarEntry> group1=new ArrayList<>();
        group1.add(new BarEntry(4f,0));
        group1.add(new BarEntry(8f,1));
        group1.add(new BarEntry(8f,2));
        group1.add(new BarEntry(2f,3));
        group1.add(new BarEntry(6f,4));
        group1.add(new BarEntry(2f,5));
        group1.add(new BarEntry(3f,6));
        group1.add(new BarEntry(7f,7));

        ArrayList<BarEntry> group2=new ArrayList<>();
        group2.add(new BarEntry(2f,0));
        group2.add(new BarEntry(5f,1));
        group2.add(new BarEntry(7f,2));
        group2.add(new BarEntry(4f,3));
        group2.add(new BarEntry(5f,4));
        group2.add(new BarEntry(5f,5));
        group2.add(new BarEntry(3f,6));
        group2.add(new BarEntry(2f,7));

        BarDataSet barDataSet1=new BarDataSet(group1,"Wins");
        barDataSet1.setColor(Color.parseColor("#30487a"));

        BarDataSet barDataSet2=new BarDataSet(group2,"Bets");
        barDataSet2.setColor(Color.parseColor("#cc0000"));

        ArrayList<String> labels=new ArrayList<>();
        labels.add("JAN");labels.add("FEB");labels.add("MAR");labels.add("APR");labels.add("MAY");labels.add("JUN");labels.add("JUL");labels.add("AUG");labels.add("SEP");

        ArrayList<BarDataSet> dataSets =new ArrayList<>();

        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        BarData data=new BarData(labels,dataSets);
        bar.setData(data);*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_graph_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        this.item = item;
        if(id == R.id.action_bar_graph_from){
            showDialog(999);
        }
        else if(id == R.id.action_bar_graph_to){
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
            BetsData data=db.getBarData(fromDefault,toDefault);

            if(data.getBarmonths().size()<=0 )
            {
                Toast.makeText(getApplicationContext(),"No records for specified perioid",Toast.LENGTH_LONG).show();
            }
            else
            {
                bar.clear();
                bar.notifyDataSetChanged();
                bar.invalidate();

                BarDataSet barDataSet1 = new BarDataSet(data.getBarwins(), "Wins");
                barDataSet1.setColor(Color.parseColor("#30487a"));

                BarDataSet barDataSet2 = new BarDataSet(data.getBarbets(), "Bets");
                barDataSet2.setColor(Color.parseColor("#cc0000"));


                ArrayList<BarDataSet> dataSets = new ArrayList<>();

                dataSets.add(barDataSet1);
                dataSets.add(barDataSet2);

                BarData dataa = new BarData(betsData.getBarmonths(), dataSets);
                bar.setData(dataa);
                bar.setDescription("Wins Vs Bets");

                bar.notifyDataSetChanged();
                bar.invalidate();
            }
        }
    };

    public static String theMonth(int month){
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return monthNames[month];

    }

}
