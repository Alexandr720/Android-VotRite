package com.android.votriteapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.android.votriteapp.global.GlobalClass;
import com.android.votriteapp.model.MassProp;
import java.util.ArrayList;

public class ListAdapter_Prop extends BaseAdapter {
    PropositionActivity context;
    ArrayList<MassProp> arrayList;

    ArrayList<Integer> propIds = new ArrayList<>();
    public ArrayList<Integer> propForIds = new ArrayList<>();
    public ArrayList<Integer> propAgainstIds = new ArrayList<>();
    public ArrayList<String> propAnswers;

    public ArrayList<Boolean> propForFlag = new ArrayList<>();
    public ArrayList<Boolean> propAgainstFlag = new ArrayList<>();
    public boolean reviewFlag = false;

    public ListAdapter_Prop(PropositionActivity _context, ArrayList<MassProp> _arrayList, ArrayList<String> _propAnswers) {
        this.context = _context;
        this.arrayList = _arrayList;
        this.propAnswers = _propAnswers;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getView(final int i, View view, final ViewGroup parent) {
        propIds = context.propIds;

        GlobalClass globalClass = (GlobalClass) context.getApplicationContext();
        reviewFlag = globalClass.getReviewFlag();

        if (reviewFlag) {
            propForFlag = globalClass.getCast_propForFlag();
            propAgainstFlag = globalClass.getCast_propAgainstFlag();
            propForIds = globalClass.getCast_propForIds();
            propAgainstIds = globalClass.getCast_propAgainstIds();
            propAnswers = globalClass.getCast_propAnswers();
        } else {
            for (int j = 0; j < getCount(); j++) {
                propForFlag.add(j, false);
                propAgainstFlag.add(j, false);
            }
        }

        final MassProp model = arrayList.get(i);

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_prop, null);
        }

        TextView tv_prop_title = view.findViewById(R.id.tv_prop_title);
        TextView tv_prop_text = view.findViewById(R.id.tv_prop_text);

        final Button votAgainst = view.findViewById(R.id.votAgainst);
        final Button votFor = view.findViewById(R.id.votFor);
        tv_prop_title.setText(model.title);
        tv_prop_text.setText(model.text);

        if (model.type == 1) {
            votAgainst.setText("No");
            votFor.setText("Yes");
        } else {
            votAgainst.setText("Against");
            votFor.setText("For");
        }

        if(reviewFlag) {
            if(!propAgainstFlag.get(i)) {
                votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                votAgainst.setTextColor(R.drawable.radio_flat_text_selector);
            } else {
                votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selected));
                votAgainst.setTextColor(Color.WHITE);
            }

            if(!propForFlag.get(i)) {
                votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                votFor.setTextColor(R.drawable.radio_flat_text_selector);
            } else {
                votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selected));
                votFor.setTextColor(Color.WHITE);
            }
        }

        votAgainst.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(propAgainstFlag.get(i)) {
                    votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                    votAgainst.setTextColor(R.drawable.radio_flat_text_selector);
                    propAgainstFlag.set(i, false);
                    propAgainstIds.remove(propIds.get(i));
                    propAnswers.set(i, "No selected");
                } else {
                    votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selected));
                    votAgainst.setTextColor(Color.WHITE);
                    propAgainstFlag.set(i, true);
                    propAgainstIds.add(propIds.get(i));

                    if(model.type == 1) {
                        propAnswers.set(i, "No");
                    } else {
                        propAnswers.set(i, "Against");
                    }

                    votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                    votFor.setTextColor(R.drawable.radio_flat_text_selector);
                    propForFlag.set(i, false);
                    propForIds.remove(propIds.get(i));
                }
            }
        });

        votFor.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(propForFlag.get(i)) {
                    votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                    votFor.setTextColor(R.drawable.radio_flat_text_selector);
                    propForFlag.set(i, false);
                    propForIds.remove(propIds.get(i));
                    propAnswers.set(i, "No selected");
                } else {
                    votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selected));
                    votFor.setTextColor(Color.WHITE);
                    propForFlag.set(i, true);
                    propForIds.add(propIds.get(i));
                    if(model.type == 1) {
                        propAnswers.set(i, "Yes");
                    } else {
                        propAnswers.set(i, "For");
                    }

                    votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                    votAgainst.setTextColor(R.drawable.radio_flat_text_selector);
                    propAgainstFlag.set(i, false);
                    propAgainstIds.remove(propIds.get(i));
                }
            }
        });

        if (context.flag_reset) {
            votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
            votAgainst.setTextColor(R.drawable.radio_flat_text_selector);
            propAgainstFlag.set(i, false);

            votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
            votFor.setTextColor(R.drawable.radio_flat_text_selector);
            propForFlag.set(i, false);

            propForIds = new ArrayList<>();
            propAgainstIds = new ArrayList<>();
        }

        return view;
    }
}