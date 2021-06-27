package com.app.ricktech.uis.pc_building_module.activity_building_products;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.ricktech.R;
import com.app.ricktech.adapters.DetialsAdapter;
import com.app.ricktech.adapters.ProductAdapter;
import com.app.ricktech.databinding.ActivityBulidingProductDetailsBinding;
import com.app.ricktech.databinding.ActivityProductBuildingBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.AddBuildModel;
import com.app.ricktech.models.ComponentModel;
import com.app.ricktech.models.ProductDataModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.pc_building_module.activity_building_product_details.BulidingProductDetailsActivity;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductBuildingActivity extends AppCompatActivity {

    private ActivityProductBuildingBinding binding;
    private String lang;
    private ProductAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<ProductModel> list;
    private String category_id = "";
    private int selectedPos = -1;
    private ProductModel productModel;
    private ActivityResultLauncher<Intent> launcher;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_building);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        category_id = intent.getStringExtra("data");
        Log.e("id", category_id+"__");
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new ProductAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());


        getProductByCategoryId();

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK){
                    Intent intent = getIntent();
                    if (productModel!=null){
                        intent.putExtra("data", productModel);
                    }
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void getProductByCategoryId() {
        Api.getService(Tags.base_url)
                .getProductByCategoryIdBuilding(lang,category_id)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

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

    public void setItemData(int adapterPosition, ProductModel productModel) {
        selectedPos = adapterPosition;
        this.productModel = productModel;
        Intent intent = new Intent(this, BulidingProductDetailsActivity.class);
        intent.putExtra("data", productModel.getId()+"");
        launcher.launch(intent);


    }
}