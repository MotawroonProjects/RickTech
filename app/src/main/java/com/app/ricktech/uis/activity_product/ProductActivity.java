package com.app.ricktech.uis.activity_product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.ricktech.R;
import com.app.ricktech.adapters.CategoryAdapter;
import com.app.ricktech.adapters.ProductAdapter;
import com.app.ricktech.databinding.ActivityCategoriesBinding;
import com.app.ricktech.databinding.ActivityProductBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.uis.activity_product_detials.ProductDetialsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ProductActivity extends AppCompatActivity {
    private ActivityProductBinding binding;
    private String lang;
    private ProductAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<Object> list;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        initView();
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


    }


    public void show() {
        Intent intent=new Intent(this, ProductDetialsActivity.class);
        startActivity(intent);
    }
}