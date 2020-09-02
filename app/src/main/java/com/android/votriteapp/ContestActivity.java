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
import java.util.List;
import java.util.Objects;

public class ContestActivity extends AppCompatActivity {

    private int ballot_id;
    private int race_id;
    private String cast_partyId;
    private int max_w_cand;
    private int flag, max_cand, min_cand;
    private ListView lv_candidate;
    private String race_type;

    private List<String> candidateChecked = new ArrayList<>();
    private ArrayList<Integer> candidateIds = new ArrayList<>();
    private ArrayList<String> candidateNames = new ArrayList<>();
    private ArrayList<String> partyLogos = new ArrayList<>();
    private ArrayList<String> candidatePhotos = new ArrayList<>();
    private ArrayList<Candidate> all_candidates = new ArrayList<>();

    private Candidate candidate;
    private ArrayList<String> selCandIds = new ArrayList<>();
    private ArrayList<String> selCandNames = new ArrayList<>();
    private ArrayList<String > candchecked = new ArrayList<>();
    private int selCandNum;

    private ListAdapter_Contest listAdapterContest;
    private JSONArray castResults = new JSONArray();

    private Button btnSkip, btnAddCandidate, btnHelp, btnNext;
    private TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnReset);
        btnAddCandidate = findViewById(R.id.btnSomeoneElse);
        btnHelp = findViewById(R.id.btnHelp);
        lv_candidate = findViewById(R.id.lv_candidate);
        TextView ballot_board = findViewById(R.id.ballot_board);
        TextView tvLessNum = findViewById(R.id.tvLessNum);
        TextView tvMoreNum = findViewById(R.id.tvMoreNum);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        ballot_id = globalVariable.getBallot_id();
        race_id = globalVariable.getRace_id();
        max_w_cand = globalVariable.getMax_w_cand();
        max_cand = globalVariable.getMax_vote();
        min_cand = globalVariable.getMin_vote();
        race_type = globalVariable.getRace_type();

        String board = globalVariable.getRace_name();
        ballot_board.setText(board);
        tvLessNum.setText(String.valueOf(min_cand));
        tvMoreNum.setText(String.valueOf(max_cand));

        btnSkip.setEnabled(true);
        btnNext.setEnabled(true);
        btnBack.setEnabled(true);

        if(globalVariable.getReviewFlag()) {
            btnSkip.setText(R.string.review_your_choice1);
        } else {
            btnSkip.setText(R.string.review_your_choice);
        }

        try {
            getCandidates();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        flag = 0;
    }

    @Override
    public void onBackPressed() {
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        if(race_type.equals("P")) {
            globalClass.setPriFlag(false);
            Intent intent = new Intent(ContestActivity.this, VotForPartyActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        } else if(race_type.equals("S")) {
            if(Share.race_index > 0) {
                Share.race_index --;
                Share.switch_race(ContestActivity.this);
            } else {
                globalClass.setRace_num(0);
                Intent intent = new Intent(ContestActivity.this, PasswordActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        }
    }

    private void getCandidates() throws JSONException {
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final RequestQueue requstQueue = Volley.newRequestQueue(this);
        String url = "";

        if(race_type.equals("P")) {
            castResults = globalVariable.getCastResults();
            if(!castResults.isNull(Share.race_index)) {
                JSONObject jsonObject = castResults.getJSONObject(Share.race_index);
                if (!jsonObject.isNull("party_id")) {
                    cast_partyId = jsonObject.getString("party_id");
                    url = ReqConst.SERVER_URL + ReqConst.API_CANDIDATE + "?ballot_id=" + ballot_id + "&race_id=" + race_id + "&party_id=" + cast_partyId;
                } else {
                    globalVariable.setPriFlag(false);
                    Intent intent = new Intent(ContestActivity.this, VotForPartyActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }
            }

        } else if(race_type.equals("S")){
            url = ReqConst.SERVER_URL + ReqConst.API_CANDIDATE + "?ballot_id=" + ballot_id + "&race_id=" + race_id;
        }

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.GET, url, null,
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

                                    castResults = globalVariable.getCastResults();

                                    if(!castResults.isNull(Share.race_index)) {
                                        JSONObject jsonObject = castResults.getJSONObject(Share.race_index);
                                        if(!jsonObject.isNull("result")) {
                                            JSONArray cand_checked = jsonObject.getJSONArray("cand_checked");
                                            candidateChecked = new ArrayList<>();
                                            for (int j = 0; j < cand_checked.length(); j++) {
                                                JSONObject jsonobject1 = cand_checked.getJSONObject(j);
                                                candidateChecked.add(jsonobject1.getString("checked"));
                                            }

                                            for (int j = 0; j < all_candidates.size(); j++) {
                                                candidateIds.add(all_candidates.get(j).getCandidate_id());
                                                candidateNames.add(all_candidates.get(j).getCandidate_name());
                                                candidatePhotos.add(all_candidates.get(j).getPhoto());
                                                partyLogos.add(all_candidates.get(j).getParty_logo());
                                            }
                                        } else {
                                            for (int j = 0; j < all_candidates.size(); j++) {
                                                candidateChecked.add("false");
                                                candidateIds.add(all_candidates.get(j).getCandidate_id());
                                                candidateNames.add(all_candidates.get(j).getCandidate_name());
                                                candidatePhotos.add(all_candidates.get(j).getPhoto());
                                                partyLogos.add(all_candidates.get(j).getParty_logo());
                                            }
                                        }
                                    } else {
                                        for (int j = 0; j < all_candidates.size(); j++) {
                                            candidateChecked.add("false");
                                            candidateIds.add(all_candidates.get(j).getCandidate_id());
                                            candidateNames.add(all_candidates.get(j).getCandidate_name());
                                            candidatePhotos.add(all_candidates.get(j).getPhoto());
                                            partyLogos.add(all_candidates.get(j).getParty_logo());
                                        }
                                    }

                                    listAdapterContest = new ListAdapter_Contest(ContestActivity.this, candidateIds, candidateNames, partyLogos, candidatePhotos, candidateChecked);
                                    lv_candidate.setAdapter(listAdapterContest);
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
                String message = getString(R.string.help_contest);
                helpAlert(message);
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSkip.setEnabled(false);
                if (!globalVariable.getReviewFlag()) {
                    selCandIds = listAdapterContest.selCandIds;
                    selCandNames = listAdapterContest.selCandNames;
                    candchecked = listAdapterContest.candChecked;
                    selCandNum = selCandIds.size();

                    for(int k = 0; k < candchecked.size(); k++) {
                        JSONObject checked = new JSONObject();
                        try {
                            checked.put("checked", candchecked.get(k));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray candsChecked = globalVariable.getCandsChecked();
                        candsChecked.put(checked);
                        globalVariable.setCandsChecked(candsChecked);
                    }

                    if(globalVariable.getRace_type().equals("P")) {
                        castResults = globalVariable.getCastResults();
                        try {
                            JSONObject jsonObject = castResults.getJSONObject(Share.race_index);
                            jsonObject.put("result", null);
                            jsonObject.put("cand_checked", globalVariable.getCandsChecked());

                            castResults.put(Share.race_index, jsonObject);

                            globalVariable.setCastResults(castResults);
                            globalVariable.setCandsPResult(new JSONArray());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (globalVariable.getRace_type().equals("S")) {
                        castResults = globalVariable.getCastResults();
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("race_index", Share.race_index);
                            jsonObject.put("race_id", race_id);
                            jsonObject.put("race_name", globalVariable.getRace_name());
                            jsonObject.put("race_type", globalVariable.getRace_type());
                            jsonObject.put("cand_checked", globalVariable.getCandsChecked());
                            jsonObject.put("result", null);

                            castResults.put(Share.race_index, jsonObject);

                            globalVariable.setCastResults(castResults);
                            globalVariable.setCandsPResult(new JSONArray());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    globalVariable.setPriFlag(false);
                    globalVariable.setCandsChecked(new JSONArray());
                    Share.repeatRace(ContestActivity.this);
                } else {
                    selCandIds = listAdapterContest.selCandIds;
                    selCandNames = listAdapterContest.selCandNames;
                    candchecked = listAdapterContest.candChecked;
                    selCandNum = selCandIds.size();

                    if (selCandNum < min_cand) {
                        String message = getString(R.string.less_than_error) +min_cand+ getString(R.string.candidates);
                        alertDialog(message);
                    } else if (selCandNum > max_cand) {
                        String message = getString(R.string.more_than_error) +max_cand+ getString(R.string.candidates);
                        alertDialog(message);
                    } else {
                        if (selCandNum != 0) {
                            for (int k = 0; k < candchecked.size(); k++) {
                                JSONObject checked = new JSONObject();
                                try {
                                    checked.put("checked", candchecked.get(k));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONArray candsChecked = globalVariable.getCandsChecked();
                                candsChecked.put(checked);
                                globalVariable.setCandsChecked(candsChecked);
                            }

                            for (int k = 0; k < selCandIds.size(); k++) {
                                JSONObject castCandPResult = new JSONObject();
                                try {
                                    castCandPResult.put("cand_ids", selCandIds.get(k));
                                    castCandPResult.put("cand_names", selCandNames.get(k));
                                    castCandPResult.put("cand_races", "0");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONArray candsPResult = globalVariable.getCandsPResult();
                                candsPResult.put(castCandPResult);
                                globalVariable.setCandsPResult(candsPResult);
                            }
                        }

                        if(globalVariable.getRace_type().equals("P")) {
                            castResults = globalVariable.getCastResults();
                            try {
                                JSONObject jsonObject = castResults.getJSONObject(Share.race_index);
                                if (selCandNum != 0) {
                                    jsonObject.put("cand_checked", globalVariable.getCandsChecked());
                                    jsonObject.put("result", globalVariable.getCandsPResult());
                                } else {
                                    jsonObject.remove("cand_checked");
                                    jsonObject.remove("result");
                                }

                                castResults.put(Share.race_index, jsonObject);

                                globalVariable.setCastResults(castResults);
                                globalVariable.setCandsPResult(new JSONArray());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (globalVariable.getRace_type().equals("S")) {
                            castResults = globalVariable.getCastResults();
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("race_index", Share.race_index);
                                jsonObject.put("race_id", race_id);
                                jsonObject.put("race_name", globalVariable.getRace_name());
                                jsonObject.put("race_type", globalVariable.getRace_type());
                                jsonObject.put("cand_checked", globalVariable.getCandsChecked());
                                jsonObject.put("result", globalVariable.getCandsPResult());

                                castResults.put(Share.race_index, jsonObject);

                                globalVariable.setCastResults(castResults);
                                globalVariable.setCandsPResult(new JSONArray());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        globalVariable.setCandsChecked(new JSONArray());
                        globalVariable.setPriFlag(true);
                        Share.race_index ++;

                        Intent intent = new Intent(ContestActivity.this, ReviewActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                }
            }
        });

        btnAddCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag < max_w_cand) {
                    addCandidate();
                    flag ++;
                } else {
                    String message = getString(R.string.add_cand_error);
                    alertDialog(message);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setEnabled(true);
                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                if(race_type.equals("P")) {
                    globalClass.setPriFlag(false);
                    Intent intent = new Intent(ContestActivity.this, VotForPartyActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                } else if(race_type.equals("S")) {
                    if(Share.race_index > 0) {
                        Share.race_index --;
                        Share.switch_race(ContestActivity.this);
                    } else {
                        globalClass.setRace_num(0);
                        Intent intent = new Intent(ContestActivity.this, PasswordActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setEnabled(false);
                selCandIds = listAdapterContest.selCandIds;
                selCandNames = listAdapterContest.selCandNames;
                candchecked = listAdapterContest.candChecked;
                selCandNum = selCandIds.size();

                if (selCandNum < min_cand) {
                    String message = getString(R.string.less_than_error) +min_cand+ getString(R.string.candidates);
                    alertDialog(message);
                } else if (selCandNum > max_cand) {
                    String message = getString(R.string.more_than_error) +max_cand+ getString(R.string.candidates);
                    alertDialog(message);
                } else {
                    if (selCandNum != 0) {
                        for (int k = 0; k < candchecked.size(); k++) {
                            JSONObject checked = new JSONObject();
                            try {
                                checked.put("checked", candchecked.get(k));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray candsChecked = globalVariable.getCandsChecked();
                            candsChecked.put(checked);
                            globalVariable.setCandsChecked(candsChecked);
                        }

                        for (int k = 0; k < selCandIds.size(); k++) {
                            JSONObject castCandPResult = new JSONObject();
                            try {
                                castCandPResult.put("cand_ids", selCandIds.get(k));
                                castCandPResult.put("cand_names", selCandNames.get(k));
                                castCandPResult.put("cand_races", "0");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray candsPResult = globalVariable.getCandsPResult();
                            candsPResult.put(castCandPResult);
                            globalVariable.setCandsPResult(candsPResult);
                        }
                    }

                    if(globalVariable.getRace_type().equals("P")) {
                        castResults = globalVariable.getCastResults();
                        try {
                            JSONObject jsonObject = castResults.getJSONObject(Share.race_index);
                            if (selCandNum != 0) {
                                jsonObject.put("cand_checked", globalVariable.getCandsChecked());
                                jsonObject.put("result", globalVariable.getCandsPResult());
                            } else {
                                jsonObject.remove("cand_checked");
                                jsonObject.remove("result");
                            }

                            castResults.put(Share.race_index, jsonObject);

                            globalVariable.setCastResults(castResults);
                            globalVariable.setCandsPResult(new JSONArray());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (globalVariable.getRace_type().equals("S")) {
                        castResults = globalVariable.getCastResults();
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("race_index", Share.race_index);
                            jsonObject.put("race_id", race_id);
                            jsonObject.put("race_name", globalVariable.getRace_name());
                            jsonObject.put("race_type", globalVariable.getRace_type());
                            jsonObject.put("cand_checked", globalVariable.getCandsChecked());
                            jsonObject.put("result", globalVariable.getCandsPResult());

                            castResults.put(Share.race_index, jsonObject);

                            globalVariable.setCastResults(castResults);
                            globalVariable.setCandsPResult(new JSONArray());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    globalVariable.setPriFlag(false);
                    globalVariable.setCandsChecked(new JSONArray());
                    Share.repeatRace(ContestActivity.this);
                }
            }
        });
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
                String candidateName = newCandidate.getText().toString();

                if (candidateName.isEmpty()) {
                    newCandidate.setError("Require new candidate's name");
                    return;
                }

                final RequestQueue requstQueue = Volley.newRequestQueue(ContestActivity.this);
                String url = ReqConst.SERVER_URL + ReqConst.API_CANDIDATE_CREATE;

                JSONObject object = new JSONObject();
                if(race_type.equals("P")) {
                    try {
                        object.put("ballot_id", String.valueOf(ballot_id));
                        object.put("race_id", String.valueOf(race_id));
                        object.put("party_id", String.valueOf(cast_partyId));
                        object.put("candidate_name", candidateName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if(race_type.equals("S")) {
                    try {
                        object.put("ballot_id", String.valueOf(ballot_id));
                        object.put("race_id", String.valueOf(race_id));
                        object.put("candidate_name", candidateName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url, object,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String state = response.getString("message");
                                if (!state.equals("0")){
                                    candidateChecked = new ArrayList<>();
                                    candidateNames = new ArrayList<>();
                                    partyLogos = new ArrayList<>();
                                    candidatePhotos = new ArrayList<>();
                                    all_candidates = new ArrayList<>();

                                    getCandidates();
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
                ){

                };
                requstQueue.add(jsonobj);

                dialog.dismiss();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void alertDialog(String message) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
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
                try {
                    getCandidates();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void helpAlert(String message) {
        final Dialog dialog = new Dialog(ContestActivity.this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_help);
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
}