package com.app.ricktech.uis.general_module.activity_web_view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityWebViewBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.NotificationCount;
import com.app.ricktech.models.SettingDataModel;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity {

    private ActivityWebViewBinding binding;
    private String lang;
    private String type;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        type=intent.getStringExtra("type");

    }

    private void initView() {
        if(type.equals("0")){
            binding.setTitle(getString(R.string.about_us));
        }

        Paper.init(this);
        lang = Paper.book().read("lang", "de");
        binding.setLang(lang);
        binding.flBack.setOnClickListener(v -> {

            finish();
        });


        //getSettings();
        String url = Tags.base_url+"settings";
        setUpWebView(url);

    }

    private void setUpWebView(String url){
        binding.progBar.setVisibility(View.VISIBLE);

        binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        binding.webView.getSettings().setAllowContentAccess(true);
        binding.webView.getSettings().setAllowFileAccess(true);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        binding.webView.loadUrl(url);

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                binding.progBar.setVisibility(View.GONE);

            }


        });

    }
    private void getSettings(){
        Api.getService(Tags.base_url)
                .getSetting(lang)
                .enqueue(new Callback<SettingDataModel>() {
                    @Override
                    public void onResponse(Call<SettingDataModel> call, Response<SettingDataModel> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                if (type.equals("0")){
                                    String url = response.body().getData().getAbout_us_link();
                                    if (url!=null){
                                        setUpWebView(url);
                                    }else {
                                        binding.progBar.setVisibility(View.GONE);
                                        Toast.makeText(WebViewActivity.this, R.string.inv_url, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SettingDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

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