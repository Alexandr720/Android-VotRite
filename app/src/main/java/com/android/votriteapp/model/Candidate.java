package com.android.votriteapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Candidate {
    public int ballot_id;
    public int candidate_id;
    public String candidate_name;
    public String created_at;
    public String created_by;
    public String email;
    public boolean enabled;
    public int lang_id;
    public String modified_at;
    public String modified_by;
    public int party_id;
    public String party_logo;
    public String party_name;
    public String photo;
    public int race_id;

    public Candidate(JSONObject jsonObject) {
        try {
            ballot_id      = jsonObject.getInt("ballot_id");
            candidate_id   = jsonObject.getInt("candidate_id");
            candidate_name = jsonObject.getString("candidate_name");
            created_at     = jsonObject.getString("created_at");
            created_by     = jsonObject.getString("created_by");
            email          = jsonObject.getString("email");
            enabled        = jsonObject.getBoolean("enabled");
            lang_id        = jsonObject.getInt("lang_id");
            modified_at    = jsonObject.getString("modified_at");
            modified_by    = jsonObject.getString("modified_by");
            party_id       = jsonObject.getInt("party_id");
            party_logo     = jsonObject.getString(("party_logo"));
            party_name     = jsonObject.getString(("party_name"));
            photo          = jsonObject.getString(("photo"));
            race_id        = jsonObject.getInt("ballot_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getBallot_id() {
        return ballot_id;
    }

    public int getCandidate_id() {
        return candidate_id;
    }

    public String getCandidate_name() {
        return candidate_name;
    }

    public String getParty_logo() {
        return party_logo;
    }

    public String getPhoto() {
        return photo;
    }
}

