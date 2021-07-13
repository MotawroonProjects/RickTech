package com.app.ricktech.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartModel implements Serializable {
    private static CartModel instance = null;
    private double total;
    private String location_details;
    private String address;
    private double longitude;
    private double latitude;
    private String create_at;
    private List<SingleProduct> single_products = new ArrayList<>();
    private List<BuildProduct> pc_buidings = new ArrayList<>();



    private CartModel() {
    }

    public static synchronized CartModel getInstance(){
        if (instance==null){
            instance = new CartModel();
        }

        return instance;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getLocation_details() {
        return location_details;
    }

    public void setLocation_details(String location_details) {
        this.location_details = location_details;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<SingleProduct> getSingle_products() {
        return single_products;
    }

    public void setSingle_products(List<SingleProduct> single_products) {
        this.single_products = single_products;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public List<BuildProduct> getPc_buidings() {
        return pc_buidings;
    }

    public void setPc_buidings(List<BuildProduct> pc_buidings) {
        this.pc_buidings = pc_buidings;
    }

    public static class SingleProduct implements Serializable{
        private int product_id;
        private String name;
        private String image;
        private int amount;
        private double price;

        public SingleProduct() {
        }

        public SingleProduct(int product_id, String name, String image, int amount, double price) {
            this.product_id = product_id;
            this.name = name;
            this.image = image;
            this.amount = amount;
            this.price = price;
        }

        public int getId() {
            return product_id;
        }

        public void setId(int id) {
            this.product_id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    public static class BuildProduct implements Serializable{
        private String title;
        private int amount;
        private double price;
        private List<Component> components;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public List<Component> getComponents() {
            return components;
        }

        public void setComponents(List<Component> components) {
            this.components = components;
        }
    }


    public static class Component implements Serializable{
        private int category_id;
        private List<Integer> product_ids;

        public Component() {
        }

        public Component(int category_id, List<Integer> product_ids) {
            this.category_id = category_id;
            this.product_ids = product_ids;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public List<Integer> getProduct_ids() {
            return product_ids;
        }

        public void setProduct_ids(List<Integer> product_ids) {
            this.product_ids = product_ids;
        }
    }



    public void addSingleProduct(ProductModel productModel){
        int itemPos = getSingleProductPos(productModel);
        if (itemPos==-1){

            SingleProduct singleProduct = new SingleProduct(productModel.getId(),productModel.getTrans_title(),productModel.getMain_image(),1,productModel.getPrice());
            total += singleProduct.getAmount()*singleProduct.getPrice();
            single_products.add(singleProduct);
        }else {
            SingleProduct singleProduct = single_products.get(itemPos);
            int amount = singleProduct.getAmount();
            int newAmount = amount+1;
            total -= amount*singleProduct.getPrice();
            singleProduct.setAmount(newAmount);
            single_products.set(itemPos, singleProduct);
        }
    }



    public void addBuildProduct(ProductModel productModel){

    }


    public int getSingleProductPos (ProductModel productModel){
        int pos = -1;
        for (int index = 0;index<single_products.size();index++){
            if (single_products.get(index).product_id==productModel.getId()){
                pos = index;
                return pos;
            }
        }
        return  pos;
    }
}
