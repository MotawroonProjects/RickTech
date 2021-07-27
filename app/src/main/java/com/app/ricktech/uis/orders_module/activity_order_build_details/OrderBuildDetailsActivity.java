package com.app.ricktech.uis.orders_module.activity_order_build_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.ricktech.R;
import com.app.ricktech.adapters.OrderAdapter;
import com.app.ricktech.adapters.OrderBuildProductsAdapter;
import com.app.ricktech.databinding.ActivityOrderBuildDetailsBinding;
import com.app.ricktech.databinding.ActivityOrderDetailsBinding;
import com.app.ricktech.databinding.ActivityOrdersBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.CustomBuildModel;
import com.app.ricktech.models.OrderDataModel;
import com.app.ricktech.models.OrderModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SuggestionModel;
import com.app.ricktech.models.SuggestionsDataModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderBuildDetailsActivity extends AppCompatActivity {

    private ActivityOrderBuildDetailsBinding binding;
    private String lang;
    private OrderBuildProductsAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<CustomBuildModel> list;
    private OrderModel.OrderPc  orderPc;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_build_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        orderPc = (OrderModel.OrderPc) intent.getSerializableExtra("data");
    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderBuildProductsAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());
        binding.progBar.setVisibility(View.GONE);
        filterData();

    }

    private void filterData() {
        list.clear();
        List<CategoryModel> categoryModelList = new ArrayList<>(getCategory());
        for (CategoryModel categoryModel:categoryModelList){
            List<OrderModel.Product> products = new ArrayList<>();
            for (OrderModel.Product product:orderPc.getPc().getProducts()){
                if (product.getProduct().getCategory().getId()==categoryModel.getId()){

                    int amount = getAmountProduct(product.getProduct().getId());
                    product.getProduct().setAmount(amount);
                    if (!isProductFound(products,product)){
                        products.add(product);

                    }
                }
            }

            CustomBuildModel customBuildModel = new CustomBuildModel(categoryModel,products);
            list.add(customBuildModel);


        }

        adapter.notifyDataSetChanged();




    }

    private List<CategoryModel> getCategory(){

        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (OrderModel.Product productModel:orderPc.getPc().getProducts()){
            if (!isCategoryListHasCategory(categoryModelList, productModel.getProduct().getCategory())){

                categoryModelList.add(productModel.getProduct().getCategory());
            }
        }
        return categoryModelList;
    }

    private boolean isCategoryListHasCategory(List<CategoryModel> categoryModelList, CategoryModel categoryModel){
        for (CategoryModel model:categoryModelList){
            if (model.getId()==categoryModel.getId()){
                return true;
            }
        }
        return false;
    }


    private int getAmountProduct(int product_id){
        int amount = 0;
        for (int index = 0;index<orderPc.getPc().getProducts().size();index++){
            OrderModel.Product product= orderPc.getPc().getProducts().get(index);
            if (product.getProduct().getId()==product_id){
                amount++;
            }
        }
        return amount;
    }

    private boolean isProductFound(List<OrderModel.Product> products, OrderModel.Product product){
        for (OrderModel.Product model :products){
            if (model.getProduct().getId()==product.getProduct().getId()){
                return true;
            }
        }
        return false;
    }
}