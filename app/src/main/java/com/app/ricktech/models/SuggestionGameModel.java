package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class SuggestionGameModel implements Serializable {
    private String id;
    private String image;
    private String trans_title;
    private double compare_rate;
    private List<CompareComponentModel> components;

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTrans_title() {
        return trans_title;
    }

    public double getCompare_rate() {
        return compare_rate;
    }

    public List<CompareComponentModel> getComponents() {
        return components;
    }
}
