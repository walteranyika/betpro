package com.victoria.betpro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.victoria.betpro.dbms.SportPesaDatabase;
import com.victoria.betpro.fragments.BetInFrag;
import com.victoria.betpro.fragments.BetWayFrag;
import com.victoria.betpro.fragments.MchezaFrag;
import com.victoria.betpro.fragments.SportPesaFrag;
import com.victoria.betpro.utility.SmsReader;

public class StartActivity extends AppCompatActivity {
    SportPesaDatabase db;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        db=new SportPesaDatabase(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        db=new SportPesaDatabase(this);
        if (!(db.count()>=1) )
        {
            new PopulateDB().execute();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Total Bet Amount is "+db.get_loss_amount(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //readSendDataAsJson();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(StartActivity.this, BarChartActivity.class));
            //return true;
        }else if(id == R.id.action_refresh)
        {
          new PopulateDB().execute();
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position==0){
                return  new SportPesaFrag();
            }
            else if (position==1){
                return  new MchezaFrag();
            }
            else if (position==2){
                return  new BetInFrag();
            }
            else if(position==3){
                return  new BetWayFrag();
            }
            return  new SportPesaFrag();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "S-Pesa";
                case 1:
                    return "Mcheza";
                case 2:
                    return "BetIn";
                case 3:
                    return "BetWay";
            }
            return null;
        }
    }
    public  class PopulateDB extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           //pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            SmsReader reader=new SmsReader(StartActivity.this);
            reader.readSportPesasms();
            reader.readBetInSms();
            reader.readBetWaySms();
            reader.readMchezaSms();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // pBar.setVisibility(View.INVISIBLE);
            db.get_number_losses();
            db.get_loss_amount();
            db.get_number_wins();
            db.get_win_amount();
        }
    }


}
