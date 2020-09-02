package com.android.votriteapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter_Party extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> partyChecked;
    private final ArrayList<String> partyNames;
    private final ArrayList<String> partyLogos;

    public ListAdapter_Party(Activity context, ArrayList<String> partyNames, ArrayList<String> partyLogos, List<String> partyChecked) {
        super(context, R.layout.list_party, partyNames);
        this.context = context;
        this.partyNames = partyNames;
        this.partyLogos = partyLogos;
        this.partyChecked = partyChecked;
    }

    @Override
    public int getCount() {
        return partyChecked.size();
    }

    @Override
    public String getItem(int position) {
        return partyChecked.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.list_party, null, true);

        if(position%2 == 0) {
            int myColor = Color.parseColor("#F1F1F1");
            @SuppressLint("CutPasteId") LinearLayout itemRecup = rowView.findViewById(R.id.item_party);
            itemRecup.setBackgroundColor(myColor);
        } else {
            int myColor = Color.parseColor("#E5E5E5");
            LinearLayout itemRecup = rowView.findViewById(R.id.item_party);
            itemRecup.setBackgroundColor(myColor);
        }

        final CheckBox checkBox = rowView.findViewById(R.id.cb_party);
        final TextView partyName = rowView.findViewById(R.id.tv_partyName);
        final ImageView partyLogo = rowView.findViewById(R.id.ivParty);

        Picasso.get().load(partyLogos.get(position)).into(partyLogo);
        partyName.setText(partyNames.get(position));
        checkBox.setChecked(Boolean.parseBoolean(partyChecked.get(position)));
        @SuppressLint("CutPasteId") LinearLayout rowCell = rowView.findViewById(R.id.item_party);

        rowCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < partyChecked.size(); i ++){
                    if(i == position) {
                        partyChecked.set(i, "true");
                    } else {
                        partyChecked.set(i, "false");
                    }
                }

                notifyDataSetChanged();
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < partyChecked.size(); i ++){
                    if(i == position) {
                        partyChecked.set(i, "true");
                    } else {
                        partyChecked.set(i, "false");
                    }
                }

                notifyDataSetChanged();
            }
        });
        return rowView;
    }
}