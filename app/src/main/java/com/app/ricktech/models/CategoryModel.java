package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class CategoryModel implements Serializable {
    private int id;
    private String desc;
    private String image;
    private String type;
    private String is_final_level;
    private String parent_id;
    private String is_in_compare;
    private String trans_title;
    private List<CategoryModel> sub_categories;
    //custom
    private List<ProductModel> selectedProduct;
    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
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

    public String getParent_id() {
        return parent_id;
    }

    public String getIs_in_compare() {
        return is_in_compare;
    }

    public String getTrans_title() {
        return trans_title;
    }

    public List<ProductModel> getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(List<ProductModel> selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<CategoryModel> getSub_categories() {
        return sub_categories;
    }
}
