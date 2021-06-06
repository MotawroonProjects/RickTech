package com.app.ricktech.uis.activity_sign_up;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityLoginBinding;
import com.app.ricktech.databinding.ActivitySignUpBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.LoginModel;
import com.app.ricktech.models.SignUpModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.share.Common;
import com.app.ricktech.uis.activity_forget_password.ForgetPasswordActivity;
import com.app.ricktech.uis.activity_home.HomeActivity;
import com.app.ricktech.uis.activity_login.LoginActivity;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private String lang;
    private SignUpModel model;
    private Preferences preferences;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        preferences=Preferences.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        model = new SignUpModel();
        binding.setModel(model);
        binding.tvLogin.setText(Html.fromHtml(getString(R.string.have_account)));
        binding.btnSignUp.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                Common.CloseKeyBoard(this, binding.edtUsername);
                signUp();
            }
        });
        binding.tvLogin.setOnClickListener(view -> {
         navigateToLoginActivity();
        });





    }

    private void signUp() {

    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        navigateToLoginActivity();
    }
}