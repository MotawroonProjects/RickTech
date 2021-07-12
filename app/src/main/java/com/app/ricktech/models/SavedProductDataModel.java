package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class SavedProductDataModel extends StatusResponse implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable{
        private String id;
        private String pc_building_type_id;
        private String price;
        private String type;
        private String created_at;
        private String trans_title;
        private List<SuggestionModel.Products> products;

        public String getId() {
            return id;
        }

        public String getPc_building_type_id() {
            return pc_building_type_id;
        }

        public String getPrice() {
            return price;
        }

        public String getType() {
            return type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getTrans_title() {
            return trans_title;
        }

        public List<SuggestionModel.Products> getProducts() {
            return products;
        }
    }
}
