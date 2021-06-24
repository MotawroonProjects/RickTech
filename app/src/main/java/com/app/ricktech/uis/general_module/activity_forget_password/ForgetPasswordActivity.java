package com.app.ricktech.uis.general_module.activity_forget_password;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityForgetPasswordBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.general_module.activity_reset_password.ResetPasswordActivity;
import com.app.ricktech.uis.general_module.activity_verification_code.VerificationCodeActivity;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    private ActivityForgetPasswordBinding binding;
    private String lang;
    private ActivityResultLauncher<Intent> launcher;



    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()==RESULT_OK&&result.getData()!=null){
                UserModel model = (UserModel) result.getData().getSerializableExtra("data");
                Intent intent = new Intent(ForgetPasswordActivity.this, ResetPasswordActivity.class);
                intent.putExtra("data", model);
                startActivity(intent);
                finish();
            }
        });


        binding.btnNext.setOnClickListener(view -> {
           String email = binding.edtEmail.getText().toString().trim();
           if (!email.isEmpty()&&
                   Patterns.EMAIL_ADDRESS.matcher(email).matches()
           ){
               binding.edtEmail.setError(null);
               Common.CloseKeyBoard(this,binding.edtEmail);
               checkEmail(email);
               //navigateToVerifiedActivity(email);
           }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
               binding.edtEmail.setError(getString(R.string.inv_email));

           }else{
               binding.edtEmail.setError(getString(R.string.field_req));

           }
        });




    }

    private void checkEmail(String email) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .checkEmail(email)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null ) {
                                if (response.body().getStatus() == 200){
                                    navigateToVerifiedActivity(response.body());
                                }else if (response.body().getStatus() == 404){
                                    Toast.makeText(ForgetPasswordActivity.this,getString(R.string.inc_email),Toast.LENGTH_SHORT).show();

                                }
                            }

                        } else {
                            dialog.dismiss();
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
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
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

    private void navigateToVerifiedActivity(UserModel model) {
        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra("data", model);
        intent.putExtra("type", "forgetpassword");
        launcher.launch(intent);
    }

}