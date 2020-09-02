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

import com.android.votriteapp.global.GlobalClass;
import com.android.votriteapp.utils.Share;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter_Contest extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<Integer> candidateIds;
    private final ArrayList<String> candidateNames;
    private final ArrayList<String> partyLogos;
    private final ArrayList<String> candidatePhotos;
    private final List<String> candidateChecked;

    ArrayList<String> selCandIds= new ArrayList<>();
    ArrayList<String> selCandNames = new ArrayList<>();
    ArrayList<String> candChecked = new ArrayList<>();

    private String selCandId;
    private String selCandName;

    public ListAdapter_Contest(Activity context, ArrayList<Integer> candidateIds, ArrayList<String> candidateNames, ArrayList<String> partyLogos, ArrayList<String> candidatePhotos, List<String> candidateChecked) throws JSONException {
        super(context, R.layout.list_contest, candidateNames);
        this.context = context;
        this.candidateIds = candidateIds;
        this.candidateNames = candidateNames;
        this.candidatePhotos = candidatePhotos;
        this.partyLogos = partyLogos;
        this.candidateChecked = candidateChecked;

        GlobalClass globalVariables = (GlobalClass) context.getApplicationContext();
        JSONArray castResults = globalVariables.getCastResults();

        if(!castResults.isNull(Share.race_index)) {
            JSONObject jsonObject = castResults.getJSONObject(Share.race_index);
            if(!jsonObject.isNull("result")) {
                JSONArray cand_checked = jsonObject.getJSONArray("cand_checked");
                candidateChecked = new ArrayList<>();
                candChecked = new ArrayList<>();
                for (int j = 0; j < cand_checked.length(); j++) {
                    JSONObject jsonobject1 = cand_checked.getJSONObject(j);
                    candidateChecked.add(jsonobject1.getString("checked"));
                    candChecked = (ArrayList<String>) candidateChecked;
                }

                JSONArray cand_result = jsonObject.getJSONArray("result");
                selCandIds= new ArrayList<>();
                selCandNames = new ArrayList<>();
                for (int j = 0; j < cand_result.length(); j++) {
                    JSONObject jsonobject1 = cand_result.getJSONObject(j);
                    selCandIds.add(jsonobject1.getString("cand_ids"));
                    selCandNames.add(jsonobject1.getString("cand_names"));
                }
            }
        }
    }

    @Override
    public int getCount() {
        return candidateChecked.size();
    }

    @Override
    public String getItem(int position) {
        return candidateChecked.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.list_contest, null, true);

        if(position%2 == 0) {
            int myColor = Color.parseColor("#F1F1F1");
            @SuppressLint("CutPasteId") LinearLayout itemRecup = rowView.findViewById(R.id.item_candidate);
            itemRecup.setBackgroundColor(myColor);
        } else {
            int myColor = Color.parseColor("#E5E5E5");
            LinearLayout itemRecup = rowView.findViewById(R.id.item_candidate);
            itemRecup.setBackgroundColor(myColor);
        }

        final CheckBox checkBox = rowView.findViewById(R.id.cb_candidate);
        final TextView candidateName = rowView.findViewById(R.id.tv_candidateName);
        final ImageView partyLogo = rowView.findViewById(R.id.iv_partyLogo);
        final ImageView candidatePhoto = rowView.findViewById(R.id.iv_candidatePhoto);
        @SuppressLint("CutPasteId") LinearLayout rowCell = rowView.findViewById(R.id.item_candidate);

        candidateName.setText(candidateNames.get(position));
        if(String.valueOf(partyLogos.get(position)).equals("null")) {
            partyLogo.setImageResource(R.drawable.background_round_image);
        }
        if(String.valueOf(candidatePhotos.get(position)).equals("null")) {
            candidatePhoto.setImageResource(R.drawable.background_round_image);
        }
        if(!String.valueOf(partyLogos.get(position)).isEmpty()) {
            partyLogo.setBackground(null);
            Picasso.get().load(partyLogos.get(position)).into(partyLogo);
        }
        if(!String.valueOf(candidatePhotos.get(position)).isEmpty()) {
            candidatePhoto.setBackground(null);
            Picasso.get().load(candidatePhotos.get(position)).into(candidatePhoto);
        }

        checkBox.setChecked(Boolean.parseBoolean(candidateChecked.get(position)));

        rowCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Boolean.parseBoolean(candidateChecked.get(position))) {
                    candidateChecked.set(position, "true");
                    checkBox.setChecked(true);

                    selCandId = String.valueOf(candidateIds.get(position));
                    selCandName = candidateNames.get(position);

                    selCandIds.add(selCandId);
                    selCandNames.add(selCandName);
                } else {
                    candidateChecked.set(position, "false");
                    checkBox.setChecked(false);

                    selCandId = String.valueOf(candidateIds.get(position));
                    selCandName = candidateNames.get(position);

                    selCandIds.remove(selCandId);
                    selCandNames.remove(selCandName);
                }
                candChecked = (ArrayList<String>) candidateChecked;
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Boolean.parseBoolean(candidateChecked.get(position))) {
                    candidateChecked.set(position, "true");
                    checkBox.setChecked(true);

                    selCandId = String.valueOf(candidateIds.get(position));
                    selCandName = candidateNames.get(position);

                    selCandIds.add(selCandId);
                    selCandNames.add(selCandName);
                } else {
                    candidateChecked.set(position, "false");
                    checkBox.setChecked(false);

                    selCandId = String.valueOf(candidateIds.get(position));
                    selCandName = candidateNames.get(position);

                    selCandIds.remove(selCandId);
                    selCandNames.remove(selCandName);
                }
                candChecked = (ArrayList<String>) candidateChecked;
            }
        });

        return rowView;
    }
}
