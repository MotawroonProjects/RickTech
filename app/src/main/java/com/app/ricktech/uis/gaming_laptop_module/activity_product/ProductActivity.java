package com.app.ricktech.uis.gaming_laptop_module.activity_product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.ricktech.R;
import com.app.ricktech.adapters.ProductAdapter;
import com.app.ricktech.databinding.ActivityProductBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.models.ProductDataModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.general_module.activity_product_detials.ProductDetialsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private ActivityProductBinding binding;
    private String lang;
    private ProductAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<ProductModel> list;
    private BrandModel model;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        model = (BrandModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(model);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new ProductAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());

        getProducts();

    }

    private void getProducts() {
        Log.e("id", model.getId()+"__");
        Api.getService(Tags.base_url)
                .getGamingProductByBrandId(lang,model.getId()+"")
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        Log.e("size", response.body().getData().size()+"_");
                        if (response.isSuccessful() && response.body() != null&&response.body().getStatus()==200 ) {
                            if (response.body().getData().size() > 0) {
                                list.clear();
                                list.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                binding.tvNoData.setVisibility(View.GONE);
                            } else{
                                binding.tvNoData.setVisibility(View.VISIBLE);

                            }

                        } else {
                            binding.progBar.setVisibility(View.GONE);

                        }


                    }

                    @Override
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage()+"__");
                            binding.progBar.setVisibility(View.GONE);

                        } catch (Exception e) {

                        }
                    }
                });
    }


    public void setItemData(ProductModel productModel) {
        Intent intent=new Intent(this, ProductDetialsActivity.class);
        intent.putExtra("data", productModel.getId()+"");
        startActivity(intent);
    }
}