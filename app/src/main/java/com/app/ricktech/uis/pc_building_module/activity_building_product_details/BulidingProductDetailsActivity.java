package com.app.ricktech.uis.pc_building_module.activity_building_product_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.app.ricktech.R;
import com.app.ricktech.adapters.DetialsAdapter;
import com.app.ricktech.databinding.ActivityBulidingProductDetailsBinding;
import com.app.ricktech.databinding.ActivityProductDetialsBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.ComponentModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SingleProductModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.ethanhua.skeleton.SkeletonScreen;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BulidingProductDetailsActivity extends AppCompatActivity {

    private ActivityBulidingProductDetailsBinding binding;
    private String lang;
    private DetialsAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<ComponentModel> list;
    private String product_id="";
    private SkeletonScreen skeletonScreen;
    private ProductModel productModel;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buliding_product_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        product_id = intent.getStringExtra("data");

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new DetialsAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());


        binding.shimmer.startShimmer();
        binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        getProductById();
    }

    private void getProductById() {
        Api.getService(Tags.base_url)
                .getProductById(lang,product_id)
                .enqueue(new Callback<SingleProductModel>() {
                    @Override
                    public void onResponse(Call<SingleProductModel> call, Response<SingleProductModel> response) {
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        //skeletonScreen.hide();
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            productModel = response.body().getData();
                            updateUi();

                        } else {
                            binding.shimmer.stopShimmer();
                            binding.shimmer.setVisibility(View.GONE);

                            //skeletonScreen.hide();


                        }


                    }

                    @Override
                    public void onFailure(Call<SingleProductModel> call, Throwable t) {
                        try {
                            binding.shimmer.stopShimmer();
                            binding.shimmer.setVisibility(View.GONE);

                            //skeletonScreen.hide();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void updateUi() {
        binding.llData.setVisibility(View.VISIBLE);
        binding.setModel(productModel);
        list.clear();
        list.addAll(productModel.getComponents());
        adapter.notifyDataSetChanged();
    }
}