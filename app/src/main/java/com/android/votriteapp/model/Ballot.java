package com.android.votriteapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ballot {
    public String address;
    public int ballot_id;
    public String board;
    public String client;
    public String election;
    public String end_date;
    public String is_delete;
    public String start_date;

    public Ballot(JSONObject jsonObject) {
        try {
            address       = jsonObject.getString("address");
            ballot_id     = jsonObject.getInt("ballot_id");
            board         = jsonObject.getString("board");
            client        = jsonObject.getString("client");
            election      = jsonObject.getString("election");
            end_date      = jsonObject.getString("end_date");
            is_delete     = jsonObject.getString("is_delete");
            start_date    = jsonObject.getString("start_date");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getBallot_id() {
        return ballot_id;
    }

    public String getElection() {
        return election;
    }

    public String getBoard() {
        return board;
    }

    public String getAddress() {
        return address;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getClient() {
        return client;
    }
}

