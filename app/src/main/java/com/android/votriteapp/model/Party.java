package com.android.votriteapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Party {
    public int ballot_id;
    public int party_id;
    public String party_logo;
    public String party_name;

    public Party(JSONObject jsonObject) {
        try {
            ballot_id      = jsonObject.getInt("ballot_id");
            party_id       = jsonObject.getInt("party_id");
            party_logo     = jsonObject.getString(("party_logo"));
            party_name     = jsonObject.getString(("party_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getBallot_id() {
        return ballot_id;
    }

    public int getParty_id() {
        return party_id;
    }

    public String getParty_name() {
        return party_name;
    }

    public String getParty_logo() {
        return party_logo;
    }
}

