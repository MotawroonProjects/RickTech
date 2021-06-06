package com.app.ricktech.uis.activity_language;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityLanguageBinding;
import com.app.ricktech.databinding.ActivitySplashBinding;
import com.app.ricktech.language.Language;

import io.paperdb.Paper;

public class LanguageActivity extends AppCompatActivity {
    private ActivityLanguageBinding binding;
    private String selectedLang="";
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        initView();
    }

    private void initView() {
        Paper.init(this);
        String lang = Paper.book().read("lang","ar");

        if (lang.equals("ar")){
            updateUiAr();

        }else if (lang.equals("en")){
            updateUiEn();

        }else {
            updateUiJer();

        }


        binding.cardAr.setOnClickListener(v -> {
            updateUiAr();
        });

        binding.cardEn.setOnClickListener(v -> {
            updateUiEn();
        });

        binding.cardJer.setOnClickListener(v -> {
            updateUiJer();
        });

        binding.btnConfirm.setOnClickListener(v -> {
            Paper.book().write(lang, selectedLang);
            Intent intent = getIntent();
            intent.putExtra("lang", selectedLang);
            setResult(RESULT_OK,intent);
            finish();
        });
    }

    private void updateUiAr(){
        selectedLang="ar";
        binding.cardAr.setCardBackgroundColor(ContextCompat.getColor(this,R.color.color1));
        binding.cardAr.setCardElevation(3.0f);

        binding.cardEn.setCardBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        binding.cardEn.setCardElevation(0.0f);

        binding.cardJer.setCardBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        binding.cardJer.setCardElevation(0.0f);
    }


    private void updateUiEn(){
        selectedLang="en";

        binding.cardAr.setCardBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        binding.cardAr.setCardElevation(0.0f);

        binding.cardEn.setCardBackgroundColor(ContextCompat.getColor(this,R.color.color1));
        binding.cardEn.setCardElevation(3.0f);

        binding.cardJer.setCardBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        binding.cardJer.setCardElevation(0.0f);
    }

    private void updateUiJer(){
        selectedLang="de";

        binding.cardAr.setCardBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        binding.cardAr.setCardElevation(0.0f);

        binding.cardEn.setCardBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        binding.cardEn.setCardElevation(0.0f);

        binding.cardJer.setCardBackgroundColor(ContextCompat.getColor(this,R.color.color1));
        binding.cardJer.setCardElevation(3.0f);
    }
}