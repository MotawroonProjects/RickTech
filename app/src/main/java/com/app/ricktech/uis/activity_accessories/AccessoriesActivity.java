package com.app.ricktech.uis.activity_accessories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityAccessoriesBinding;
import com.app.ricktech.databinding.ActivityForgetPasswordBinding;
import com.app.ricktech.language.Language;

import io.paperdb.Paper;

public class AccessoriesActivity extends AppCompatActivity {
    private ActivityAccessoriesBinding binding;
    private String lang;



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
        binding.llBack.setOnClickListener(v -> {
            finish();
        });

    }
}