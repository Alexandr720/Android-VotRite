package com.android.votriteapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class ListAdapter_Contest_Race extends ArrayAdapter<String> {
    private final Activity context;
    ArrayList<String> candidateRaces;
    ArrayList<String> candidateNames;
    private int cand_num;

    public ListAdapter_Contest_Race(Activity context, ArrayList<String> candidateRaces, ArrayList<String> candidateNames) {
        super(context, R.layout.list_contest_race_change, candidateNames);
        this.context = context;
        this.candidateRaces = candidateRaces;
        this.candidateNames = candidateNames;
    }

    @Override
    public int getCount() {
        return candidateNames.size();
    }

    @Override
    public String getItem(int position) {
        return candidateNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.list_contest_race_change, null, true);

        if(position%2 == 0) {
            int myColor = Color.parseColor("#F1F1F1");
            LinearLayout itemRecup = rowView.findViewById(R.id.item_candidate_race_change);
            itemRecup.setBackgroundColor(myColor);
        } else {
            int myColor = Color.parseColor("#E5E5E5");
            LinearLayout itemRecup = rowView.findViewById(R.id.item_candidate_race_change);
            itemRecup.setBackgroundColor(myColor);
        }

        final TextView candidateRace = rowView.findViewById(R.id.candidateRace);
        final TextView candidateName = rowView.findViewById(R.id.candidateName);

        candidateRace.setText(candidateRaces.get(position));
        candidateName.setText(candidateNames.get(position));

        final TextView plusRace = rowView.findViewById(R.id.plusRace);
        final TextView minusRace = rowView.findViewById(R.id.minusRace);

        cand_num = candidateNames.size();
        plusRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String race = candidateRaces.get(position);
                int raceInt = Integer.parseInt(race);
                if(raceInt < cand_num) {
                    raceInt += 1;
                }
                candidateRaces.set(position, "" + raceInt);
                notifyDataSetChanged();
            }
        });

        minusRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String race = candidateRaces.get(position);
                int raceInt = Integer.parseInt(race);
                if(raceInt > 0) {
                    raceInt -= 1;
                }
                candidateRaces.set(position, "" + raceInt);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}