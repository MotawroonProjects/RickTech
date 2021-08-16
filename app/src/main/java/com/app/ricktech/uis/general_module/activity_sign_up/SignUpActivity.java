package com.app.ricktech.uis.general_module.activity_sign_up;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivitySignUpBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.SignUpModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.general_module.activity_home.HomeActivity;
import com.app.ricktech.uis.general_module.activity_login.LoginActivity;
import com.app.ricktech.uis.general_module.activity_verification_code.VerificationCodeActivity;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private String lang;
    private SignUpModel model;
    private Preferences preferences;
    private UserModel userModel;
    private ActivityResultLauncher<Intent> launcher;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        preferences=Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "de");
        binding.setLang(lang);
        model = new SignUpModel();
        binding.setModel(model);
        binding.tvLogin.setText(Html.fromHtml(getString(R.string.have_account)));
        binding.btnSignUp.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                Common.CloseKeyBoard(this, binding.edtUsername);
                if (userModel==null){
                    signUp();
                }else {
                   updateProfile();
                }

            }
        });
        binding.tvLogin.setOnClickListener(view -> {
         navigateToLoginActivity();
        });


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()==RESULT_OK){
                navigateToHomeActivity();
            }
        });


        if (userModel!=null){
            binding.llSignUp.setVisibility(View.GONE);
            binding.tvLogin.setVisibility(View.INVISIBLE);
            binding.btnSignUp.setText(getString(R.string.update_profile));
            model.setEmail(userModel.getData().getEmail());
            model.setName(userModel.getData().getName());
            model.setPassword("123456");
            model.setRe_password("123456");

        }


    }

    private void signUp() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .signUp(model.getName(), model.getPassword(),model.getEmail(),"android")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                navigateToVerificationCodeActivity(response.body());
                            } else if (response.body().getStatus() == 403) {
                                Toast.makeText(SignUpActivity.this, R.string.email_found, Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 402) {
                                Toast.makeText(SignUpActivity.this, R.string.un_found, Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            Log.e("code", response.code()+"__");
                            try {
                                Log.e("errorbody", response.errorBody().string()+"__");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            Log.e("error", t.getMessage()+"__");

                        }catch (Exception e){}

                    }
                });
    }
    private void updateProfile() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateProfile("Bearer "+userModel.getData().getToken(),model.getName(), model.getEmail())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                userModel = response.body();
                                preferences.create_update_userdata(SignUpActivity.this,response.body());
                                setResult(RESULT_OK);
                                finish();
                            } else if (response.body().getStatus() == 403) {
                                Toast.makeText(SignUpActivity.this, R.string.email_found, Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 402) {
                                Toast.makeText(SignUpActivity.this, R.string.un_found, Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            Log.e("code", response.code()+"__");
                            try {
                                Log.e("errorbody", response.errorBody().string()+"__");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            Log.e("error", t.getMessage()+"__");

                        }catch (Exception e){}

                    }
                });
    }


    private void navigateToVerificationCodeActivity(UserModel userModel) {
        Log.e("dd", userModel.getData().getEmail_activation_code()+"__");
        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra("data", userModel);
        intent.putExtra("type", "sign_up");
        launcher.launch(intent);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        if (userModel==null){
            navigateToLoginActivity();

        }else {
            finish();
        }
    }
}