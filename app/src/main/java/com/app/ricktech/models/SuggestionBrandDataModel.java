package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class SuggestionBrandDataModel extends StatusResponse implements Serializable {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable
    {
        private String id;
        private String trans_title;
        private double price;

        public String getId() {
            return id;
        }

        public String getTrans_title() {
            return trans_title;
        }

        public double getPrice() {
            return price;
        }
    }
}
