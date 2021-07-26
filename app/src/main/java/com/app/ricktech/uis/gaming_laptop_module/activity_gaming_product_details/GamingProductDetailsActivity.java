package com.app.ricktech.uis.gaming_laptop_module.activity_gaming_product_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.ricktech.R;
import com.app.ricktech.adapters.DetialsAdapter;
import com.app.ricktech.databinding.ActivityBulidingProductDetailsBinding;
import com.app.ricktech.databinding.ActivityGamingProductDetailsBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.models.ComponentModel;
import com.app.ricktech.models.ManageCartModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SingleProductModel;
import com.app.ricktech.models.SuggestionGameDataModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.pc_building_module.activity_building.BulidingActivity;
import com.app.ricktech.uis.pc_building_module.activity_games.GamesActivity;
import com.ethanhua.skeleton.SkeletonScreen;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamingProductDetailsActivity extends AppCompatActivity {
    private ActivityGamingProductDetailsBinding binding;
    private String lang;
    private DetialsAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<ComponentModel> list;
    private String product_id="";
    private SkeletonScreen skeletonScreen;
    private ProductModel productModel;
    private ManageCartModel manageCartModel;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gaming_product_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        product_id = intent.getStringExtra("data");

    }

    private void initView() {
        manageCartModel = ManageCartModel.getInstance();
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

        binding.flAddToCart.setOnClickListener(v -> {
            CartModel.SingleProduct product = new CartModel.SingleProduct(productModel.getId(),productModel.getTrans_title(),productModel.getMain_image(),1, productModel.getPrice());
            manageCartModel.addSingleProduct(this,product);
            Toast.makeText(this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
        });

        binding.shimmer.startShimmer();
        binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        getProductById();

        binding.btnCompare.setOnClickListener(v -> compare());
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

    private void compare(){
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .compareGaming(lang,product_id)
                .enqueue(new Callback<SuggestionGameDataModel>() {
                    @Override
                    public void onResponse(Call<SuggestionGameDataModel> call, Response<SuggestionGameDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null ) {
                                if (response.body().getStatus() == 200)
                                {
                                    Intent intent = new Intent(GamingProductDetailsActivity.this, GamesActivity.class);
                                    intent.putExtra("data", (Serializable) response.body().getData());
                                    startActivity(intent);

                                }else if(response.body().getStatus() == 403){
                                    Toast.makeText(GamingProductDetailsActivity.this, R.string.no_games, Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                dialog.dismiss();
                            }

                        } else {
                            dialog.dismiss();

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<SuggestionGameDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }
}