package com.app.ricktech.uis.activity_forget_password;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityForgetPasswordBinding;
import com.app.ricktech.databinding.ActivityLoginBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.LoginModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.share.Common;
import com.app.ricktech.uis.activity_verification_code.VerificationCodeActivity;

import io.paperdb.Paper;

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

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()==RESULT_OK){

                    finish();
                }
            }
        });


        binding.btnNext.setOnClickListener(view -> {
           String email = binding.edtEmail.getText().toString().trim();
           if (!email.isEmpty()&&
                   Patterns.EMAIL_ADDRESS.matcher(email).matches()
           ){
               binding.edtEmail.setError(null);
               Common.CloseKeyBoard(this,binding.edtEmail);
               navigateToVerifiedActivity(email);
           }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
               binding.edtEmail.setError(getString(R.string.inv_email));

           }else{
               binding.edtEmail.setError(getString(R.string.field_req));

           }
        });




    }

    private void navigateToVerifiedActivity(String email) {
        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra("email", email);
        launcher.launch(intent);
    }

}