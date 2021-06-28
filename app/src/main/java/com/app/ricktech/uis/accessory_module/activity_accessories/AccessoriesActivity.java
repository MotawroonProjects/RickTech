package com.app.ricktech.uis.accessory_module.activity_accessories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.ricktech.R;
import com.app.ricktech.adapters.AccessoryAdapter;
import com.app.ricktech.adapters.CategoryAdapter;
import com.app.ricktech.adapters.SubBuildingAdapter;
import com.app.ricktech.databinding.ActivityAccessoriesBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.AccessoryDataModel;
import com.app.ricktech.models.CategoryBuildingDataModel;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.accessory_module.activity_accessory_products.AccessoryProductsActivity;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccessoriesActivity extends AppCompatActivity {
    private ActivityAccessoriesBinding binding;
    private String lang;
    private AccessoryAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<CategoryModel> list;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_accessories);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new AccessoryAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(v -> finish());

        binding.llBack.setOnClickListener(v -> {
            finish();
        });
        getCategory();

    }

    private void getCategory() {
        Api.getService(Tags.base_url)
                .getAccessory(lang)
                .enqueue(new Callback<AccessoryDataModel>() {
                    @Override
                    public void onResponse(Call<AccessoryDataModel> call, Response<AccessoryDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            if (response.body().getData().size() > 0) {
                                list.clear();
                                list.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                binding.tvNoData.setVisibility(View.GONE);

                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);


                            }

                        } else {
                            binding.progBar.setVisibility(View.GONE);

                        }


                    }

                    @Override
                    public void onFailure(Call<AccessoryDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");
                            binding.progBar.setVisibility(View.GONE);

                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void setItemData(int adapterPosition, CategoryModel categoryModel) {
        Intent intent = new Intent(this, AccessoryProductsActivity.class);
        intent.putExtra("data", categoryModel.getId()+"");
        startActivity(intent);

    }

}