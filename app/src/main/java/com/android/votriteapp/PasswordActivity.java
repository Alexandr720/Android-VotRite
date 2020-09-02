package com.android.votriteapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.votriteapp.model.PinCode;
import com.android.votriteapp.model.Race;
import com.android.votriteapp.utils.Share;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class PasswordActivity extends AppCompatActivity {
    private EditText etPassword;
    private PinCode pinCodeData;
    private int ballot_id;
    private String pinCode, is_used;
    private ArrayList<Race> all_races = new ArrayList<>();
    private Race race;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        TextView btnBack = findViewById(R.id.btnBack);
        etPassword = findViewById(R.id.etPassword);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        ballot_id = globalVariable.getBallot_id();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordActivity.this, BallotActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode = etPassword.getText().toString().trim();
                getPinCodes();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PasswordActivity.this, BallotActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private void getPinCodes() {
        final RequestQueue requstQueue = Volley.newRequestQueue(this);
        String url = ReqConst.SERVER_URL + ReqConst.API_PINCODE + "?ballot_id=" + ballot_id + "&pin=" + pinCode;

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(JSONObject response) {
                    if(response != null){
                        try {
                            if(response.length() == 0) {
                                String message = getString(R.string.errorPassword);
                                confirmAlert(message);
                            } else {
                                JSONArray array = response.getJSONArray("data");
                                pinCodeData = new PinCode((JSONObject) array.get(0));
                                is_used = pinCodeData.getIs_used();

                                if(is_used.equals("true")) {
                                    String message = getString(R.string.pincode_used);
                                    confirmAlert(message);
                                } else if(is_used.equals("false")){
                                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                    globalVariable.setPin_code(pinCodeData.getPin());

                                    String expire_date = pinCodeData.getExpiration_time();

                                    SimpleDateFormat local_time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                                    local_time.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String locale_time = local_time.format(new Date());

                                    if (locale_time.compareTo(expire_date) > 0) {
                                        String message = getString(R.string.errorExpireTime);
                                        confirmAlert(message);
                                    } else {
                                        getRaces();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.v("null", "null");
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
    }

    private void getRaces() {
        final RequestQueue requstQueue = Volley.newRequestQueue(this);
        String url = ReqConst.SERVER_URL + ReqConst.API_RACE_ACTIVE;

        JSONObject object = new JSONObject();
        try {
            object.put("pincode", pinCode);
            object.put("ballot_id", String.valueOf(ballot_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response != null){
                            try {
                                if(response.length() == 0) {
                                    index ++;
                                    if(index < 5) {
                                        getRaces();
                                    } else {
                                        String alertTitle, alertMessage, btnOK, btnNO;
                                        alertTitle = getString(R.string.confirm);
                                        alertMessage = getString(R.string.errorRace);
                                        btnOK = getString(R.string.retry);
                                        btnNO = getString(R.string.close);
                                        alertBallot(alertTitle, alertMessage, btnOK, btnNO);
                                    }
                                } else {
                                    index = 0;
                                    JSONArray array = response.getJSONArray("data");

                                    for (int i = 0; i < array.length(); i++) {
                                        race = new Race((JSONObject) array.get(i));
                                        all_races.add(race);
                                    }

                                    final GlobalClass globalClass = (GlobalClass) getApplicationContext();
                                    globalClass.setCast_candRace(new ArrayList<String>());
                                    globalClass.setCastResults(new JSONArray());
                                    globalClass.setCast_propIds(new ArrayList<Integer>());
                                    globalClass.setCast_propNames(new ArrayList<String>());
                                    globalClass.setCast_propForIds(new ArrayList<Integer>());
                                    globalClass.setCast_propAgainstIds(new ArrayList<Integer>());
                                    globalClass.setCast_propAnswers(new ArrayList<String>());
                                    globalClass.setCast_massPropIds(new ArrayList<Integer>());
                                    globalClass.setCast_massPropNames(new ArrayList<String>());
                                    globalClass.setCast_massPropForIds(new ArrayList<Integer>());
                                    globalClass.setCast_massPropAnswers(new ArrayList<String>());
                                    globalClass.setCast_massPropAgainstIds(new ArrayList<Integer>());
                                    globalClass.setRace_num(all_races.size());
                                    globalClass.setReviewFlag(false);
                                    JSONArray jsonArray = new JSONArray();
                                    for (int i =0; i < all_races.size(); i ++) {
                                        jsonArray.put(i, null);
                                    }
                                    globalClass.setCastResults(jsonArray);

                                    Share.array_race = all_races;
                                    Share.race_index = 0;
                                    Share.switch_race(PasswordActivity.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.v("null", "null");
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

        dialogBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getPinCodes();
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
