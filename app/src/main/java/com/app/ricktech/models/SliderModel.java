package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class SliderModel extends StatusResponse implements Serializable {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class  Data implements Serializable
    {
        private String id;
        private String title;
        private String desc;
        private String image;
        private String action_link;
        private String action_link_title;
        private String product_id;
        private String created_at;
        private String updated_at;
        private ProductModel product;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }

        public String getImage() {
            return image;
        }

        public String getAction_link() {
            return action_link;
        }

        public String getAction_link_title() {
            return action_link_title;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public ProductModel getProduct() {
            return product;
        }
    }

}
