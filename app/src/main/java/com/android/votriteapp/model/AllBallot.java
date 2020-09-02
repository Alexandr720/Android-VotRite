package com.android.votriteapp.model;

import java.util.ArrayList;

public class AllBallot {
    public static ArrayList<Ballot> ballots;

    public AllBallot (ArrayList<Ballot> ballots) {
        AllBallot.ballots = ballots;
    }

    public ArrayList<Ballot> getBallots() {
        return ballots;
    }
}
