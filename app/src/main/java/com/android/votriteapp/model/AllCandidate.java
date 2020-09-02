package com.android.votriteapp.model;

import java.util.ArrayList;

public class AllCandidate {
    public static ArrayList<Candidate> candidates;

    public AllCandidate(ArrayList<Candidate> candidates) {
        AllCandidate.candidates = candidates;
    }

    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }
}
