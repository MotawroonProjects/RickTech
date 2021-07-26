package com.app.ricktech.models;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List<CategoryModel> subCategories = new ArrayList<>();
    //custom
    private List<ProductModel> selectedProduct = new ArrayList<>();
    private List<ProductModel> defaultSelectedProduct = new ArrayList<>();


    public CategoryModel() {
    }

    public CategoryModel(int id, String desc, String image, String type, String is_final_level, String parent_id, String is_in_compare, String trans_title, List<CategoryModel> subCategories, List<ProductModel> selectedProduct,List<ProductModel> defaultSelectedProduct) {
        this.id = id;
        this.desc = desc;
        this.image = image;
        this.type = type;
        this.is_final_level = is_final_level;
        this.parent_id = parent_id;
        this.is_in_compare = is_in_compare;
        this.trans_title = trans_title;
        this.subCategories = subCategories;
        this.selectedProduct = selectedProduct;
        this.defaultSelectedProduct = defaultSelectedProduct;
    }

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
        return subCategories;
    }

    public void setSub_categories(List<CategoryModel> sub_categories) {
        this.subCategories = sub_categories;
    }



    public List<ProductModel> getDefaultSelectedProduct() {
        return defaultSelectedProduct;
    }

    public void setDefaultSelectedProduct(List<ProductModel> defaultSelectedProduct) {
        this.defaultSelectedProduct = defaultSelectedProduct;
    }
}
