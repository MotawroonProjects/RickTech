package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class SearchDataModel extends StatusResponse implements Serializable {
    private int current_page;
    private List<ProductModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<ProductModel> getData() {
        return data;
    }
}
