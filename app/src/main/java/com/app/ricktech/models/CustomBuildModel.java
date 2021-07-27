package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class CustomBuildModel implements Serializable {
    private CategoryModel categoryModel;
    private List<OrderModel.Product> productModelList;

    public CustomBuildModel() {
    }

    public CustomBuildModel(CategoryModel categoryModel, List<OrderModel.Product> productModelList) {
        this.categoryModel = categoryModel;
        this.productModelList = productModelList;
    }

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public List<OrderModel.Product> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List<OrderModel.Product> productModelList) {
        this.productModelList = productModelList;
    }
}
