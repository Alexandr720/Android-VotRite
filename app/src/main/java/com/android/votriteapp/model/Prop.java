package com.android.votriteapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Prop {
    public int ballot_id;
//    public String created_at;
//    public String created_by;
//    public String modified_at;
//    public String modified_by;
    public int prop_answer_type;
//    public int prop_lang_id;
//    public int prop_location_id;
    public String prop_name;
    public String prop_text;
    public String prop_title;
    public String prop_type;
    public int proposition_id;

    public Prop(JSONObject jsonObject) {
        try {
            ballot_id               = jsonObject.getInt("ballot_id");
//            created_at              = jsonObject.getString("created_at");
//            created_by              = jsonObject.getString("created_by");
//            modified_at             = jsonObject.getString("modified_at");
//            modified_by             = jsonObject.getString("modified_by");
            prop_answer_type        = jsonObject.getInt("prop_answer_type");
//            prop_lang_id            = jsonObject.getInt("prop_lang_id");
//            prop_location_id        = jsonObject.getInt("prop_location_id");
            prop_name               = jsonObject.getString("prop_name");
            prop_text               = jsonObject.getString("prop_text");
            prop_title              = jsonObject.getString("prop_title");
            prop_type               = jsonObject.getString("prop_type");
            proposition_id          = jsonObject.getInt("proposition_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getBallot_id() {
        return ballot_id;
    }

    public int getProp_answer_type() {
        return prop_answer_type;
    }

    public String getProp_name() {
        return prop_name;
    }

    public String getProp_text() {
        return prop_text;
    }

    public String getProp_title() {
        return prop_title;
    }

    public String getProp_type() {
        return prop_type;
    }

    public int getProposition_id() {
        return proposition_id;
    }
}