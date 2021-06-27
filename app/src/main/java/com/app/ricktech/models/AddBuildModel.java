package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class AddBuildModel implements Serializable {
    private String category_id;
    private List<String> product_ids;

    public AddBuildModel(String category_id, List<String> product_ids) {
        this.category_id = category_id;
        this.product_ids = product_ids;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public List<String> getList() {
        return product_ids;
    }

    public void setList(List<String> list) {
        this.product_ids = list;
    }
}
