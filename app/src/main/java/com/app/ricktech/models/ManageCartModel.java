package com.app.ricktech.models;

import android.content.Context;

import com.app.ricktech.preferences.Preferences;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ManageCartModel implements Serializable {
    private static ManageCartModel instance= null;
    private Preferences preferences;
    private static String date;


    public static synchronized ManageCartModel getInstance(){
        if (instance==null){
            instance = new ManageCartModel();
            date = instance.getDate();
        }
        return instance;
    }

    public void addSingleProduct(Context context,CartModel.SingleProduct product)
    {

        CartModel cartModel = getCartModel(context);
        cartModel.setCreate_at(date);
        cartModel.addSingleProduct(product);
        preferences.create_update_cart(context,cartModel);
    }



    public void addBuildProduct(Context context,List<AddBuildModel> list,String name,int amount){
        CartModel cartModel = getCartModel(context);

        cartModel.setCreate_at(date);
        cartModel.addBuildProduct(list, name, amount);
        preferences.create_update_cart(context,cartModel);
    }



    public void removeSingleProduct(Context context,CartModel.SingleProduct singleProduct){
        CartModel cartModel = getCartModel(context);
        cartModel.removeSingleProduct(singleProduct);
        preferences.create_update_cart(context,cartModel);
    }

    public void updateItemBuild(Context context,int parent_pos, int parent_pos2, int child_pos, CartModel.SingleProduct singleProduct){

        CartModel cartModel = getCartModel(context);
        cartModel.updateItemBuild(parent_pos, parent_pos2, child_pos, singleProduct);
        preferences.create_update_cart(context,cartModel);
    }

    public void removeItemBuild(Context context,int parent_pos,int parent_pos2,int child_pos){
        CartModel cartModel = getCartModel(context);
        cartModel.removeItemBuild(parent_pos, parent_pos2, child_pos);
        preferences.create_update_cart(context,cartModel);

    }



    public void removeBuild(Context context,int parent_pos){
        CartModel cartModel = getCartModel(context);
        cartModel.removeBuild(parent_pos);
        preferences.create_update_cart(context,cartModel);

    }

    public void clearCart(Context context){
        CartModel cartModel = getCartModel(context);
        cartModel.clearCart();
        preferences.clearCart(context);
        instance = null;
    }

    public CartModel getCartModel(Context context){
        preferences = Preferences.getInstance();
        CartModel cartModel = preferences.getCartData(context);
        if (cartModel==null){
            cartModel = new CartModel();
        }

        return cartModel;
    }

    public List<CartModel> getCartList(Context context){
        preferences = Preferences.getInstance();
        List<CartModel> list = new ArrayList<>();
        CartModel cartModel = preferences.getCartData(context);
        if (cartModel!=null){
            list.add(preferences.getCartData(context));
        }

        return list;
    }

    private String getDate(){
        Calendar calendar = Calendar.getInstance();
       return new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(calendar.getTime());
    }

    public List<CartModel.SingleProduct> getSingleProductList(Context context){
        CartModel cartModel = getCartModel(context);
        if(cartModel==null){
            return new ArrayList<>();
        }
        return cartModel.getSingle_products();
    }

    public List<CartModel.BuildProduct> getBuildProductList(Context context){
        CartModel cartModel = getCartModel(context);
        if(cartModel==null){
            return new ArrayList<>();
        }
        return cartModel.getPc_buidings();
    }

    public double getTotal(Context context){
        CartModel cartModel = getCartModel(context);
        if(cartModel==null){
            return 0.0;
        }
        return cartModel.getTotal();
    }


}
