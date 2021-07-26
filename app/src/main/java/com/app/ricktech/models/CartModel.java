package com.app.ricktech.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartModel implements Serializable {
    private double total;
    private String location_details;
    private String address;
    private double longitude;
    private double latitude;
    private String create_at;
    private List<SingleProduct> single_products = new ArrayList<>();
    private List<BuildProduct> pc_buidings = new ArrayList<>();




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

    /*------------------------models--------------------------*/
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

        public BuildProduct() {
        }

        public BuildProduct(String title, int amount, double price, List<Component> components) {
            this.title = title;
            this.amount = amount;
            this.price = price;
            this.components = components;
        }

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
        private String category_name;
        private String category_image;
        private List<Integer> product_ids;
        private List<SingleProduct> componentItemList;

        public Component() {
        }

        public Component(int category_id, String category_name, String category_image, List<Integer> product_ids, List<SingleProduct> componentItemList) {
            this.category_id = category_id;
            this.category_name = category_name;
            this.category_image = category_image;
            this.product_ids = product_ids;
            this.componentItemList = componentItemList;
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

        public List<SingleProduct> getComponentItemList() {
            return componentItemList;
        }

        public void setComponentItemList(List<SingleProduct> componentItemList) {
            this.componentItemList = componentItemList;
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
    }



    /*------------------------operations--------------------------*/

    public void addSingleProduct(SingleProduct product)
    {
        int itemPos = getSingleProductPos(product);
        if (itemPos==-1){

            single_products.add(product);
        }else {

            single_products.set(itemPos, product);
        }

        calculateTotal();
    }



    public void addBuildProduct(List<AddBuildModel> list,String name,int amount){
        List<Component> componentList = new ArrayList<>();
        double total = 0;
        for (AddBuildModel addBuildModel:list){
            Component component = new Component();
            component.setCategory_id(Integer.parseInt(addBuildModel.getCategory_id()));
            component.setCategory_name(addBuildModel.getCategory_name());
            component.setCategory_image(addBuildModel.getCategory_image());
            Log.e("name", addBuildModel.getCategory_name()+"__"+addBuildModel.getProductModelList().size());

            List<Integer> products_ids = new ArrayList<>();
            List<SingleProduct> componentItemList = new ArrayList<>();
            for (ProductModel productModel:addBuildModel.getProductModelList()){
                Log.e("11111", "1111111");
                products_ids.add(productModel.getId());
                SingleProduct componentItem = new SingleProduct(productModel.getId(),productModel.getTrans_title(), productModel.getMain_image(),productModel.getCount(),productModel.getPrice());
                componentItemList.add(componentItem);

                Log.e("price", productModel.getCount()+"___"+productModel.getPrice());
                total += productModel.getCount()*productModel.getPrice();
            }
            component.setProduct_ids(products_ids);
            component.setComponentItemList(componentItemList);
            componentList.add(component);
            Log.e("rrttggggggg", total+"_"+componentList.size()+"_____");

        }
        total = total*amount;

        Log.e("total", total+"_"+componentList.size()+"_____");
        BuildProduct buildProduct = new BuildProduct(name,amount,total,componentList);
        pc_buidings.add(buildProduct);
        calculateTotal();

    }

    private int getSingleProductPos (SingleProduct product){
        int pos = -1;
        for (int index = 0;index<single_products.size();index++){
            if (single_products.get(index).product_id==product.getId()){
                pos = index;
                return pos;
            }
        }
        return  pos;
    }

    private void calculateTotal ()
    {
        total = 0;
        for (SingleProduct singleProduct : single_products){
            total += singleProduct.getAmount()*singleProduct.getPrice();
        }

        for (BuildProduct buildProduct : pc_buidings){
            for (Component component:buildProduct.components){

                for (SingleProduct item :component.componentItemList){
                    total += item.amount*item.price;
                }
            }
        }
    }

    public void removeSingleProduct(SingleProduct singleProduct){
        int pos = getSingleProductPos(singleProduct);
        single_products.remove(pos);
        calculateTotal();
    }

    public void clearCart(){
        total = 0;
        single_products.clear();
        pc_buidings.clear();
    }

    public void updateItemBuild(int parent_pos,int parent_pos2,int child_pos,SingleProduct singleProduct){
        BuildProduct buildProduct = pc_buidings.get(parent_pos);
        List<Component> components = buildProduct.getComponents();
        Component component = components.get(parent_pos2);
        List<SingleProduct> componentItemList = component.getComponentItemList();
        componentItemList.set(child_pos,singleProduct);
        component.setComponentItemList(componentItemList);
        components.set(parent_pos2, component);
        buildProduct.setComponents(components);
        pc_buidings.set(parent_pos, buildProduct);
        calculateTotal();

    }

    public void removeItemBuild(int parent_pos,int parent_pos2,int child_pos){
        BuildProduct buildProduct = pc_buidings.get(parent_pos);
        List<Component> components =new ArrayList<>(buildProduct.getComponents()) ;
        Component component = components.get(parent_pos2);
        List<SingleProduct> componentItemList = new ArrayList<>(component.getComponentItemList());
        List<Integer> products_ids = new ArrayList<>(component.getProduct_ids());

        componentItemList.remove(child_pos);
        products_ids.remove(child_pos);

        if (componentItemList.size()==0){
            components.remove(parent_pos2);
            if (components.size()==0){
                pc_buidings.remove(parent_pos);


            }else {
                buildProduct.setComponents(components);
                pc_buidings.set(parent_pos, buildProduct);

            }
        }else {
            component.setComponentItemList(componentItemList);
            component.setProduct_ids(products_ids);

        }


        calculateTotal();

    }



    public void removeBuild(int parent_pos){
        pc_buidings.remove(parent_pos);
    }

}
