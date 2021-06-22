package com.app.ricktech.models;

import java.io.Serializable;

public class ComponentModel implements Serializable {
    private int id;
    private String product_id;
    private String category_id;
    private String component_id;
    private CategoryModel category;
    private ProductModel component;

    public int getId() {
        return id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getComponent_id() {
        return component_id;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public ProductModel getComponent() {
        return component;
    }
}
