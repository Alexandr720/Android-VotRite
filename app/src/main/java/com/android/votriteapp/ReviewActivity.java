package com.android.votriteapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.votriteapp.apiHelper.ReqConst;
import com.android.votriteapp.global.GlobalClass;
import com.android.votriteapp.utils.Share;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ReviewActivity extends AppCompatActivity {

    private String pincode;
    private int ballot_id, race_id, race_num;
    private ArrayList<String> cast_prop_names = new ArrayList<>();
    private ArrayList<String> cast_prop_answers = new ArrayList<>();
    private ArrayList<String> cast_mass_prop_names = new ArrayList<>();
    private ArrayList<String> cast_mass_prop_answers = new ArrayList<>();
    JSONArray castRaceCandidates = new JSONArray();
    JSONArray castPCandidates = new JSONArray();
    JSONArray castParties = new JSONArray();
    ArrayList<String> castPriRaceIndexs = new ArrayList<>();
    ArrayList<String> castPriRaceNames = new ArrayList<>();
    ArrayList<String> castPriPartyNames = new ArrayList<>();
    ArrayList<String> castPriRaceIds = new ArrayList<>();
    ArrayList<String> castPriPartyIds = new ArrayList<>();
    ArrayList<String> castRRaceIndexs = new ArrayList<>();
    ArrayList<String> castRRaceIds = new ArrayList<>();
    ArrayList<String> castRRaceNames = new ArrayList<>();
    ArrayList<String> castRCastIndexs = new ArrayList<>();
    ArrayList<String> castRCastIds = new ArrayList<>();
    ArrayList<String> castRCastNames = new ArrayList<>();
    ArrayList<String> castRCastRaces = new ArrayList<>();

    TextView ballot_board;
    TextView ballot_client;
    TextView ballot_election;
    TextView voted_date;
    String board;
    String client;
    String election;
    ArrayList<Integer> cast_prop_ids;
    ArrayList<Integer> cast_massProp_ids;

    private ArrayList<Integer> cast_prop_for_ids = new ArrayList<>();
    private ArrayList<Integer> cast_prop_against_ids = new ArrayList<>();
    private ArrayList<Integer> cast_mass_prop_for_ids = new ArrayList<>();
    private ArrayList<Integer> cast_mass_prop_against_ids = new ArrayList<>();

    private JSONArray castResults = new JSONArray();

//    private Bitmap reviewBitmap;
//    private LinearLayout reviewLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        TextView btnBack = findViewById(R.id.btnBack);
        Button btnReset = findViewById(R.id.btnReset);
        Button btnAccept = findViewById(R.id.btnAccept);
        Button btnHelp = findViewById(R.id.btnHelp);
//        Button btnPrint = findViewById(R.id.btnPrint);

        ballot_board = findViewById(R.id.ballot_name);
        ballot_client = findViewById(R.id.ballot_client);
        ballot_election = findViewById(R.id.ballot_election);
        voted_date = findViewById(R.id.voted_date);
//        reviewLayout = findViewById(R.id.reviewLayout);

        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        ballot_id = globalClass.getBallot_id();
        race_id = globalClass.getRace_id();
        pincode = globalClass.getPin_code();
        board = globalClass.getBallot_election();
        client = globalClass.getBallot_client();
        election = globalClass.getBallot_election();
        pincode = globalClass.getPin_code();
        cast_prop_ids = globalClass.getCast_propIds();
        cast_prop_names = globalClass.getCast_propNames();
        cast_prop_for_ids = globalClass.getCast_propForIds();
        cast_prop_against_ids = globalClass.getCast_propAgainstIds();
        cast_prop_answers = globalClass.getCast_propAnswers();
        cast_massProp_ids = globalClass.getCast_massPropIds();
        cast_mass_prop_names = globalClass.getCast_massPropNames();
        cast_mass_prop_for_ids = globalClass.getCast_massPropForIds();
        cast_mass_prop_answers = globalClass.getCast_massPropAnswers();
        cast_mass_prop_against_ids = globalClass.getCast_massPropAgainstIds();

        castResults = globalClass.getCastResults();

        globalClass.setReviewFlag(true);

        try {
            drawReview();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                race_num = globalClass.getRace_num();

//                if(Share.race_index == race_num) {
//                    if(Share.flag) {
                        ArrayList<Integer> massPropIds = globalClass.getCast_massPropIds();
                        ArrayList<Integer> propIds = globalClass.getCast_propIds();

                        String propFlag = globalClass.getPropFlag();

                        if (propFlag.equals("massProp")) {
                            if (massPropIds.size() != 0) {
                                Intent intent = new Intent(ReviewActivity.this, MassPropositionActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                            } else if (propIds.size() != 0) {
                                Intent intent = new Intent(ReviewActivity.this, PropositionActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                            } else {
                                Share.race_index--;
                                Share.switch_race(ReviewActivity.this);
                            }
                        } else {
                            if (propIds.size() != 0) {
                                Intent intent = new Intent(ReviewActivity.this, PropositionActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                            } else {
                                Share.race_index--;
                                Share.switch_race(ReviewActivity.this);
                            }
                        }
//                    } else {
//                        Share.race_index--;
//                        Share.switch_race(ReviewActivity.this);
//                    }
//                } else {
//                    Share.race_index--;
//                    Share.switch_race(ReviewActivity.this);
//                }
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getString(R.string.help_review);
                helpAlert(message);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                globalClass.setCastResults(new JSONArray());
                globalClass.setCast_candRace(new ArrayList<String>());
                globalClass.setCast_propIds(new ArrayList<Integer>());
                globalClass.setCast_propForIds(new ArrayList<Integer>());
                globalClass.setCast_propAgainstIds(new ArrayList<Integer>());
                globalClass.setCast_propAnswers(new ArrayList<String>());
                globalClass.setCast_massPropIds(new ArrayList<Integer>());
                globalClass.setCast_massPropForIds(new ArrayList<Integer>());
                globalClass.setCast_massPropAgainstIds(new ArrayList<Integer>());
                globalClass.setCast_massPropAnswers(new ArrayList<String>());
                globalClass.setPriFlag(false);
                globalClass.setReviewFlag(false);
                Share.race_index = 0;
                Share.switch_race(ReviewActivity.this);
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alertTitle, alertMessage, btnOK, btnNO;
                alertTitle = getString(R.string.confirm);
                alertMessage = getString(R.string.confirmCastParty);
                btnOK = getString(R.string.yes);
                btnNO = getString(R.string.no);
                confirmCastParty(alertTitle, alertMessage, btnOK, btnNO);
            }
        });

//  Print PDF Action
//        btnPrint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reviewBitmap = loadBitmapFromView(reviewLayout, reviewLayout.getWidth(), reviewLayout.getHeight());
//                printReview();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        race_num = globalClass.getRace_num();

//        if(Share.race_index == race_num) {
//            if(Share.flag) {
                ArrayList<Integer> massPropIds = globalClass.getCast_massPropIds();
                ArrayList<Integer> propIds = globalClass.getCast_propIds();

                String propFlag = globalClass.getPropFlag();

                if (propFlag.equals("massProp")) {
                    if (massPropIds.size() != 0) {
                        Intent intent = new Intent(ReviewActivity.this, MassPropositionActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    } else if (propIds.size() != 0) {
                        Intent intent = new Intent(ReviewActivity.this, PropositionActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    } else {
                        Share.race_index--;
                        Share.switch_race(ReviewActivity.this);
                    }
                } else {
                    if (propIds.size() != 0) {
                        Intent intent = new Intent(ReviewActivity.this, PropositionActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    } else {
                        Share.race_index--;
                        Share.switch_race(ReviewActivity.this);
                    }
                }
//            } else {
//                Share.race_index--;
//                Share.switch_race(ReviewActivity.this);
//            }
//        } else {
//            Share.race_index--;
//            Share.switch_race(ReviewActivity.this);
//        }
    }

    private void helpAlert(String message) {
        final Dialog dialog = new Dialog(ReviewActivity.this, android.R.style.Theme_Dialog);
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

    @SuppressLint("InflateParams")
    private void drawReview() throws JSONException {
        Date date = new Date();
        String votedDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);

        ballot_board.setText(board);
        ballot_client.setText(client);
        ballot_election.setText(election);
        voted_date.setText(votedDate);

        int length = castResults.length();
        for (int i = 0; i < length; i ++) {
            JSONObject jsonObject = castResults.getJSONObject(i);
            String race_type = jsonObject.getString("race_type");

            switch (race_type) {
                case "P":
                    castParties.put(jsonObject);
                    castPCandidates.put(jsonObject);
                    break;
                case "S":
                    castPCandidates.put(jsonObject);
                    break;
                case "R":
                    castRaceCandidates.put(jsonObject);
                    break;
            }
        }

        LinearLayout ll_review_parties = findViewById(R.id.ll_review_parties);
        if(castParties.length() == 0) {
            LinearLayout ll_parties = findViewById(R.id.ll_parties);
            ll_parties.removeAllViews();
        } else {
            for (int i = 0; i < castParties.length(); i++) {
                try {
                    castPriRaceIndexs = new ArrayList<>();
                    castPriRaceNames = new ArrayList<>();
                    castPriPartyNames = new ArrayList<>();

                    JSONObject jsonobject = castParties.getJSONObject(i);

                    castPriRaceIndexs.add(jsonobject.getString("race_index"));
                    castPriRaceNames.add(jsonobject.getString("race_name"));

                    for (int j = 0; j < castPriRaceIndexs.size(); j ++) {
                        @SuppressLint("InflateParams") LinearLayout oneParty = (LinearLayout) getLayoutInflater().inflate(R.layout.item_review_party, null);
                        final TextView raceName = oneParty.findViewById(R.id.tv_cast_race_name);
                        TextView partyName = oneParty.findViewById(R.id.tv_cast_party_name);

                        raceName.setText(castPriRaceNames.get(j));
                        raceName.setId(Integer.parseInt(castPriRaceIndexs.get(j)));
                        raceName.setTextColor(Color.parseColor("#6B5CE4"));
                        raceName.setPadding(16, 0, 16, 2);

                        if(!jsonobject.isNull("party_name")) {
                            castPriPartyNames.add(jsonobject.getString("party_name"));
                        } else {
                            castPriPartyNames.add("No selected");
                        }
                        partyName.setText(castPriPartyNames.get(j));

                        ll_review_parties.addView(oneParty);

                        raceName.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void onClick(View v) {
                                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                                globalClass.setPriFlag(false);
                                Share.race_index = raceName.getId()-1;
                                Share.repeatRace(ReviewActivity.this);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        LinearLayout ll_review_cands = findViewById(R.id.ll_review_cands);
        if(castPCandidates.length() == 0 ) {
            ll_review_cands.removeAllViews();
        } else {
            for (int i = 0; i < castPCandidates.length(); i++) {
                try {
                    castRRaceIndexs = new ArrayList<>();
                    castRRaceIds = new ArrayList<>();
                    castRRaceNames = new ArrayList<>();
                    castRCastIds = new ArrayList<>();
                    castRCastNames = new ArrayList<>();

                    JSONObject jsonobject = castPCandidates.getJSONObject(i);

                    castRRaceIndexs.add(jsonobject.getString("race_index"));
                    castRRaceIds.add(jsonobject.getString("race_id"));
                    castRRaceNames.add(jsonobject.getString("race_name"));

                    for (int j = 0; j < castRRaceNames.size(); j ++) {
                        @SuppressLint("InflateParams") LinearLayout oneCand = (LinearLayout) getLayoutInflater().inflate(R.layout.item_review_cand, null);
                        final TextView raceName = oneCand.findViewById(R.id.tv_cast_race_name);
                        raceName.setId(Integer.parseInt(castRRaceIndexs.get(j)));
                        raceName.setTextColor(Color.parseColor("#6B5CE4"));
                        raceName.setPadding(16, 0, 16, 2);
                        raceName.setText(castRRaceNames.get(j));

                        @SuppressLint("InflateParams") LinearLayout ll_result_Rcands;
                        TextView candName;
                        TextView candRace;

                        if (jsonobject.isNull("result")) {
                            ll_result_Rcands = (LinearLayout) getLayoutInflater().inflate(R.layout.item_review_rcand_result, null);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(10, 0, 0, 0);
                            ll_result_Rcands.setLayoutParams(params);
                            candName = ll_result_Rcands.findViewById(R.id.tv_cast_cand_name);
                            candRace = ll_result_Rcands.findViewById(R.id.tv_cast_cand_race);

                            candName.setText(R.string.no_sel_cand);
                            candRace.setText("");
                            oneCand.addView(ll_result_Rcands);
                        } else {
                            JSONArray rcand_result = jsonobject.getJSONArray("result");
                            for (int k = 0; k < rcand_result.length(); k ++) {
                                JSONObject jsonobject1 = rcand_result.getJSONObject(k);
                                castRCastIds.add(jsonobject1.getString("cand_ids"));
                                castRCastNames.add(jsonobject1.getString("cand_names"));
                            }

                            for (int k = 0; k < castRCastNames.size(); k++) {
                                ll_result_Rcands = (LinearLayout) getLayoutInflater().inflate(R.layout.item_review_rcand_result, null);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(10, 0, 0, 0);
                                ll_result_Rcands.setLayoutParams(params);

                                candName = ll_result_Rcands.findViewById(R.id.tv_cast_cand_name);
                                candRace = ll_result_Rcands.findViewById(R.id.tv_cast_cand_race);

                                candName.setText(castRCastNames.get(k));
                                candRace.setText("");
                                oneCand.addView(ll_result_Rcands);
                            }
                        }
                        ll_review_cands.addView(oneCand);

                        raceName.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void onClick(View v) {
                                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                                Share.race_index = raceName.getId()-1;
                                globalClass.setPriFlag(true);
                                Share.repeatRace(ReviewActivity.this);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        LinearLayout ll_review_Rcands = findViewById(R.id.ll_review_Rcands);
        if (castRaceCandidates.length() == 0) {
            ll_review_Rcands.removeAllViews();
        } else {
            for (int i = 0; i < castRaceCandidates.length(); i++) {
                try {
                    castRCastIndexs = new ArrayList<>();
                    castRRaceIds = new ArrayList<>();
                    castRRaceNames = new ArrayList<>();
                    castRCastIds = new ArrayList<>();
                    castRCastNames = new ArrayList<>();
                    castRCastRaces = new ArrayList<>();

                    JSONObject jsonobject = castRaceCandidates.getJSONObject(i);

                    castRCastIndexs.add(jsonobject.getString("race_index"));
                    castRRaceIds.add(jsonobject.getString("race_id"));
                    castRRaceNames.add(jsonobject.getString("race_name"));

                    for (int j = 0; j < castRRaceNames.size(); j ++) {
                        @SuppressLint("InflateParams") LinearLayout oneCand = (LinearLayout) getLayoutInflater().inflate(R.layout.item_review_rcand, null);
                        final TextView raceName = oneCand.findViewById(R.id.tv_cast_race_name);
                        raceName.setId(Integer.parseInt(castRCastIndexs.get(j)));
                        raceName.setText(castRRaceNames.get(j));
                        raceName.setTextColor(Color.parseColor("#6B5CE4"));
                        raceName.setPadding(16, 0, 16, 2);

                        if (!jsonobject.isNull("result")) {
                            JSONArray rcand_result = jsonobject.getJSONArray("result");
                            for (int k = 0; k < rcand_result.length(); k ++) {
                                JSONObject jsonobject1 = rcand_result.getJSONObject(k);
                                castRCastIds.add(jsonobject1.getString("cand_ids"));
                                castRCastNames.add(jsonobject1.getString("cand_names"));
                                castRCastRaces.add(jsonobject1.getString("cand_races"));
                            }

                            ArrayList<String> castRCastIds1 = new ArrayList<>();
                            ArrayList<String> castRCastNames1 = new ArrayList<>();
                            ArrayList<String> castRCastRaces1 = new ArrayList<>();

                            for (int k = 0; k < castRCastIds.size(); k ++) {
                                if (!castRCastRaces.get(k).equals("0")) {
                                    castRCastIds1.add(castRCastIds.get(k));
                                    castRCastNames1.add(castRCastNames.get(k));
                                    castRCastRaces1.add(castRCastRaces.get(k));
                                }
                            }

                            castRCastIds = castRCastIds1;
                            castRCastNames = castRCastNames1;
                            castRCastRaces = castRCastRaces1;

                            for (int k = 0; k < castRCastNames.size(); k++) {
                                @SuppressLint("InflateParams") LinearLayout ll_result_Rcands = (LinearLayout) getLayoutInflater().inflate(R.layout.item_review_rcand_result, null);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(10, 0, 0, 0);
                                ll_result_Rcands.setLayoutParams(params);
                                TextView candName = ll_result_Rcands.findViewById(R.id.tv_cast_cand_name);
                                TextView candRace = ll_result_Rcands.findViewById(R.id.tv_cast_cand_race);

                                candName.setText(castRCastNames.get(k));
                                candRace.setText(castRCastRaces.get(k));
                                oneCand.addView(ll_result_Rcands);
                            }
                        } else {
                            @SuppressLint("InflateParams") LinearLayout ll_result_Rcands = (LinearLayout) getLayoutInflater().inflate(R.layout.item_review_rcand_result, null);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(10, 0, 0, 0);
                            ll_result_Rcands.setLayoutParams(params);
                            TextView candName = ll_result_Rcands.findViewById(R.id.tv_cast_cand_name);
                            TextView candRace = ll_result_Rcands.findViewById(R.id.tv_cast_cand_race);

                            candName.setText(R.string.no_sel_cand);
                            candRace.setText("");
                            oneCand.addView(ll_result_Rcands);
                        }
                        ll_review_Rcands.addView(oneCand);

                        raceName.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void onClick(View v) {
                                Share.race_index = raceName.getId()-1;
                                Share.repeatRace(ReviewActivity.this);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if((castRaceCandidates.length() == 0) && (castPCandidates.length() == 0)) {
            LinearLayout ll_cands = findViewById(R.id.ll_cast_cands);
            ll_cands.removeAllViews();
        }

        LinearLayout ll_review_props = findViewById(R.id.ll_review_props);
        if((cast_prop_ids.size() == 0) || (cast_prop_answers.size() == 0)) {
            LinearLayout ll_props = findViewById(R.id.ll_props);
            ll_props.removeAllViews();
        } else {
            for (int i = 0; i < cast_prop_ids.size(); i ++) {
                @SuppressLint("InflateParams") LinearLayout oneProp = (LinearLayout) getLayoutInflater().inflate(R.layout.item_review_prop, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 10);
                oneProp.setLayoutParams(params);

                TextView propName = oneProp.findViewById(R.id.tv_prop_name);
                propName.setPadding(16, 0, 16, 0);
                propName.setText(String.valueOf(cast_prop_names.get(i)));
                propName.setTextColor(Color.parseColor("#6B5CE4"));

                TextView propAnswer = oneProp.findViewById(R.id.tv_prop_answer);
                propAnswer.setText(String.valueOf(cast_prop_answers.get(i)));

                ll_review_props.addView(oneProp);

                propName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReviewActivity.this, PropositionActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    }
                });
            }
        }

        LinearLayout ll_review_massProps = findViewById(R.id.ll_review_massProps);
        if((cast_massProp_ids.size() == 0) || (cast_mass_prop_answers.size() == 0)) {
            LinearLayout ll_massProps = findViewById(R.id.ll_massProps);
            ll_massProps.removeAllViews();
        } else {
            for (int i = 0; i < cast_massProp_ids.size(); i ++) {
                @SuppressLint("InflateParams") LinearLayout oneProp = (LinearLayout) getLayoutInflater().inflate(R.layout.item_review_prop, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 10);
                oneProp.setLayoutParams(params);

                TextView propName = oneProp.findViewById(R.id.tv_prop_name);
                propName.setPadding(16, 0, 16, 0);
                propName.setText(String.valueOf(cast_mass_prop_names.get(i)));
                propName.setTextColor(Color.parseColor("#6B5CE4"));

                TextView propAnswer = oneProp.findViewById(R.id.tv_prop_answer);
                propAnswer.setText(String.valueOf(cast_mass_prop_answers.get(i)));

                ll_review_massProps.addView(oneProp);

                propName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ReviewActivity.this, MassPropositionActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    }
                });
            }
        }
    }

    private void confirmCastParty(String alertTitle, String alertMessage, String btnOK, String btnNO) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancel_ok);
        dialog.setCanceledOnTouchOutside(true);

        TextView dialogTitle = dialog.findViewById(R.id.alertTitle);
        TextView dialogMessage = dialog.findViewById(R.id.alertMessage);
        TextView dialogBtnOK = dialog.findViewById(R.id.btnOK);
        TextView dialogBtnNO = dialog.findViewById(R.id.btnNO);

        dialogTitle.setText(alertTitle);
        dialogMessage.setText(alertMessage);
        dialogBtnOK.setText(btnOK);
        dialogBtnNO.setText(btnNO);

        dialogBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (castParties.length() != 0) {
                    for (int i = 0; i < castParties.length(); i++) {
                        try {
                            castPriRaceIds = new ArrayList<>();
                            castPriPartyIds = new ArrayList<>();

                            JSONObject jsonobject = castParties.getJSONObject(i);

                            castPriRaceIds.add(jsonobject.getString("race_id"));
                            castPriPartyIds.add(jsonobject.getString("party_id"));

                            for (int j = 0; j < castPriRaceIds.size(); j++) {
                                final RequestQueue requstQueue = Volley.newRequestQueue(ReviewActivity.this);
                                String url = ReqConst.SERVER_URL + ReqConst.API_COUNTER_PARTY_CREATE;

                                JSONObject object = new JSONObject();
                                try {
                                    object.put("ballot_id", String.valueOf(ballot_id));
                                    object.put("race_id", String.valueOf(castPriRaceIds.get(j)));
                                    object.put("party_id", String.valueOf(castPriPartyIds.get(j)));
                                    object.put("pincode", pincode);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JsonObjectRequest jsonobj = new JsonObjectRequest(
                                        Request.Method.POST,
                                        url,
                                        object,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }
                                );
                                requstQueue.add(jsonobj);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(castPCandidates.length() != 0 ) {
                    for (int i = 0; i < castPCandidates.length(); i++) {
                        try {
                            castRRaceIds = new ArrayList<>();
                            castRCastIds = new ArrayList<>();
                            castRCastRaces = new ArrayList<>();

                            JSONObject jsonobject = castPCandidates.getJSONObject(i);

                            castRRaceIds.add(jsonobject.getString("race_id"));
                            JSONArray rcand_result = jsonobject.getJSONArray("result");

                            for (int j = 0; j < rcand_result.length(); j ++) {
                                JSONObject jsonobject1 = rcand_result.getJSONObject(j);
                                castRCastIds.add(jsonobject1.getString("cand_ids"));
                            }

                            for (int j = 0; j < castRRaceIds.size(); j ++) {
                                for (int k = 0; k < castRCastIds.size(); k ++) {
                                    final RequestQueue requstQueue = Volley.newRequestQueue(ReviewActivity.this);
                                    String url = ReqConst.SERVER_URL + ReqConst.API_COUNTER_CAND_CREATE;

                                    JSONObject object = new JSONObject();
                                    try {
                                        object.put("ballot_id", String.valueOf(ballot_id));
                                        object.put("race_id", String.valueOf(castRRaceIds.get(j)));
                                        object.put("candidate_id", String.valueOf(castRCastIds.get(k)));
                                        object.put("cast_value", 0);
                                        object.put("pincode", pincode);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    JsonObjectRequest jsonobj = new JsonObjectRequest(
                                            Request.Method.POST,
                                            url,
                                            object,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                }
                                            }
                                    );
                                    requstQueue.add(jsonobj);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (castRaceCandidates.length() != 0) {
                    for (int i = 0; i < castRaceCandidates.length(); i++) {
                        try {
                            castRRaceIds = new ArrayList<>();
                            castRCastIds = new ArrayList<>();
                            castRCastRaces = new ArrayList<>();

                            JSONObject jsonobject = castRaceCandidates.getJSONObject(i);

                            castRRaceIds.add(jsonobject.getString("race_id"));
                            JSONArray rcand_result = jsonobject.getJSONArray("result");

                            for (int j = 0; j < rcand_result.length(); j ++) {
                                JSONObject jsonobject1 = rcand_result.getJSONObject(j);
                                castRCastIds.add(jsonobject1.getString("cand_ids"));
                                castRCastRaces.add(jsonobject1.getString("cand_races"));
                            }

                            for (int j = 0; j < castRRaceIds.size(); j ++) {
                                for (int k = 0; k < castRCastIds.size(); k ++) {
                                    final RequestQueue requstQueue = Volley.newRequestQueue(ReviewActivity.this);
                                    String url = ReqConst.SERVER_URL + ReqConst.API_COUNTER_CAND_CREATE;

                                    JSONObject object = new JSONObject();
                                    try {
                                        object.put("ballot_id", String.valueOf(ballot_id));
                                        object.put("race_id", String.valueOf(castRRaceIds.get(j)));
                                        object.put("candidate_id", String.valueOf(castRCastIds.get(k)));
                                        object.put("cast_value", String.valueOf(castRCastRaces.get(k)));
                                        object.put("pincode", pincode);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    JsonObjectRequest jsonobj = new JsonObjectRequest(
                                            Request.Method.POST,
                                            url,
                                            object,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                }
                                            }
                                    );
                                    requstQueue.add(jsonobj);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (cast_prop_for_ids.size() != 0) {
                    for (int i = 0; i < cast_prop_for_ids.size(); i++) {
                        final RequestQueue requstQueue = Volley.newRequestQueue(ReviewActivity.this);
                        String url = ReqConst.SERVER_URL + ReqConst.API_COUNTER_PROP_CREATE;

                        JSONObject object = new JSONObject();
                        try {
                            object.put("ballot_id", String.valueOf(ballot_id));
                            object.put("race_id", String.valueOf(race_id));
                            object.put("proposition_id", String.valueOf(cast_prop_for_ids.get(i)));
                            object.put("cast_yes", 1);
                            object.put("cast_no", 0);
                            object.put("pincode", pincode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonobj = new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        );
                        requstQueue.add(jsonobj);
                    }
                }

                if (cast_prop_against_ids.size() != 0) {
                    for (int i = 0; i < cast_prop_against_ids.size(); i++) {
                        final RequestQueue requstQueue = Volley.newRequestQueue(ReviewActivity.this);
                        String url = ReqConst.SERVER_URL + ReqConst.API_COUNTER_PROP_CREATE;

                        JSONObject object = new JSONObject();
                        try {
                            object.put("ballot_id", String.valueOf(ballot_id));
                            object.put("race_id", String.valueOf(race_id));
                            object.put("proposition_id", String.valueOf(cast_prop_against_ids.get(i)));
                            object.put("cast_yes", 0);
                            object.put("cast_no", 1);
                            object.put("pincode", pincode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonobj = new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        );
                        requstQueue.add(jsonobj);
                    }
                }

                if (cast_mass_prop_for_ids.size() != 0) {
                    for (int i = 0; i < cast_mass_prop_for_ids.size(); i++) {
                        final RequestQueue requstQueue = Volley.newRequestQueue(ReviewActivity.this);
                        String url = ReqConst.SERVER_URL + ReqConst.API_COUNTER_PROP_CREATE;

                        JSONObject object = new JSONObject();
                        try {
                            object.put("ballot_id", String.valueOf(ballot_id));
                            object.put("race_id", String.valueOf(race_id));
                            object.put("proposition_id", String.valueOf(cast_mass_prop_for_ids.get(i)));
                            object.put("cast_yes", 1);
                            object.put("cast_no", 0);
                            object.put("pincode", pincode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonobj = new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        );
                        requstQueue.add(jsonobj);
                    }
                }

                if (cast_mass_prop_against_ids.size() != 0) {
                    for (int i = 0; i < cast_mass_prop_against_ids.size(); i++) {
                        final RequestQueue requstQueue = Volley.newRequestQueue(ReviewActivity.this);
                        String url = ReqConst.SERVER_URL + ReqConst.API_COUNTER_PROP_CREATE;

                        JSONObject object = new JSONObject();
                        try {
                            object.put("ballot_id", String.valueOf(ballot_id));
                            object.put("race_id", String.valueOf(race_id));
                            object.put("proposition_id", String.valueOf(cast_mass_prop_against_ids.get(i)));
                            object.put("cast_yes", 0);
                            object.put("cast_no", 1);
                            object.put("pincode", pincode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonobj = new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        );
                        requstQueue.add(jsonobj);
                    }
                }

                final RequestQueue requstQueue = Volley.newRequestQueue(ReviewActivity.this);
                String url = ReqConst.SERVER_URL + ReqConst.API_update_pincode_used;

                JSONObject object = new JSONObject();
                try {
                    JSONObject key = new JSONObject();
                    key.put("ballot_id", String.valueOf(ballot_id));
                    key.put("pin", pincode);

                    object.put("is_used", "true");
                    object.put("keys", key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonobj = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                requstQueue.add(jsonobj);

                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                globalClass.setCastResults(new JSONArray());
                globalClass.setCast_candRace(new ArrayList<String>());
                globalClass.setCast_propIds(new ArrayList<Integer>());
                globalClass.setCast_propNames(new ArrayList<String>());
                globalClass.setCast_propAnswerType(new ArrayList<Integer>());
                globalClass.setCast_propTexts(new ArrayList<String>());
                globalClass.setCast_propTitles(new ArrayList<String>());
                globalClass.setCast_propForIds(new ArrayList<Integer>());
                globalClass.setCast_propAgainstIds(new ArrayList<Integer>());
                globalClass.setCast_propAnswers(new ArrayList<String>());
                globalClass.setCast_massPropIds(new ArrayList<Integer>());
                globalClass.setCast_massPropNames(new ArrayList<String>());
                globalClass.setCast_massPropAnswerType(new ArrayList<Integer>());
                globalClass.setCast_massPropTexts(new ArrayList<String>());
                globalClass.setCast_massPropTitles(new ArrayList<String>());
                globalClass.setCast_massPropForIds(new ArrayList<Integer>());
                globalClass.setCast_massPropAgainstIds(new ArrayList<Integer>());
                globalClass.setCast_massPropAnswers(new ArrayList<String>());
                globalClass.setRace_num(0);
                globalClass.setPriFlag(false);
                globalClass.setReviewFlag(false);
                Share.race_index = 0;
                Share.array_race = new ArrayList<>();

                Intent intent = new Intent(ReviewActivity.this, FinishActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        dialogBtnNO.setOnClickListener(new View.OnClickListener() {
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

//    Print PDF function
//
//    public static Bitmap loadBitmapFromView(View v, int width, int height) {
//        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        v.draw(c);
//
//        return b;
//    }
//
//    private void printReview(){
//        PrintHelper photoPrinter = new PrintHelper(this);
//        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
//
//        photoPrinter.printBitmap("Review Print", reviewBitmap);
//    }
}