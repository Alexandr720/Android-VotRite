package com.android.votriteapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.votriteapp.ContestActivity;
import com.android.votriteapp.ContestRaceChangeActivity;
import com.android.votriteapp.PropositionActivity;
import com.android.votriteapp.R;
import com.android.votriteapp.ReviewActivity;
import com.android.votriteapp.VotForPartyActivity;
import com.android.votriteapp.apiHelper.ReqConst;
import com.android.votriteapp.global.GlobalClass;
import com.android.votriteapp.model.Prop;
import com.android.votriteapp.model.Race;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Share {
    public static ArrayList<Race> array_race = new ArrayList<>();
    public static String race_type = "", race_state;
    public static int race_index = 0, ballot_id;

    public static boolean flag = false;

    private static Prop prop;
    private static ArrayList<Prop> all_props = new ArrayList<>();
    private static ArrayList<Integer> propIds = new ArrayList<>();
    private static ArrayList<String> propNames = new ArrayList<>();
    private static ArrayList<String> propTitles = new ArrayList<>();
    private static ArrayList<String> propTexts = new ArrayList<>();
    private static ArrayList<Integer> propAnswerTypes = new ArrayList<>();

    private static ArrayList<Integer> massPropIds = new ArrayList<>();
    private static ArrayList<String> massPropNames = new ArrayList<>();
    private static ArrayList<String> massPropTitles = new ArrayList<>();
    private static ArrayList<String> massPropTexts = new ArrayList<>();
    private static ArrayList<Integer> massPropAnswerTypes = new ArrayList<>();

    public static void switch_race(Activity fromContext) {
        final GlobalClass globalVariable = (GlobalClass) fromContext.getApplicationContext();

        globalVariable.setRace_id(array_race.get(race_index).getRace_id());
        globalVariable.setRace_name(array_race.get(race_index).getRace_name());
        globalVariable.setRace_type(array_race.get(race_index).getRace_type());
        globalVariable.setMax_vote(array_race.get(race_index).getMax_num_of_votes());
        globalVariable.setMin_vote(array_race.get(race_index).getMin_num_of_votes());
        globalVariable.setMax_w_cand(array_race.get(race_index).getMax_num_of_write_ins());

        ballot_id = globalVariable.getBallot_id();
        race_state = String.valueOf(array_race.get(race_index).getState());
        race_type = String.valueOf(array_race.get(race_index).getRace_type());

        if(race_state.equals("null")) {
            switch (race_type) {
                case "S": {
                    Intent intent = new Intent(fromContext, ContestActivity.class);
                    fromContext.startActivity(intent);
                    break;
                }
                case "P": {
                    if(globalVariable.getPriFlag()) {
                        Intent intent = new Intent(fromContext, ContestActivity.class);
                        fromContext.startActivity(intent);
                    } else {
                        globalVariable.setPriFlag(false);
                        Intent intent = new Intent(fromContext, VotForPartyActivity.class);
                        fromContext.startActivity(intent);
                    }
                    break;
                }
                case "R": {
                    Intent intent = new Intent(fromContext, ContestRaceChangeActivity.class);
                    fromContext.startActivity(intent);
                    break;
                }
            }
            fromContext.finish();
            fromContext.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

    public static void repeatRace(final Activity fromContext) {
        flag = false;
        race_index ++;
        final GlobalClass globalVariables = (GlobalClass) fromContext.getApplicationContext();

        if (race_index == array_race.size()) {
            flag = true;
            globalVariables.setCast_propIds(new ArrayList<Integer>());
            globalVariables.setCast_propNames(new ArrayList<String>());
            globalVariables.setCast_propAnswerType(new ArrayList<Integer>());
            globalVariables.setCast_propTexts(new ArrayList<String>());
            globalVariables.setCast_propTitles(new ArrayList<String>());

            globalVariables.setCast_massPropIds(new ArrayList<Integer>());
            globalVariables.setCast_massPropNames(new ArrayList<String>());
            globalVariables.setCast_massPropAnswerType(new ArrayList<Integer>());
            globalVariables.setCast_massPropTexts(new ArrayList<String>());
            globalVariables.setCast_massPropTitles(new ArrayList<String>());
            globalVariables.setPropFlag("null");

            final RequestQueue requstQueue = Volley.newRequestQueue(fromContext);
            String url = ReqConst.SERVER_URL + ReqConst.API_get_prop + "?ballot_id=" + ballot_id;
            JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            if(response != null){
                                try {
                                    if(response.length() == 0) {
                                        Intent intent = new Intent(fromContext, ReviewActivity.class);
                                        fromContext.startActivity(intent);
                                    } else {
                                        JSONArray array = response.getJSONArray("data");
                                        all_props.clear();
                                        for (int i = 0; i < array.length(); i++) {
                                            prop = new Prop((JSONObject) array.get(i));
                                            all_props.add(prop);
                                        }

                                        propIds.clear();
                                        propNames.clear();
                                        propAnswerTypes.clear();
                                        propTexts.clear();
                                        propTitles.clear();
                                        massPropIds.clear();
                                        massPropNames.clear();
                                        massPropAnswerTypes.clear();
                                        massPropTexts.clear();
                                        massPropTitles.clear();

                                        for (int j = 0; j < all_props.size(); j++) {
                                            if(all_props.get(j).getProp_type().equals("P")) {
                                                propIds.add(all_props.get(j).getProposition_id());
                                                propNames.add(all_props.get(j).getProp_name());
                                                propAnswerTypes.add(all_props.get(j).getProp_answer_type());
                                                propTexts.add(all_props.get(j).getProp_text());
                                                propTitles.add(all_props.get(j).getProp_title());

                                                globalVariables.setCast_propIds(propIds);
                                                globalVariables.setCast_propNames(propNames);
                                                globalVariables.setCast_propAnswerType(propAnswerTypes);
                                                globalVariables.setCast_propTexts(propTexts);
                                                globalVariables.setCast_propTitles(propTitles);
                                            } else if(all_props.get(j).getProp_type().equals("M")) {
                                                massPropIds.add(all_props.get(j).getProposition_id());
                                                massPropNames.add(all_props.get(j).getProp_name());
                                                massPropAnswerTypes.add(all_props.get(j).getProp_answer_type());
                                                massPropTexts.add(all_props.get(j).getProp_text());
                                                massPropTitles.add(all_props.get(j).getProp_title());

                                                globalVariables.setCast_massPropIds(massPropIds);
                                                globalVariables.setCast_massPropNames(massPropNames);
                                                globalVariables.setCast_massPropAnswerType(massPropAnswerTypes);
                                                globalVariables.setCast_massPropTexts(massPropTexts);
                                                globalVariables.setCast_massPropTitles(massPropTitles);
                                            }
                                        }

                                        Intent intent = new Intent(fromContext, PropositionActivity.class);
                                        fromContext.startActivity(intent);
                                    }
                                    fromContext.finish();
                                    fromContext.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
                                Toast.makeText(fromContext.getApplicationContext(),"Network Connection Error!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
            requstQueue.add(jsonobj);
        } else {
            switch_race(fromContext);
        }
    }
}
