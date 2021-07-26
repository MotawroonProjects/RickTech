package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private int id;
    private int user_id;
    private String title;
    private double total;
    private String more_details;
    private String location_details;
    private String address;
    private String longitude;
    private String latitude;
    private String status;
    private String finished_date;
    private String type;
    private String cancel_reason;
    private String back_reason;
    private String created_at;
    private String updated_at;
    private List<OrderPc> order_details_pcs;
    private List<OrderProducts> order_details_products;


    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public double getTotal() {
        return total;
    }

    public String getMore_details() {
        return more_details;
    }

    public String getLocation_details() {
        return location_details;
    }

    public String getAddress() {
        return address;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getStatus() {
        return status;
    }

    public String getFinished_date() {
        return finished_date;
    }

    public String getType() {
        return type;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public String getBack_reason() {
        return back_reason;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public List<OrderPc> getOrder_details_pcs() {
        return order_details_pcs;
    }

    public List<OrderProducts> getOrder_details_products() {
        return order_details_products;
    }

    public static class OrderPc implements Serializable{
        private int id;
        private int order_id;
        private String product_id;
        private int pc_id;
        private int price;
        private int amount;
        private String created_at;
        private String updated_at;
        private Pc pc;

        public int getId() {
            return id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public int getPc_id() {
            return pc_id;
        }

        public int getPrice() {
            return price;
        }

        public int getAmount() {
            return amount;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public Pc getPc() {
            return pc;
        }
    }
    public static class Product implements Serializable{
        private int id;
        private int pc_id;
        private int pc_brand_category_id;
        private int product_id;
        private String created_at;
        private String updated_at;
        private ProductModel product;

        public int getId() {
            return id;
        }

        public int getPc_id() {
            return pc_id;
        }

        public int getPc_brand_category_id() {
            return pc_brand_category_id;
        }

        public int getProduct_id() {
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
    public static class Pc implements Serializable{
        private int id;
        private String pc_building_type_id;
        private double price;
        private String type;
        private int user_id;
        private String created_at;
        private String updated_at;
        private String trans_title;
        private List<Product> products;

        public int getId() {
            return id;
        }

        public String getPc_building_type_id() {
            return pc_building_type_id;
        }

        public double getPrice() {
            return price;
        }

        public String getType() {
            return type;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getTrans_title() {
            return trans_title;
        }

        public List<Product> getProducts() {
            return products;
        }
    }
    public static class OrderProducts implements Serializable{
        private int id;
        private int order_id;
        private int product_id;
        private String pc_id;
        private double price;
        private int amount;
        private String created_at;
        private String updated_at;
        private ProductModel product;

        public int getId() {
            return id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public String getPc_id() {
            return pc_id;
        }

        public double getPrice() {
            return price;
        }

        public int getAmount() {
            return amount;
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
