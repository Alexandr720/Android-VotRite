package com.android.votriteapp.global;

import android.app.Application;
import org.json.JSONArray;
import java.util.ArrayList;

public class GlobalClass extends Application{
    private int ballot_id;
    private String ballot_board;
    private String ballot_client;
    private String ballot_election;
    private int race_num;
    private int race_id;
    private String race_type;
    private String race_name;
    private int max_w_cand;
    private int max_vote;
    private int min_vote;
    private String pin_code;

    private int cast_partyId;

    private ArrayList<String> cast_candRace = new ArrayList<>();

    private ArrayList<Integer> cast_propIds = new ArrayList<>();
    private ArrayList<String> cast_propNames = new ArrayList<>();
    private ArrayList<Integer> cast_propAnswerType = new ArrayList<>();
    private ArrayList<String> cast_propTexts = new ArrayList<>();
    private ArrayList<String> cast_propTitles = new ArrayList<>();

    private ArrayList<Integer> cast_massPropIds = new ArrayList<>();
    private ArrayList<String> cast_massPropNames = new ArrayList<>();
    private ArrayList<Integer> cast_massPropAnswerType = new ArrayList<>();
    private ArrayList<String> cast_massPropTexts = new ArrayList<>();
    private ArrayList<String> cast_massPropTitles = new ArrayList<>();

    private ArrayList<Integer> cast_propForIds = new ArrayList<>();
    private ArrayList<Integer> cast_propAgainstIds = new ArrayList<>();
    private ArrayList<String> cast_propAnswers = new ArrayList<>();

    public ArrayList<Boolean> cast_propForFlag = new ArrayList<>();
    public ArrayList<Boolean> cast_propAgainstFlag = new ArrayList<>();

    public ArrayList<Boolean> cast_massPropForFlag = new ArrayList<>();
    public ArrayList<Boolean> cast_massPropAgainstFlag = new ArrayList<>();

    private ArrayList<Integer> cast_massPropForIds = new ArrayList<>();
    private ArrayList<Integer> cast_massPropAgainstIds = new ArrayList<>();
    private ArrayList<String> cast_massPropAnswers = new ArrayList<>();

    private JSONArray candsPResult = new JSONArray();
    private JSONArray candsResult = new JSONArray();
    private JSONArray candsChecked = new JSONArray();

    private String propFlag;
    private boolean priFlag = false;
    private boolean reviewFlag = false;
    private boolean changeFlag = false;

    private JSONArray castResults = new JSONArray();


    public int getBallot_id() {
        return ballot_id;
    }
    public void setBallot_id(int aBallot_id) {
        ballot_id = aBallot_id;
    }

    public String getBallot_election() {
        return ballot_election;
    }
    public void setBallot_election(String aBallot_election) {
        ballot_election = aBallot_election;
    }

    public String getBallot_board() {
        return ballot_board;
    }
    public void setBallot_board(String aBallot_board) {
        ballot_board = aBallot_board;
    }

    public String getBallot_client() {
        return ballot_client;
    }
    public void setBallot_client(String _ballot_client) {
        ballot_client = _ballot_client;
    }

    public int getRace_num() {
        return race_num;
    }
    public void setRace_num(int aRace_num) {
        race_num = aRace_num;
    }

    public int getRace_id() {
        return race_id;
    }
    public void setRace_id(int aRace_id) {
        race_id = aRace_id;
    }

    public String getRace_type() {
        return race_type;
    }
    public void setRace_type(String aRace_type) {
        race_type = aRace_type;
    }

    public String getRace_name() {
        return race_name;
    }
    public void setRace_name(String aRace_name) {
        race_name = aRace_name;
    }

    public int getMin_vote() {
        return min_vote;
    }
    public void setMin_vote(int amin_vote) {
        min_vote = amin_vote;
    }

    public int getMax_vote() {
        return max_vote;
    }
    public void setMax_vote(int amax_vote) {
        max_vote = amax_vote;
    }

    public int getMax_w_cand() {
        return max_w_cand;
    }
    public void setMax_w_cand(int aMax_w_cand) {
        max_w_cand = aMax_w_cand;
    }

    public String getPin_code() {
        return pin_code;
    }
    public void setPin_code(String aPin_code) {
        pin_code = aPin_code;
    }

    public ArrayList<String> getCast_candRace() {
        return  cast_candRace;
    }
    public void setCast_candRace(ArrayList<String> cast_candRace) {
        this.cast_candRace = cast_candRace;
    }

    public ArrayList<Integer> getCast_massPropIds() {
        return  cast_massPropIds;
    }
    public void setCast_massPropIds(ArrayList<Integer> cast_massPropIds) {
        this.cast_massPropIds = cast_massPropIds;
    }

    public ArrayList<String> getCast_massPropNames() {
        return  cast_massPropNames;
    }
    public void setCast_massPropNames(ArrayList<String> cast_massPropNames) {
        this.cast_massPropNames = cast_massPropNames;
    }

    public ArrayList<Integer> getCast_massPropAnswerType() {
        return  cast_massPropAnswerType;
    }
    public void setCast_massPropAnswerType(ArrayList<Integer> cast_massPropAnswerType) {
        this.cast_massPropAnswerType = cast_massPropAnswerType;
    }

    public ArrayList<String> getCast_massPropTexts() {
        return  cast_massPropTexts;
    }
    public void setCast_massPropTexts(ArrayList<String> cast_massPropTexts) {
        this.cast_massPropTexts = cast_massPropTexts;
    }

