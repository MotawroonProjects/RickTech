package com.app.ricktech.models;

import java.io.Serializable;

public class CompareComponentModel implements Serializable {
    private String id;
    private String game_id;
    private String category_id;
    private String product_id;
    private double points;
    private CategoryModel category;
    private String image;
    private String type;
    private String is_final_level;
    private String is_in_compare;
    private String trans_title;
    private ProductModel product;

    public String getId() {
        return id;
    }

    public String getGame_id() {
        return game_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public double getPoints() {
        return points;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getIs_final_level() {
        return is_final_level;
    }

    public String getIs_in_compare() {
        return is_in_compare;
    }

    public String getTrans_title() {
        return trans_title;
    }

    public ProductModel getProduct() {
        return product;
    }
}
