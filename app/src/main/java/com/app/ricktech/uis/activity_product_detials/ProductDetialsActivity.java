package com.app.ricktech.uis.activity_product_detials;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.ricktech.R;
import com.app.ricktech.adapters.DetialsAdapter;
import com.app.ricktech.adapters.ProductAdapter;
import com.app.ricktech.databinding.ActivityProductBinding;
import com.app.ricktech.databinding.ActivityProductDetialsBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ProductDetialsActivity extends AppCompatActivity {
    private ActivityProductDetialsBinding binding;
    private String lang;
    private DetialsAdapter adapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detials);
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
        adapter = new DetialsAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());


    }


}