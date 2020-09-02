package com.android.votriteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.votriteapp.global.GlobalClass;
import com.android.votriteapp.model.MassProp;
import com.android.votriteapp.utils.Share;
import java.util.ArrayList;
import java.util.Objects;

public class MassPropositionActivity extends AppCompatActivity {

    public boolean flag_reset = false;
    Button btnNext, btnSkip, btnHelp;
    TextView tvBallotName, btnBack;

    private ArrayList<Integer> massPropForIds = new ArrayList<>();
    private ArrayList<Integer> massPropAgainstIds = new ArrayList<>();
    public ArrayList<Integer> massPropIds = new ArrayList<>();
    public ArrayList<String> massPropAnswers = new ArrayList<>();
    public ArrayList<Boolean> massPropForFlag = new ArrayList<>();
    public ArrayList<Boolean> massPropAgainstFlag = new ArrayList<>();

    private String race_type;

    MassProp mass;
    ArrayList<MassProp> array_mass = new ArrayList<>();
    ListAdapter_MassProp massListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawListView();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                ArrayList<Integer> propIds = globalClass.getCast_propIds();

                if(propIds.size() == 0) {
                    Share.race_index --;
                    if(race_type.equals("R")) {
                        Intent intent = new Intent(MassPropositionActivity.this, ContestRaceChangeActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MassPropositionActivity.this, ContestActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(MassPropositionActivity.this, PropositionActivity.class);
                    startActivity(intent);
                }
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getString(R.string.help_prop);
                helpAlert(message);
            }
        });
    }

    public void drawListView() {
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();

        massPropIds = globalClass.getCast_massPropIds();
        race_type = globalClass.getRace_type();

        if(massPropIds.size() == 0) {
            Intent intent = new Intent(MassPropositionActivity.this, ReviewActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

        setContentView(R.layout.activity_mass_proposition);

        tvBallotName = findViewById(R.id.tv_prop_name);
        String ballotBoard = globalClass.getBallot_board();
        tvBallotName.setText(ballotBoard);

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnReset);

        if(globalClass.getReviewFlag()) {
            btnSkip.setText(R.string.review_your_choice1);
        } else {
            btnSkip.setText(R.string.review_your_choice);
        }

        array_mass.clear();
        for (int i = 0; i < globalClass.getCast_massPropNames().size(); i++) {
            mass = new MassProp(globalClass.getCast_massPropIds().get(i), globalClass.getCast_massPropNames().get(i), globalClass.getCast_massPropAnswerType().get(i),
                    globalClass.getCast_massPropTexts().get(i), globalClass.getCast_massPropTitles().get(i));
            array_mass.add(mass);
            massPropAnswers.add("No selected");
        }

        ListView listView = findViewById(R.id.listView);
        massListAdapter = new ListAdapter_MassProp(this, array_mass, massPropAnswers);
        listView.setAdapter(massListAdapter);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!globalClass.getReviewFlag()) {
                    for (int i = 0; i < massPropIds.size(); i++) {
                        massPropAnswers.add("No selected");
                        massPropForFlag.add(false);
                        massPropAgainstFlag.add(false);
                    }

                    globalClass.setCast_massPropForIds(new ArrayList<Integer>());
                    globalClass.setCast_massPropAgainstIds(new ArrayList<Integer>());
                } else {
                    massPropForIds = massListAdapter.massPropForIds;
                    massPropAgainstIds = massListAdapter.massPropAgainstIds;
                    massPropAnswers = massListAdapter.massPropAnswers;
                    massPropForFlag = massListAdapter.massPropForFlag;
                    massPropAgainstFlag = massListAdapter.massPropAgainstFlag;

                    globalClass.setCast_massPropForIds(massPropForIds);
                    globalClass.setCast_massPropAgainstIds(massPropAgainstIds);
                }
                globalClass.setCast_massPropAnswers(massPropAnswers);
                globalClass.setCast_massPropForFlag(massPropForFlag);
                globalClass.setCast_massPropAgainstFlag(massPropAgainstFlag);
                globalClass.setPropFlag("massProp");

                Intent intent = new Intent(MassPropositionActivity.this, ReviewActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                massPropForIds = massListAdapter.massPropForIds;
                massPropAgainstIds = massListAdapter.massPropAgainstIds;
                massPropAnswers = massListAdapter.massPropAnswers;
                massPropForFlag = massListAdapter.massPropForFlag;
                massPropAgainstFlag = massListAdapter.massPropAgainstFlag;

                globalClass.setCast_massPropForIds(massPropForIds);
                globalClass.setCast_massPropAgainstIds(massPropAgainstIds);
                globalClass.setCast_massPropAnswers(massPropAnswers);
                globalClass.setCast_massPropForFlag(massPropForFlag);
                globalClass.setCast_massPropAgainstFlag(massPropAgainstFlag);
                globalClass.setPropFlag("massProp");

                Intent intent = new Intent(MassPropositionActivity.this, ReviewActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        ArrayList<Integer> propIds = globalClass.getCast_propIds();

        if(propIds.size() == 0) {
            Share.race_index --;
            if(race_type.equals("R")) {
                Intent intent = new Intent(MassPropositionActivity.this, ContestRaceChangeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MassPropositionActivity.this, ContestActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(MassPropositionActivity.this, PropositionActivity.class);
            startActivity(intent);
        }
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void helpAlert(String message) {
        final Dialog dialog = new Dialog(MassPropositionActivity.this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_help);
        dialog.setCanceledOnTouchOutside(true);

        TextView dialogBtnOK = dialog.findViewById(R.id.btnOK);
        TextView dialogMessage = dialog.findViewById(R.id.alertMessage);
        dialogMessage.setText(message);

        dialogBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
