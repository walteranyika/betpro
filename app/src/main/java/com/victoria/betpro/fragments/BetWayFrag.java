package com.victoria.betpro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.victoria.betpro.GraphsActivity;
import com.victoria.betpro.R;
import com.victoria.betpro.dbms.BetWayDatabase;

/**
 * Created by Walter on 8/11/2016.
 */
public class BetWayFrag extends Fragment {
    TextView tvTotalBets, tvTotalAmountBets,tvTotalWins,tvTotalAmountWon, tvTotalLosses, tvTotalAmountLost;
    BetWayDatabase db;
    Button btnGraphs;
    public BetWayFrag () {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_start, container, false);
        tvTotalBets= (TextView) rootView.findViewById(R.id.textViewTotalBets);//
        tvTotalAmountBets= (TextView) rootView.findViewById(R.id.textViewTotalAmountBets);//
        tvTotalWins= (TextView) rootView.findViewById(R.id.textViewTotalWins);//
        tvTotalAmountWon= (TextView) rootView.findViewById(R.id.textViewTotalAmountWon);//
        tvTotalLosses= (TextView) rootView.findViewById(R.id.textViewTotalLosses);
        tvTotalAmountLost= (TextView) rootView.findViewById(R.id.textViewTotalAmountLost);
        //progress= (ProgressBar) findViewById(R.id.progressBar);
        btnGraphs= (Button) rootView.findViewById(R.id.btnGraphs);
        db=new BetWayDatabase(getActivity());
        btnGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.count()>0){
                    Intent x=new Intent(getActivity(),GraphsActivity.class);
                    x.putExtra("company","betway");
                    startActivity(x);
                }else {
                    Snackbar.make(rootView, "You have no records for BetWay Bets to display on the graph", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        get_db_values(db);
        return rootView;
    }
    public void get_db_values(BetWayDatabase db) {
        tvTotalBets.setText(db.get_bet_count());
        tvTotalAmountBets.setText(db.get_amount_bets());
        tvTotalWins.setText(db.get_number_wins());
        tvTotalAmountWon.setText(db.get_win_amount());
        tvTotalLosses.setText(db.get_number_losses());
        tvTotalAmountLost.setText(db.get_loss_amount());
    }
}
