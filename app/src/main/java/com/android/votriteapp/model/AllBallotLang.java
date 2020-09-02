package com.android.votriteapp.model;

import java.util.ArrayList;

public class AllBallotLang {
    public static ArrayList<BallotLang> ballotLangs;

    public AllBallotLang(ArrayList<BallotLang> ballotLangs) {
        AllBallotLang.ballotLangs = ballotLangs;
    }

    public ArrayList<BallotLang> getBallotLangs() {
        return ballotLangs;
    }
}
