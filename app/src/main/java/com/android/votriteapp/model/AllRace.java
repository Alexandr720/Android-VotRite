package com.android.votriteapp.model;

import java.util.ArrayList;

public class AllRace {
    public static ArrayList<Race> races;

    public AllRace(ArrayList<Race> races) {
        AllRace.races = races;
    }

    public ArrayList<Race> getRaces() {
        return races;
    }
}
