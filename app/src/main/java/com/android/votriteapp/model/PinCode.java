package com.android.votriteapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PinCode {
    public String ballot_id;
    public String created_at;
    public String created_by;
    public String expiration_time;
    public String is_active;
    public String is_used;
    public String modified_at;
    public String modified_by;
    public String pin;

    public PinCode(JSONObject jsonObject) {
        try {
            ballot_id       = jsonObject.getString("ballot_id");
            created_at      = jsonObject.getString("created_at");
            created_by      = jsonObject.getString("created_by");
            expiration_time = jsonObject.getString("expiration_time");
            is_active       = jsonObject.getString("is_active");
            is_used         = jsonObject.getString("is_used");
            modified_at     = jsonObject.getString("modified_at");
            modified_by     = jsonObject.getString("modified_by");
            pin             = jsonObject.getString("pin");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getBallot_id() {
        return ballot_id;
    }

    public String getPin() {
        return pin;
    }

    public String getExpiration_time() {
        return expiration_time;
    }

    public String getIs_used() {
        return is_used;
    }
}
