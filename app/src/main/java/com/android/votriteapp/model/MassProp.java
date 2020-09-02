package com.android.votriteapp.model;

import java.util.ArrayList;

public class MassProp {
    public int id;
    public String name;
    public int type;
    public String text;
    public String title;

    public MassProp(int id, String name, int type, String text, String title) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.text = text;
        this.title = title;
    }
}
