package com.app.ricktech.models;

import java.io.Serializable;

public class BrandModel implements Serializable {
    private int id;
    private String image;
    private String trans_title;

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTrans_title() {
        return trans_title;
    }
}
