package com.app.ricktech.uis.separate_laptop_gaming_module.separate_laptop_gaming_brand;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.ricktech.R;
import com.app.ricktech.adapters.CategoryAdapter;
import com.app.ricktech.databinding.ActivitySeparatePcBrandBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.BrandDataModel;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.separate_laptop_gaming_module.separate_laptop_gaming_categories.SeparateLaptopGamingCategoriesActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeparateLaptopGamingBrandActivity extends AppCompatActivity {
    private ActivitySeparatePcBrandBinding binding;
    private String lang;
    private CategoryAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<BrandModel> list;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_separate_pc_brand);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "de");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CategoryAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(v -> finish());

        getBrand();

    }

    private void getBrand() {
        Api.getService(Tags.base_url)
                .getSeparateLaptopGamingBrand(lang)
                .enqueue(new Callback<BrandDataModel>() {
                    @Override
                    public void onResponse(Call<BrandDataModel> call, Response<BrandDataModel> response) {
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
                    public void onFailure(Call<BrandDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage()+"__");
                            binding.progBar.setVisibility(View.GONE);

                        } catch (Exception e) {

                        }
                    }
                });
    }


    public void setItemData(BrandModel brandModel) {
        Intent intent=new Intent(this, SeparateLaptopGamingCategoriesActivity.class);
        intent.putExtra("data", brandModel.getId()+"");
        startActivity(intent);
    }
}