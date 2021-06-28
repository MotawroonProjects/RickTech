package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class AddToBuildDataModel implements Serializable {
    private String title;
    private double price;
    private List<AddBuildModel> pc_buidings;

    public AddToBuildDataModel(String title, double price, List<AddBuildModel> pc_buidings) {
        this.title = title;
        this.price = price;
        this.pc_buidings = pc_buidings;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public List<AddBuildModel> getPc_buidings() {
        return pc_buidings;
    }
}
