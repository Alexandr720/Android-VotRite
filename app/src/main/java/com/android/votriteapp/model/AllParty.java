package com.android.votriteapp.model;

import java.util.ArrayList;

public class AllParty {
    public static ArrayList<Party> parties;

    public AllParty(ArrayList<Party> parties) {
        AllParty.parties = parties;
    }

    public ArrayList<Party> getParties() {
        return parties;
    }
}