    public ArrayList<String> getCast_massPropTitles() {
        return  cast_massPropTitles;
    }
    public void setCast_massPropTitles(ArrayList<String> cast_massPropTitles) {
        this.cast_massPropTitles = cast_massPropTitles;
    }

    public ArrayList<Integer> getCast_propIds() {
        return  cast_propIds;
    }
    public void setCast_propIds(ArrayList<Integer> cast_propIds) {
        this.cast_propIds = cast_propIds;
    }

    public ArrayList<String> getCast_propNames() {
        return  cast_propNames;
    }
    public void setCast_propNames(ArrayList<String> cast_propNames) {
        this.cast_propNames = cast_propNames;
    }

    public ArrayList<Integer> getCast_propAnswerType() {
        return  cast_propAnswerType;
    }
    public void setCast_propAnswerType(ArrayList<Integer> cast_propAnswerType) {
        this.cast_propAnswerType = cast_propAnswerType;
    }

    public ArrayList<String> getCast_propTexts() {
        return  cast_propTexts;
    }
    public void setCast_propTexts(ArrayList<String> cast_propTexts) {
        this.cast_propTexts = cast_propTexts;
    }

    public ArrayList<String> getCast_propTitles() {
        return  cast_propTitles;
    }
    public void setCast_propTitles(ArrayList<String> cast_propTitles) {
        this.cast_propTitles = cast_propTitles;
    }

    public ArrayList<Integer> getCast_propForIds() {
        return  cast_propForIds;
    }
    public void setCast_propForIds(ArrayList<Integer> cast_propForIds) {
        this.cast_propForIds = cast_propForIds;
    }

    public ArrayList<Integer> getCast_propAgainstIds() {
        return  cast_propAgainstIds;
    }
    public void setCast_propAgainstIds(ArrayList<Integer> cast_propAgainstIds) {
        this.cast_propAgainstIds = cast_propAgainstIds;
    }

    public ArrayList<String> getCast_propAnswers() {
        return  cast_propAnswers;
    }
    public void setCast_propAnswers(ArrayList<String> cast_propAnswers) {
        this.cast_propAnswers = cast_propAnswers;
    }

    public ArrayList<Integer> getCast_massPropForIds() {
        return  cast_massPropForIds;
    }
    public void setCast_massPropForIds(ArrayList<Integer> cast_massPropForIds) {
        this.cast_massPropForIds = cast_massPropForIds;
    }

    public ArrayList<Integer> getCast_massPropAgainstIds() {
        return  cast_massPropAgainstIds;
    }
    public void setCast_massPropAgainstIds(ArrayList<Integer> cast_massPropAgainstIds) {
        this.cast_massPropAgainstIds = cast_massPropAgainstIds;
    }

    public ArrayList<String> getCast_massPropAnswers() {
        return  cast_massPropAnswers;
    }
    public void setCast_massPropAnswers(ArrayList<String> cast_massPropAnswers) {
        this.cast_massPropAnswers = cast_massPropAnswers;
    }

    public ArrayList<Boolean> getCast_massPropForFlag() {
        return  cast_massPropForFlag;
    }
    public void setCast_massPropForFlag(ArrayList<Boolean> cast_massPropForFlag) {
        this.cast_massPropForFlag = cast_massPropForFlag;
    }

    public ArrayList<Boolean> getCast_massPropAgainstFlag() {
        return  cast_massPropAgainstFlag;
    }
    public void setCast_massPropAgainstFlag(ArrayList<Boolean> cast_massPropAgainstFlag) {
        this.cast_massPropAgainstFlag = cast_massPropAgainstFlag;
    }

    public ArrayList<Boolean> getCast_propForFlag() {
        return  cast_propForFlag;
    }
    public void setCast_propForFlag(ArrayList<Boolean> cast_propForFlag) {
        this.cast_propForFlag = cast_propForFlag;
    }

    public ArrayList<Boolean> getCast_propAgainstFlag() {
        return  cast_propAgainstFlag;
    }
    public void setCast_propAgainstFlag(ArrayList<Boolean> cast_propAgainstFlag) {
        this.cast_propAgainstFlag = cast_propAgainstFlag;
    }

    public int getCast_partyId() {
        return cast_partyId;
    }
    public void setCast_partyId(int aCast_partyId) {
        cast_partyId = aCast_partyId;
    }

    public JSONArray getCandsPResult() {
        return candsPResult;
    }
    public void setCandsPResult(JSONArray _candsPResult) {
        this.candsPResult = _candsPResult;
    }

    public JSONArray getCandsChecked() {
        return candsChecked;
    }
    public void setCandsChecked(JSONArray _candsChecked) {
        this.candsChecked = _candsChecked;
    }

    public JSONArray getCandsResult() {
        return candsResult;
    }
    public void setCandsResult(JSONArray _candsResult) {
        this.candsResult = _candsResult;
    }

    public String getPropFlag() {
        return propFlag;
    }
    public void setPropFlag(String propFlag) {
        this.propFlag = propFlag;
    }

    public boolean getPriFlag() {
        return priFlag;
    }
    public void setPriFlag(boolean _priFlag) {
        this.priFlag = _priFlag;
    }

    public boolean getReviewFlag() {
        return reviewFlag;
    }
    public void setReviewFlag(boolean _reviewFlag) {
        this.reviewFlag = _reviewFlag;
    }

    public boolean getChangeFlag() {
        return !changeFlag;
    }
    public void setChangeFlag(boolean _changeFlag) {
        this.changeFlag = _changeFlag;
    }

    public JSONArray getCastResults() {
        return castResults;
    }
    public void setCastResults(JSONArray castResults) {
        this.castResults = castResults;
    }
}
