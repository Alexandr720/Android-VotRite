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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.votriteapp.apiHelper.ReqConst;
import com.android.votriteapp.global.GlobalClass;
import com.android.votriteapp.model.AllParty;
import com.android.votriteapp.model.Party;
import com.android.votriteapp.utils.Share;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VotForPartyActivity extends AppCompatActivity {

    private List<String> partyChecked = new ArrayList<>();
    private ArrayList<String> partyNames = new ArrayList<>();
    private ArrayList<String> partyLogos = new ArrayList<>();
    private ArrayList<Party> all_party = new ArrayList<>();

    private ListAdapter_Party listAdapterParty;

    private Party party;
    private AllParty allParty;

    private ListView lv_party;
    private int ballot_id, race_id, length;

    private JSONArray castResults = new JSONArray();

    private Button btnCast, btnSkip, btnHelp;
    private TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vot_for_party);

        btnBack = findViewById(R.id.btnBack);
        btnCast = findViewById(R.id.btnCast);
        btnSkip = findViewById(R.id.btnReset);
        btnHelp = findViewById(R.id.btnHelp);

        lv_party = findViewById(R.id.lv_party);
        TextView ballot_board = findViewById(R.id.ballot_board);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        ballot_id = globalVariable.getBallot_id();
        race_id = globalVariable.getRace_id();
        final String board = globalVariable.getRace_name();
        ballot_board.setText(board);

        btnSkip.setEnabled(true);
        btnCast.setEnabled(true);
        btnBack.setEnabled(true);

        if(globalVariable.getReviewFlag()) {
            btnSkip.setText(R.string.review_your_choice1);
        } else {
            btnSkip.setText(R.string.review_your_choice);
        }

        getParty(ballot_id);
    }

    @Override
    public void onBackPressed() {
        length = partyChecked.size();
        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        if (Share.race_index > 0) {
            Share.race_index--;
            Share.switch_race(VotForPartyActivity.this);
        } else {
            globalClass.setRace_num(0);
            Intent intent = new Intent(VotForPartyActivity.this, PasswordActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }

    private void getParty(final int ballot_id) {
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final RequestQueue requstQueue = Volley.newRequestQueue(this);
        String url = ReqConst.SERVER_URL + ReqConst.API_PARTY + "?ballot_id=" + ballot_id;

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response != null){
                            try {
                                if(response.length() == 0) {
                                    String alertTitle, alertMessage, btnOK, btnNO;
                                    alertTitle = getString(R.string.error);
                                    alertMessage = getString(R.string.emptyParty);
                                    btnOK = getString(R.string.retry);
                                    btnNO = getString(R.string.close);
                                    networkError(alertTitle, alertMessage, btnOK, btnNO);
                                } else {
                                    JSONArray array = response.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        party = new Party((JSONObject) array.get(i));
                                        all_party.add(party);
                                    }
                                    allParty = new AllParty(all_party);

                                    castResults = globalVariable.getCastResults();

                                    if(!castResults.isNull(Share.race_index)) {
                                        JSONObject jsonObject = castResults.getJSONObject(Share.race_index);
                                        if(!jsonObject.isNull("party_id")) {
                                            JSONArray party_checked = jsonObject.getJSONArray("party_checked");
                                            partyChecked = new ArrayList<>();
                                            for (int j = 0; j < party_checked.length(); j++) {
                                                JSONObject jsonobject1 = party_checked.getJSONObject(j);
                                                partyChecked.add(jsonobject1.getString("checked"));
                                            }

                                            for (int j = 0; j < all_party.size(); j++) {
                                                partyNames.add(all_party.get(j).getParty_name());
                                                partyLogos.add(all_party.get(j).getParty_logo());
                                            }
                                        } else {
                                            for (int j = 0; j < all_party.size(); j++) {
                                                partyChecked.add("false");
                                                partyNames.add(all_party.get(j).getParty_name());
                                                partyLogos.add(all_party.get(j).getParty_logo());
                                            }
                                        }
                                    } else {
                                        for (int j = 0; j < all_party.size(); j++) {
                                            partyChecked.add("false");
                                            partyNames.add(all_party.get(j).getParty_name());
                                            partyLogos.add(all_party.get(j).getParty_logo());
                                        }
                                    }

                                    listAdapterParty = new ListAdapter_Party(VotForPartyActivity.this, partyNames, partyLogos, partyChecked);
                                    lv_party.setAdapter(listAdapterParty);
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
                String message = getString(R.string.help_party);
                helpAlert(message);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setEnabled(false);
                length = partyChecked.size();
                final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                if (Share.race_index > 0) {
                    Share.race_index--;
                    Share.switch_race(VotForPartyActivity.this);
                } else {
                    globalClass.setRace_num(0);
                    Intent intent = new Intent(VotForPartyActivity.this, PasswordActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSkip.setEnabled(false);
                length = partyChecked.size();
                if (!globalVariable.getReviewFlag()) {
                    for (int i = 0; i < length; i ++) {
                        for(int k = 0; k < partyChecked.size(); k++) {
                            JSONObject checked = new JSONObject();
                            try {
                                checked.put("checked", partyChecked.get(k));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray candsChecked = globalVariable.getCandsChecked();
                            candsChecked.put(checked);
                            globalVariable.setCandsChecked(candsChecked);
                        }

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("race_index", Share.race_index);
                            jsonObject.put("race_id", race_id);
                            jsonObject.put("race_name", globalVariable.getRace_name());
                            jsonObject.put("race_type", globalVariable.getRace_type());
                            jsonObject.put("party_checked", globalVariable.getCandsChecked());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        castResults = globalVariable.getCastResults();
                        try {
                            castResults.put(Share.race_index, jsonObject);
                            globalVariable.setCastResults(castResults);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        globalVariable.setCandsChecked(new JSONArray());
                    }
                    Share.repeatRace(VotForPartyActivity.this);
                } else {
                    for (int i = 0; i < length; i ++) {
                        if(partyChecked.get(i).equals("true")) {
                            globalVariable.setCast_partyId(allParty.getParties().get(i).getParty_id());

                            for(int k = 0; k < partyChecked.size(); k++) {
                                JSONObject checked = new JSONObject();
                                try {
                                    checked.put("checked", partyChecked.get(k));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONArray candsChecked = globalVariable.getCandsChecked();
                                candsChecked.put(checked);
                                globalVariable.setCandsChecked(candsChecked);
                            }

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("race_index", Share.race_index);
                                jsonObject.put("race_id", race_id);
                                jsonObject.put("race_name", globalVariable.getRace_name());
                                jsonObject.put("race_type", globalVariable.getRace_type());
                                jsonObject.put("party_id", globalVariable.getCast_partyId());
                                jsonObject.put("party_name", allParty.getParties().get(i).getParty_name());
                                jsonObject.put("party_checked", globalVariable.getCandsChecked());

                                if(!castResults.isNull(Share.race_index)) {
                                    try {
                                        JSONObject jsonObject1 = castResults.getJSONObject(Share.race_index);

                                        if(!jsonObject1.isNull("party_id")) {
                                            int party_id = jsonObject1.getInt("party_id");
                                            if (String.valueOf(party_id).equals(String.valueOf(globalVariable.getCast_partyId()))) {
                                                if(!jsonObject1.isNull("cand_checked")) {
                                                    JSONArray cand_checked = jsonObject1.getJSONArray("cand_checked");
                                                    jsonObject.put("cand_checked", cand_checked);
                                                }

                                                if(!jsonObject1.isNull("result")) {
                                                    JSONArray cand_result = jsonObject1.getJSONArray("result");
                                                    jsonObject.put("result", cand_result);
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                castResults.put(Share.race_index, jsonObject);
                                globalVariable.setCastResults(castResults);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            globalVariable.setCandsChecked(new JSONArray());
                            Share.race_index ++;

                            Intent intent = new Intent(VotForPartyActivity.this, ReviewActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            break;
                        } else if (partyChecked.get(i).equals("false")){
                            if (i == length - 1) {
                                String message = getString(R.string.party_sel_error);
                                alertBallot(message);
                                break;
                            }
                        }
                    }
                }
            }
        });

        btnCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCast.setEnabled(false);
                int length = partyChecked.size();
                for (int i = 0; i < length; i ++) {
                    if(partyChecked.get(i).equals("true")) {
                        globalVariable.setCast_partyId(allParty.getParties().get(i).getParty_id());

                        castResults = globalVariable.getCastResults();

                        for(int k = 0; k < partyChecked.size(); k++) {
                            JSONObject checked = new JSONObject();
                            try {
                                checked.put("checked", partyChecked.get(k));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray candsChecked = globalVariable.getCandsChecked();
                            candsChecked.put(checked);
                            globalVariable.setCandsChecked(candsChecked);
                        }

                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("race_index", Share.race_index);
                            jsonObject.put("race_id", race_id);
                            jsonObject.put("race_name", globalVariable.getRace_name());
                            jsonObject.put("race_type", globalVariable.getRace_type());
                            jsonObject.put("party_id", globalVariable.getCast_partyId());
                            jsonObject.put("party_name", allParty.getParties().get(i).getParty_name());
                            jsonObject.put("party_checked", globalVariable.getCandsChecked());

                            if(!castResults.isNull(Share.race_index)) {
                                try {
                                    JSONObject jsonObject1 = castResults.getJSONObject(Share.race_index);

                                    if(!jsonObject1.isNull("party_id")) {
                                        int party_id = jsonObject1.getInt("party_id");
                                        if (String.valueOf(party_id).equals(String.valueOf(globalVariable.getCast_partyId()))) {
                                            if(!jsonObject1.isNull("cand_checked")) {
                                                JSONArray cand_checked = jsonObject1.getJSONArray("cand_checked");
                                                jsonObject.put("cand_checked", cand_checked);
                                            }

                                            if(!jsonObject1.isNull("result")) {
                                                JSONArray cand_result = jsonObject1.getJSONArray("result");
                                                jsonObject.put("result", cand_result);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            castResults.put(Share.race_index, jsonObject);
                            globalVariable.setCastResults(castResults);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        globalVariable.setCandsChecked(new JSONArray());
                        Intent intent = new Intent(VotForPartyActivity.this, ContestActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        break;
                    } else if (partyChecked.get(i).equals("false")){
                        if (i == length - 1) {
                            String message = getString(R.string.party_sel_error);
                            alertBallot(message);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void alertBallot(String message) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_simple);
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
        btnCast.setEnabled(true);
        btnBack.setEnabled(true);

        dialogBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getParty(ballot_id);
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
        final Dialog dialog = new Dialog(VotForPartyActivity.this, android.R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_help);
        dialog.setCanceledOnTouchOutside(true);

        TextView dialogBtnOK = dialog.findViewById(R.id.btnOK);
        TextView dialogMessage = dialog.findViewById(R.id.alertMessage);
        dialogMessage.setText(message);

        btnSkip.setEnabled(true);
        btnCast.setEnabled(true);
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
