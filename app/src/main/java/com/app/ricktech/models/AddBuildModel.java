package com.app.ricktech.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddBuildModel implements Serializable {
    private String category_id;
    private String category_name;
    private String category_image;
    private List<String> product_ids;
    private List<ProductModel> productModelList = new ArrayList<>();

    public AddBuildModel() {
    }

    public AddBuildModel(String category_id, List<String> product_ids) {
        this.category_id = category_id;
        this.product_ids = product_ids;
    }

    public AddBuildModel(String category_id, String category_name, String category_image, List<String> product_ids) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_image = category_image;
        this.product_ids = product_ids;
    }

    public List<ProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
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

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public List<String> getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(List<String> product_ids) {
        this.product_ids = product_ids;
    }
}
