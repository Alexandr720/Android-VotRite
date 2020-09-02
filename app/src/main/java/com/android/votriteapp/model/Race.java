package com.android.votriteapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Race {
    public int ballot_id;
    public int max_num_of_write_ins;
    public int race_id;
    public String race_name;
    public String race_type;
    public String state;
    public String voted_at;
    public int min_num_of_votes;
    public int max_num_of_votes;

    public Race(JSONObject jsonObject) {
        try {
            ballot_id            = jsonObject.getInt("ballot_id");
            max_num_of_write_ins = jsonObject.getInt("max_num_of_write_ins");
            race_id              = jsonObject.getInt("race_id");
            race_name            = jsonObject.getString("race_name");
            race_type            = jsonObject.getString("race_type");
            state                = jsonObject.getString("state");
            voted_at             = jsonObject.getString("voted_at");
            min_num_of_votes     = jsonObject.getInt("min_num_of_votes");
            max_num_of_votes     = jsonObject.getInt("max_num_of_votes");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getBallot_id() {
        return ballot_id;
    }

    public int getMax_num_of_write_ins() {
        return max_num_of_write_ins;
    }

    public int getRace_id() {
        return race_id;
    }

    public String getRace_name() {
        return race_name;
    }

    public String getRace_type() {
        return race_type;
    }

    public String getState() {
        return state;
    }

    public String getVoted_at() {
        return voted_at;
    }

    public int getMin_num_of_votes() {
        return min_num_of_votes;
    }

    public int getMax_num_of_votes() {
        return max_num_of_votes;
    }
}