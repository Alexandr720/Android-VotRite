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

public class PropositionActivity extends AppCompatActivity {
    private TextView btnBack;

    private ArrayList<Integer> propForIds = new ArrayList<>();
    private ArrayList<Integer> propAgainstIds = new ArrayList<>();
    public ArrayList<Integer> propIds = new ArrayList<>();
    public ArrayList<String> propAnswers = new ArrayList<>();
    public ArrayList<Boolean> propForFlag = new ArrayList<>();
    public ArrayList<Boolean> propAgainstFlag = new ArrayList<>();

    public boolean flag_reset = false;
    TextView tvBallotName;
    MassProp mass;
    ArrayList<MassProp> array_props = new ArrayList<>();
    ListAdapter_Prop propListAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawListView();

        Button btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getString(R.string.help_prop);
                helpAlert(message);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                Share.race_index -= 2;
                globalClass.setPriFlag(true);
                Share.repeatRace(PropositionActivity.this);
            }
        });
    }

    public void drawListView() {
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();

        propIds = globalClass.getCast_propIds();

        if(propIds.size() == 0) {
            Intent intent = new Intent(PropositionActivity.this, MassPropositionActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

        setContentView(R.layout.activity_proposition);

        tvBallotName = findViewById(R.id.tv_prop_name);
        String ballotBoard = globalClass.getBallot_board();
        tvBallotName.setText(ballotBoard);

        Button btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        Button btnSkip = findViewById(R.id.btnReset);

        if(globalClass.getReviewFlag()) {
            btnSkip.setText(R.string.review_your_choice1);
        } else {
            btnSkip.setText(R.string.review_your_choice);
        }

        array_props.clear();
        for (int i = 0; i < globalClass.getCast_propNames().size(); i++) {
            mass = new MassProp(globalClass.getCast_propIds().get(i), globalClass.getCast_propNames().get(i), globalClass.getCast_propAnswerType().get(i),
                    globalClass.getCast_propTexts().get(i), globalClass.getCast_propTitles().get(i));
            array_props.add(mass);
            propAnswers.add("No selected");
        }

        listView = findViewById(R.id.listViewProps);
        propListAdapter = new ListAdapter_Prop(this, array_props, propAnswers);
        listView.setAdapter(propListAdapter);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!globalClass.getReviewFlag()) {
                    for (int i = 0; i < propIds.size(); i++) {
                        propAnswers.add("No selected");
                        propForFlag.add(false);
                        propAgainstFlag.add(false);
                    }

                    globalClass.setCast_propForIds(new ArrayList<Integer>());
                    globalClass.setCast_propAgainstIds(new ArrayList<Integer>());
                    globalClass.setCast_propAnswers(propAnswers);
                    globalClass.setCast_propForFlag(propForFlag);
                    globalClass.setCast_propAgainstFlag(propAgainstFlag);
                    globalClass.setPropFlag("prop");

                    Intent intent = new Intent(PropositionActivity.this, MassPropositionActivity.class);
                    startActivity(intent);
                } else {
                    propForIds = propListAdapter.propForIds;
                    propAgainstIds = propListAdapter.propAgainstIds;
                    propAnswers = propListAdapter.propAnswers;
                    propForFlag = propListAdapter.propForFlag;
                    propAgainstFlag = propListAdapter.propAgainstFlag;

                    globalClass.setCast_propForIds(propForIds);
                    globalClass.setCast_propAgainstIds(propAgainstIds);
                    globalClass.setCast_propAnswers(propAnswers);
                    globalClass.setCast_propForFlag(propForFlag);
                    globalClass.setCast_propAgainstFlag(propAgainstFlag);
                    globalClass.setPropFlag("prop");

                    Intent intent = new Intent(PropositionActivity.this, ReviewActivity.class);
                    startActivity(intent);
                }
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                propForIds = propListAdapter.propForIds;
                propAgainstIds = propListAdapter.propAgainstIds;
                propAnswers = propListAdapter.propAnswers;
                propForFlag = propListAdapter.propForFlag;
                propAgainstFlag = propListAdapter.propAgainstFlag;

                globalClass.setCast_propForIds(propForIds);
                globalClass.setCast_propAgainstIds(propAgainstIds);
                globalClass.setCast_propAnswers(propAnswers);
                globalClass.setCast_propForFlag(propForFlag);
                globalClass.setCast_propAgainstFlag(propAgainstFlag);
                globalClass.setPropFlag("prop");

                Intent intent = new Intent(PropositionActivity.this, MassPropositionActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        Share.race_index -= 2;
        globalClass.setPriFlag(true);
        Share.repeatRace(PropositionActivity.this);
    }

    private void helpAlert(String message) {
        final Dialog dialog = new Dialog(PropositionActivity.this, android.R.style.Theme_Dialog);
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
