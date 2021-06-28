package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class AddCompareModel implements Serializable {
    private List<AddBuildModel> compare_attributes;

    public AddCompareModel(List<AddBuildModel> compare_attributes) {
        this.compare_attributes = compare_attributes;
    }

    public List<AddBuildModel> getCompare_attributes() {
        return compare_attributes;
    }
}
