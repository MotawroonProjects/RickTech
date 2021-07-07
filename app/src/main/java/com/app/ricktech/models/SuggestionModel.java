package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class SuggestionModel implements Serializable {
    private int id;
    private String desc;
    private String image;
    private String type;
    private String is_final_level;
    private String parent_id;
    private String is_in_compare;
    private String trans_title;
    private Suggestions suggestions;

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

    public Suggestions getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(Suggestions suggestions) {
        this.suggestions = suggestions;
    }

    public static class Suggestions implements Serializable{
        private List<Products> products;
        private String category_id_to_get_next_level;
        private String is_final_level;
        private boolean isDefaultData = true;
        private List<Products> selectedProducts;


        public List<Products> getProducts() {
            return products;
        }

        public List<Products> getSelectedProducts() {
            return selectedProducts;
        }

        public void setSelectedProducts(List<Products> selectedProducts) {
            this.selectedProducts = selectedProducts;
        }

        public String getCategory_id_to_get_next_level() {
            return category_id_to_get_next_level;
        }

        public String getIs_final_level() {
            return is_final_level;
        }

        public boolean isDefaultData() {
            return isDefaultData;
        }

        public void setDefaultData(boolean defaultData) {
            isDefaultData = defaultData;
        }
    }

    public static class Products implements Serializable
    {
        private ProductModel product;
        public ProductModel getProduct() {
            return product;
        }

        public void setProduct(ProductModel product) {
            this.product = product;
        }
    }
}
