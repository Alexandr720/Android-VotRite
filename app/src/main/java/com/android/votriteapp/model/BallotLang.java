package com.android.votriteapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class BallotLang {
    public int ballot_id;
    public int ballot_lang_id;
    public int lang_id;
    public String language_code;
    public String language_name;

    public BallotLang(JSONObject jsonObject) {
        try {
            ballot_id           = jsonObject.getInt("ballot_id");
            ballot_lang_id      = jsonObject.getInt("ballot_lang_id");
            lang_id             = jsonObject.getInt("lang_id");
            language_code       = jsonObject.getString("language_code");
            language_name       = jsonObject.getString("language_name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getBallot_id() {
        return ballot_id;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public String getLanguage_name() {
        return language_name;
    }
}

