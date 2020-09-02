package com.android.votriteapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.android.votriteapp.global.GlobalClass;
import com.android.votriteapp.model.MassProp;
import java.util.ArrayList;

public class ListAdapter_MassProp extends BaseAdapter {
    MassPropositionActivity context;
    ArrayList<MassProp> arrayList;
    public ArrayList<Integer> massPropForIds = new ArrayList<>();
    public ArrayList<Integer> massPropAgainstIds = new ArrayList<>();
    public ArrayList<String> massPropAnswers;
    ArrayList<Integer> massPropIds = new ArrayList<>();
    public ArrayList<Boolean> massPropForFlag = new ArrayList<>();
    public ArrayList<Boolean> massPropAgainstFlag = new ArrayList<>();
    public boolean reviewFlag = false;

    public ListAdapter_MassProp(MassPropositionActivity _context, ArrayList<MassProp> _arrayList, ArrayList<String> _massPropAnswers) {
        this.context = _context;
        this.arrayList = _arrayList;
        this.massPropAnswers = _massPropAnswers;
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
        massPropIds = context.massPropIds;

        GlobalClass globalClass = (GlobalClass) context.getApplicationContext();
        reviewFlag = globalClass.getReviewFlag();

        if (reviewFlag) {
            massPropForFlag = globalClass.getCast_massPropForFlag();
            massPropAgainstFlag = globalClass.getCast_massPropAgainstFlag();
            massPropForIds = globalClass.getCast_massPropForIds();
            massPropAgainstIds = globalClass.getCast_massPropAgainstIds();
            massPropAnswers = globalClass.getCast_massPropAnswers();
        } else {
            for (int j = 0; j < getCount(); j++) {
                massPropForFlag.add(j, false);
                massPropAgainstFlag.add(j, false);
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
            if(!massPropAgainstFlag.get(i)) {
                votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                votAgainst.setTextColor(R.drawable.radio_flat_text_selector);
            } else {
                votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selected));
                votAgainst.setTextColor(Color.WHITE);
            }

            if(!massPropForFlag.get(i)) {
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
                if(massPropAgainstFlag.get(i)) {
                    votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                    votAgainst.setTextColor(R.drawable.radio_flat_text_selector);
                    massPropAgainstFlag.set(i, false);
                    massPropAgainstIds.remove(massPropIds.get(i));
                    massPropAnswers.set(i, "No selected");
                } else {
                    votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selected));
                    votAgainst.setTextColor(Color.WHITE);
                    massPropAgainstFlag.set(i, true);
                    massPropAgainstIds.add(massPropIds.get(i));

                    if(model.type == 1) {
                        massPropAnswers.set(i, "No");
                    } else {
                        massPropAnswers.set(i, "Against");
                    }

                    votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                    votFor.setTextColor(R.drawable.radio_flat_text_selector);
                    massPropForFlag.set(i, false);
                    massPropForIds.remove(massPropIds.get(i));
                }
            }
        });

        votFor.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(massPropForFlag.get(i)) {
                    votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                    votFor.setTextColor(R.drawable.radio_flat_text_selector);
                    massPropForFlag.set(i, false);
                    massPropForIds.remove(massPropIds.get(i));
                    massPropAnswers.set(i, "No selected");
                } else {
                    votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selected));
                    votFor.setTextColor(Color.WHITE);
                    massPropForFlag.set(i, true);
                    massPropForIds.add(massPropIds.get(i));

                    if(model.type == 1) {
                        massPropAnswers.set(i, "Yes");
                    } else {
                        massPropAnswers.set(i, "For");
                    }

                    votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                    votAgainst.setTextColor(R.drawable.radio_flat_text_selector);
                    massPropAgainstFlag.set(i, false);
                    massPropAgainstIds.remove(massPropIds.get(i));
                }
            }
        });

        if (context.flag_reset) {
            votAgainst.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
            votAgainst.setTextColor(R.drawable.radio_flat_text_selector);
            massPropAgainstFlag.set(i, false);

            votFor.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
            votFor.setTextColor(R.drawable.radio_flat_text_selector);
            massPropForFlag.set(i, false);

            massPropForIds = new ArrayList<>();
            massPropAgainstIds = new ArrayList<>();
        }

        return view;
    }
}