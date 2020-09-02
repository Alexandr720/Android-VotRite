package com.android.votriteapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.android.votriteapp.model.AllBallot;
import com.android.votriteapp.model.Ballot;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;

public class BallotActivity extends AppCompatActivity {

    private Ballot ballot;
    private ArrayList<Ballot> all_ballots = new ArrayList<>();
    private AllBallot allBallot;

    private ArrayList<String> ballots = new ArrayList<>();
    private ArrayList<String> addresses = new ArrayList<>();
    private ArrayList<String> endDates = new ArrayList<>();
    private ArrayList<Integer> ballotFlags = new ArrayList<>();
    private ListAdapter_Ballot listAdapterBallot;

    private ListView lv_ballot;
    private EditText ballot_search;
    private String keyWords = "%%";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ballot);

        lv_ballot = findViewById(R.id.lv_ballot);
        ballot_search = findViewById(R.id.etBallotSearch);
        ballot_search.clearFocus();

        // Read all Ballots
        readInitData(keyWords);

        // Action to click item of ballot list
        lv_ballot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set global variables for ballot
                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                globalVariable.setBallot_id(allBallot.getBallots().get(position).getBallot_id());
                globalVariable.setBallot_board(allBallot.getBallots().get(position).getBoard());
                globalVariable.setBallot_client(allBallot.getBallots().get(position).getClient());
                globalVariable.setBallot_election(allBallot.getBallots().get(position).getElection());

                Intent intent = new Intent(BallotActivity.this, PasswordActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        // search functions for ballot name
        ballot_search.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                keyWords = ballot_search.getText().toString();
                if(keyWords.equals("")) {
                    keyWords = "%%";
                } else {
                    keyWords = "%" + keyWords + "%";
                }

                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    all_ballots = new ArrayList<>();
                    ballots = new ArrayList<>();
                    addresses = new ArrayList<>();
                    endDates = new ArrayList<>();
                    ballotFlags = new ArrayList<>();
                    readInitData(keyWords);
                    return true;
                }
                return false;
            }
        });
    }

    // Get ballots from server using ballot keywords
    private void readInitData(final String keyWords) {
        final RequestQueue requstQueue = Volley.newRequestQueue(BallotActivity.this);
        String url = ReqConst.SERVER_URL + ReqConst.API_BALLOT_ACTIVE;

        JSONObject object = new JSONObject();
        try {
            object.put("election", keyWords);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response != null){
                            try {
                                if(response.length() == 0) {
                                    if(keyWords.equals("%%")) {
                                        String alertTitle, alertMessage, btnOK, btnNO;
                                        alertTitle = getString(R.string.confirm);
                                        alertMessage = getString(R.string.errorBallot);
                                        btnOK = getString(R.string.retry);
                                        btnNO = getString(R.string.close);
                                        alertBallot(alertTitle, alertMessage, btnOK, btnNO);
                                    } else {
                                        String message = getString(R.string.search_not_fount);
                                        confirmAlert(message);
                                    }
                                } else {
                                    JSONArray array = response.getJSONArray("data");

                                    for (int i = 0; i < array.length(); i++) {
                                        ballot = new Ballot((JSONObject) array.get(i));
                                        all_ballots.add(ballot);
                                    }

                                    allBallot = new AllBallot(all_ballots);

                                    for (int j = 0; j < all_ballots.size(); j++) {
                                        ballots.add(all_ballots.get(j).getElection());
                                        addresses.add(all_ballots.get(j).getAddress());

                                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                        Date date = format.parse(all_ballots.get(j).getEnd_date());
                                        assert date != null;
                                        String formatDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);
                                        endDates.add(formatDate);

                                        ballotFlags.add(R.drawable.usa);
                                    }

                                    // Draw ballot list view
                                    listAdapterBallot = new ListAdapter_Ballot(BallotActivity.this, ballots, addresses, endDates, ballotFlags);
                                    lv_ballot.setAdapter(listAdapterBallot);
                                }
                            } catch (JSONException | ParseException e) {
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
                            alertBallot(alertTitle, alertMessage, btnOK, btnNO);
                        }
                    }
                }
        );
        requstQueue.add(jsonobj);
    }

    // network error message
    private void alertBallot(String alertTitle, String alertMessage, String btnOK, String btnNO) {
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
                dialog.dismiss();
                readInitData("%%");
            }
        });

        dialogBtnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // confirm alert
    private void confirmAlert(String message) {
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
}
