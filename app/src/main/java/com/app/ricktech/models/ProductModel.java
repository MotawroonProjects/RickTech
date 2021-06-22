package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {

    private int id;
    private String category_id;
    private int brand_id;
    private String main_image;
    private double price;
    private double old_price;
    private String have_offer;
    private String offer_type;
    private double offer_value;
    private double offer_value_value;
    private double rank;
    private double points;
    private int amount;
    private String type;
    private String is_shown;
    private String trans_title;
    private String trans_desc;
    private BrandModel brand;
    private CategoryModel category;
    private List<ComponentModel> components;

    public int getId() {
        return id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public String getMain_image() {
        return main_image;
    }

    public double getPrice() {
        return price;
    }

    public double getOld_price() {
        return old_price;
    }

    public String getHave_offer() {
        return have_offer;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public double getOffer_value() {
        return offer_value;
    }

    public double getOffer_value_value() {
        return offer_value_value;
    }

    public double getRank() {
        return rank;
    }

    public double getPoints() {
        return points;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getIs_shown() {
        return is_shown;
    }

    public String getTrans_title() {
        return trans_title;
    }

    public String getTrans_desc() {
        return trans_desc;
    }

    public BrandModel getBrand() {
        return brand;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public List<ComponentModel> getComponents() {
        return components;
    }
}
