package com.android.votriteapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class ListAdapter_Ballot extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> ballotNames;
    private final ArrayList<String> ballotAddresses;
    private final ArrayList<String> ballotEndDates;
    private final ArrayList<Integer> ballotFlags;

    public ListAdapter_Ballot(Activity context, ArrayList<String> ballotNames, ArrayList<String> ballotAddresses, ArrayList<String> ballotEndDates, ArrayList<Integer> ballotFlags) {
        super(context, R.layout.list_ballot, ballotNames);
        this.context = context;
        this.ballotNames = ballotNames;
        this.ballotAddresses = ballotAddresses;
        this.ballotEndDates = ballotEndDates;
        this.ballotFlags = ballotFlags;
    }

    @Override
    public int getCount() {
        return ballotNames.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.list_ballot, null, true);

        if(position%2 == 0) {
            int myColor = Color.parseColor("#F1F1F1");
            LinearLayout itemRecup = rowView.findViewById(R.id.item_ballot);
            itemRecup.setBackgroundColor(myColor);
        } else {
            int myColor = Color.parseColor("#E5E5E5");
            LinearLayout itemRecup = rowView.findViewById(R.id.item_ballot);
            itemRecup.setBackgroundColor(myColor);
        }

        final ImageView ballotFlag = rowView.findViewById(R.id.ivBallotFlag);
        final TextView ballotName = rowView.findViewById(R.id.tvBallotTitle);
        final TextView ballotAddress = rowView.findViewById(R.id.tvBallotAddress);
        final TextView ballotEndDate = rowView.findViewById(R.id.tvBallotEndDate);

        ballotFlag.setImageResource(ballotFlags.get(position));
        ballotName.setText(ballotNames.get(position));
        ballotAddress.setText(ballotAddresses.get(position));
        ballotEndDate.setText(String.valueOf(ballotEndDates.get(position)));

        return rowView;
    }
}