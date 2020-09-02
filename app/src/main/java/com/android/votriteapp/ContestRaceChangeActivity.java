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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.votriteapp.apiHelper.ReqConst;
import com.android.votriteapp.global.GlobalClass;
import com.android.votriteapp.model.Candidate;
import com.android.votriteapp.utils.Share;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class ContestRaceChangeActivity extends AppCompatActivity {
    private Button btnSkip, btnAdd, btnHelp, btnNext;
    private TextView btnBack;
    String board;
    private int max_w_cand, min_cand, max_cand;
    private int flag;
    private int ballot_id;
    private int race_id;

    private Candidate candidate;
    private ArrayList<Candidate> all_candidates = new ArrayList<>();

    private ListView lv_candidate_race_change;
    private ListAdapter_Contest_Race listAdapterChangeRace;

    private ArrayList<String> candidateIds = new ArrayList<>();
    private ArrayList<String> candidateNames = new ArrayList<>();
    private ArrayList<String> candidateRaces = new ArrayList<>();
    private ArrayList<String> castRCastRaces = new ArrayList<>();

    private ArrayList<String> candidateIdsChanged = new ArrayList<>();
    private ArrayList<String> candidateNamesChanged = new ArrayList<>();
    private ArrayList<String> candidateRacesChanged = new ArrayList<>();

    private JSONObject castRaceCandResult = new JSONObject();
    private JSONArray candsResult = new JSONArray();
    private JSONArray castResults = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_race);

        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnSkip);
        btnAdd = findViewById(R.id.btnAdd);
        btnNext = findViewById(R.id.btnNext);
        btnHelp = findViewById(R.id.btnHelp);
        lv_candidate_race_change = findViewById(R.id.lv_candidate_race_change);
        TextView ballot_board = findViewById(R.id.ballot_board);
        TextView tvLessNum = findViewById(R.id.tvLessNum);
        TextView tvMoreNum = findViewById(R.id.tvMoreNum);

        final GlobalClass globalClass = (GlobalClass) getApplicationContext() ;
        max_w_cand = globalClass.getMax_w_cand();
        max_cand = globalClass.getMax_vote();
        min_cand = globalClass.getMin_vote();
        ballot_id = globalClass.getBallot_id();
        race_id = globalClass.getRace_id();
        board = globalClass.getRace_name();
        ballot_board.setText(board);
        tvLessNum.setText(String.valueOf(min_cand));
        tvMoreNum.setText(String.valueOf(max_cand));

        if(globalClass.getReviewFlag()) {
            btnSkip.setText(R.string.review_your_choice1);
        } else {
            btnSkip.setText(R.string.review_your_choice);
        }

        btnSkip.setEnabled(true);
        btnNext.setEnabled(true);
        btnBack.setEnabled(true);

        getCandidateRace();
    }

    private void getCandidateRace() {
        final GlobalClass globalVariables = (GlobalClass) getApplicationContext();

        final RequestQueue requstQueue = Volley.newRequestQueue(this);
        String url = ReqConst.SERVER_URL + ReqConst.API_CANDIDATE + "?ballot_id=" + ballot_id + "&race_id=" + race_id;

        final JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response != null){
                            try {
                                if(response.length() == 0) {
                                    String alertTitle, alertMessage, btnOK, btnNO;
                                    alertTitle = getString(R.string.error);
                                    alertMessage = getString(R.string.emptyCand);
                                    btnOK = getString(R.string.retry);
                                    btnNO = getString(R.string.close);
                                    networkError(alertTitle, alertMessage, btnOK, btnNO);
                                } else {
                                    JSONArray array = response.getJSONArray("data");

                                    for (int i = 0; i < array.length(); i++) {
                                        candidate = new Candidate((JSONObject) array.get(i));
                                        all_candidates.add(candidate);
                                    }

                                    castResults = globalVariables.getCastResults();

                                    if(!castResults.isNull(Share.race_index)) {
                                        JSONObject jsonObject = castResults.getJSONObject(Share.race_index);
                                        if(!jsonObject.isNull("result")) {
                                            JSONArray rcand_result = jsonObject.getJSONArray("result");
                                            for (int j = 0; j < rcand_result.length(); j++) {
                                                JSONObject jsonobject1 = rcand_result.getJSONObject(j);
                                                castRCastRaces.add(jsonobject1.getString("cand_races"));
                                            }

                                            if(globalVariables.getChangeFlag()) {
                                                candidateRaces = castRCastRaces;
                                            }
                                        } else {
                                            for (int j = 0; j < all_candidates.size(); j++) {
                                                candidateRaces.add("0");
                                            }
                                        }
                                    } else {
                                        for (int j = 0; j < all_candidates.size(); j++) {
                                            candidateRaces.add("0");
                                        }
                                    }

                                    for (int j = 0; j < all_candidates.size(); j++) {
                                        candidateIds.add(String.valueOf(all_candidates.get(j).getCandidate_id()));
                                        candidateNames.add(all_candidates.get(j).getCandidate_name());
                                    }

                                    globalVariables.setCast_candRace(candidateRaces);

                                    listAdapterChangeRace = new ListAdapter_Contest_Race(ContestRaceChangeActivity.this, candidateRaces, candidateNames);
                                    lv_candidate_race_change.setAdapter(listAdapterChangeRace);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            String alertTitle, alertMessage, btnOK, btnNO;
                            alertTitle = getString(R.string.error);
                            alertMessage = getString(R.string.errorNetwork);
                            btnOK = getString(R.string.retry);
                            btnNO = getString(R.string.close);
                            networkError(alertTitle, alertMessage, btnOK, btnNO);
                        }
                    }
                }
        );
        requstQueue.add(jsonobj);

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getString(R.string.help_cand_change_race);
                helpAlert(message);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag < max_w_cand) {
                    addCandidate();
                    flag ++;
                } else {
                    String message = getString(R.string.add_cand_error);
                    confirmAlert(message);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setEnabled(true);

                if(Share.race_index > 0) {
                    Share.race_index --;
                    Share.switch_race(ContestRaceChangeActivity.this);
                } else {
                    globalVariables.setRace_num(0);
                    Intent intent = new Intent(ContestRaceChangeActivity.this, PasswordActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSkip.setEnabled(false);

                if (!globalVariables.getReviewFlag()) {
                    candidateIdsChanged = candidateIds;
                    candidateNamesChanged = candidateNames;
                    candidateRacesChanged = candidateRaces;

                    JSONObject castRaceCandidates = new JSONObject();
                    try {
                        castRaceCandidates.put("race_index", Share.race_index);
                        castRaceCandidates.put("race_id", race_id);
                        castRaceCandidates.put("race_type", globalVariables.getRace_type());
                        castRaceCandidates.put("race_name", globalVariables.getRace_name());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    castResults = globalVariables.getCastResults();
                    try {
                        castResults.put(Share.race_index, castRaceCandidates);
                        globalVariables.setCastResults(castResults);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Share.repeatRace(ContestRaceChangeActivity.this);
                } else {
                    candidateIdsChanged = candidateIds;
                    candidateNamesChanged = candidateNames;
                    candidateRacesChanged = new ArrayList<>();

                    for (int i = 0; i < candidateRaces.size(); i ++) {
                        if (!candidateRaces.get(i).equals("0")) {
                            candidateRacesChanged.add(candidateRaces.get(i));
                        }
                    }

                    if (candidateRacesChanged.size() < min_cand) {
                        String message = getString(R.string.less_than_error) +min_cand+ getString(R.string.candidates);
                        confirmAlert(message);
                    } else if (candidateRacesChanged.size() > max_cand) {
                        String message = getString(R.string.more_than_error) +max_cand+ getString(R.string.candidates);
                        confirmAlert(message);
                    }  else if((candidateRacesChanged.size() == 0) || (candidateRacesChanged.size() == 1)) {
                        globalVariables.setCandsResult(new JSONArray());
                        for (int k = 0; k < candidateRaces.size(); k++) {
                            castRaceCandResult = new JSONObject();
                            try {
                                castRaceCandResult.put("cand_ids", candidateIdsChanged.get(k));
                                castRaceCandResult.put("cand_names", candidateNamesChanged.get(k));
                                castRaceCandResult.put("cand_races", candidateRaces.get(k));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            candsResult = globalVariables.getCandsResult();
                            candsResult.put(castRaceCandResult);
                            globalVariables.setCandsResult(candsResult);
                        }

                        JSONObject castRaceCandidates = new JSONObject();
                        try {
                            castRaceCandidates.put("race_index", Share.race_index);
                            castRaceCandidates.put("race_id", race_id);
                            castRaceCandidates.put("race_type", globalVariables.getRace_type());
                            castRaceCandidates.put("race_name", globalVariables.getRace_name());
                            castRaceCandidates.put("result", globalVariables.getCandsResult());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        castResults = globalVariables.getCastResults();
                        try {
                            castResults.put(Share.race_index, castRaceCandidates);
                            globalVariables.setCastResults(castResults);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Share.race_index ++;
                        globalVariables.setCast_candRace(new ArrayList<String>());

                        Intent intent = new Intent(ContestRaceChangeActivity.this, ReviewActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    } else {
                        outlook:
                        for (int i = 0; i < candidateRacesChanged.size() - 1; i ++) {
                            for (int j = i + 1; j < candidateRacesChanged.size(); j++) {
                                if (candidateRacesChanged.get(i).equals(candidateRacesChanged.get(j))) {
                                    String message = getString(R.string.error_ranks);
                                    confirmAlert(message);
                                    break outlook;
                                } else {
                                    if (i == candidateRacesChanged.size() -2) {
                                        globalVariables.setCandsResult(new JSONArray());
                                        for (int k = 0; k < candidateIdsChanged.size(); k++) {
                                            castRaceCandResult = new JSONObject();
                                            try {
                                                castRaceCandResult.put("cand_ids", candidateIdsChanged.get(k));
                                                castRaceCandResult.put("cand_names", candidateNamesChanged.get(k));
                                                castRaceCandResult.put("cand_races", candidateRaces.get(k));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            candsResult = globalVariables.getCandsResult();
                                            candsResult.put(castRaceCandResult);
                                            globalVariables.setCandsResult(candsResult);
                                        }

                                        JSONObject castRaceCandidates = new JSONObject();
                                        try {
                                            castRaceCandidates.put("race_index", Share.race_index);
                                            castRaceCandidates.put("race_id", race_id);
                                            castRaceCandidates.put("race_type", globalVariables.getRace_type());
                                            castRaceCandidates.put("race_name", globalVariables.getRace_name());
                                            castRaceCandidates.put("result", globalVariables.getCandsResult());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        castResults = globalVariables.getCastResults();
                                        try {
                                            castResults.put(Share.race_index, castRaceCandidates);
                                            globalVariables.setCastResults(castResults);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Share.race_index ++;
                                        globalVariables.setCast_candRace(new ArrayList<String>());

                                        Intent intent = new Intent(ContestRaceChangeActivity.this, ReviewActivity.class);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                                        break outlook;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setEnabled(false);

                candidateIdsChanged = candidateIds;
                candidateNamesChanged = candidateNames;
                candidateRacesChanged = new ArrayList<>();

                for (int i = 0; i < candidateRaces.size(); i ++) {
                    if (!candidateRaces.get(i).equals("0")) {
                        candidateRacesChanged.add(candidateRaces.get(i));
                    }
                }

                if (candidateRacesChanged.size() < min_cand) {
                    String message = getString(R.string.less_than_error) +min_cand+ getString(R.string.candidates);
                    confirmAlert(message);
                } else if (candidateRacesChanged.size() > max_cand) {
                    String message = getString(R.string.more_than_error) +max_cand+ getString(R.string.candidates);
                    confirmAlert(message);
                } else if((candidateRacesChanged.size() == 0) || (candidateRacesChanged.size() == 1)) {
                    globalVariables.setCandsResult(new JSONArray());
                    for (int k = 0; k < candidateRaces.size(); k++) {
                        castRaceCandResult = new JSONObject();
                        try {
                            castRaceCandResult.put("cand_ids", candidateIdsChanged.get(k));
                            castRaceCandResult.put("cand_names", candidateNamesChanged.get(k));
                            castRaceCandResult.put("cand_races", candidateRaces.get(k));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        candsResult = globalVariables.getCandsResult();
                        candsResult.put(castRaceCandResult);
                        globalVariables.setCandsResult(candsResult);
                    }

                    JSONObject castRaceCandidates = new JSONObject();
                    try {
                        castRaceCandidates.put("race_index", Share.race_index);
                        castRaceCandidates.put("race_id", race_id);
                        castRaceCandidates.put("race_type", globalVariables.getRace_type());
                        castRaceCandidates.put("race_name", globalVariables.getRace_name());
                        castRaceCandidates.put("result", globalVariables.getCandsResult());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    castResults = globalVariables.getCastResults();
                    try {
                        castResults.put(Share.race_index, castRaceCandidates);
                        globalVariables.setCastResults(castResults);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    globalVariables.setCast_candRace(new ArrayList<String>());
                    Share.repeatRace(ContestRaceChangeActivity.this);
                } else {
                    outlook:
                    for (int i = 0; i < candidateRacesChanged.size() - 1; i ++) {
                        for (int j = i + 1; j < candidateRacesChanged.size(); j++) {
                            if (candidateRacesChanged.get(i).equals(candidateRacesChanged.get(j))) {
                                String message = getString(R.string.error_ranks);
                                confirmAlert(message);
                                break outlook;
                            } else {
                                if (i == candidateRacesChanged.size() -2) {
                                    globalVariables.setCandsResult(new JSONArray());
                                    for (int k = 0; k < candidateIdsChanged.size(); k++) {
                                        castRaceCandResult = new JSONObject();
                                        try {
                                            castRaceCandResult.put("cand_ids", candidateIdsChanged.get(k));
                                            castRaceCandResult.put("cand_names", candidateNamesChanged.get(k));
                                            castRaceCandResult.put("cand_races", candidateRaces.get(k));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        candsResult = globalVariables.getCandsResult();
                                        candsResult.put(castRaceCandResult);
                                        globalVariables.setCandsResult(candsResult);
                                    }

                                    JSONObject castRaceCandidates = new JSONObject();
                                    try {
                                        castRaceCandidates.put("race_index", Share.race_index);
                                        castRaceCandidates.put("race_id", race_id);
                                        castRaceCandidates.put("race_type", globalVariables.getRace_type());
                                        castRaceCandidates.put("race_name", globalVariables.getRace_name());
                                        castRaceCandidates.put("result", globalVariables.getCandsResult());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    castResults = globalVariables.getCastResults();
                                    try {
                                        castResults.put(Share.race_index, castRaceCandidates);
                                        globalVariables.setCastResults(castResults);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    globalVariables.setCast_candRace(new ArrayList<String>());
                                    Share.repeatRace(ContestRaceChangeActivity.this);

                                    break outlook;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        GlobalClass globalVariables = (GlobalClass) getApplicationContext();
        if(Share.race_index > 0) {
            Share.race_index --;
            Share.switch_race(ContestRaceChangeActivity.this);
        } else {
            globalVariables.setRace_num(0);
            Intent intent = new Intent(ContestRaceChangeActivity.this, PasswordActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }

    private void confirmAlert(String message) {
        final Dialog dialog = new Dialog(ContestRaceChangeActivity.this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_simple);
        dialog.setCanceledOnTouchOutside(true);

        TextView dialogBtnOK = dialog.findViewById(R.id.btnOK);
        TextView dialogMessage = dialog.findViewById(R.id.alertMessage);
        dialogMessage.setText(message);

        btnSkip.setEnabled(true);
        btnNext.setEnabled(true);
        btnBack.setEnabled(true);

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

    private void helpAlert(String message) {
        final Dialog dialog = new Dialog(ContestRaceChangeActivity.this, android.R.style.Theme_Dialog);
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

    private void addCandidate() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_candidate);
        dialog.setCanceledOnTouchOutside(true);

        TextView dialogBtnClose = dialog.findViewById(R.id.btnClose);
        final Button btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setEnabled(true);

        dialogBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false);
                EditText newCandidate = dialog.findViewById(R.id.etNewCandidate);
                final String candidateName = newCandidate.getText().toString();

                if (candidateName.isEmpty()) {
                    newCandidate.setError("Require new candidate's name");
                    return;
                }

                final RequestQueue requstQueue = Volley.newRequestQueue(ContestRaceChangeActivity.this);
                String url = ReqConst.SERVER_URL + ReqConst.API_CANDIDATE_CREATE;

                JSONObject object = new JSONObject();

                try {
                    object.put("ballot_id", String.valueOf(ballot_id));
                    object.put("race_id", String.valueOf(race_id));
                    object.put("candidate_name", candidateName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url, object,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String state = response.getString("message");
                                    if (!state.equals("0")){
                                        candidateRaces.add("0");
                                        candidateNames.add(candidateName);

                                        listAdapterChangeRace = new ListAdapter_Contest_Race(ContestRaceChangeActivity.this, candidateRaces, candidateNames);
                                        lv_candidate_race_change.setAdapter(listAdapterChangeRace);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(error != null){
                                    String alertTitle, alertMessage, btnOK, btnNO;
                                    alertTitle = getString(R.string.error);
                                    alertMessage = getString(R.string.errorNetwork);
                                    btnOK = getString(R.string.retry);
                                    btnNO = getString(R.string.close);
                                    networkError(alertTitle, alertMessage, btnOK, btnNO);
                                }
                            }
                        }
                );
                requstQueue.add(jsonobj);

                dialog.dismiss();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void networkError(String alertTitle, String alertMessage, String btnOK, String btnNO) {
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

        btnSkip.setEnabled(true);
        btnNext.setEnabled(true);
        btnBack.setEnabled(true);

        dialogBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
}
