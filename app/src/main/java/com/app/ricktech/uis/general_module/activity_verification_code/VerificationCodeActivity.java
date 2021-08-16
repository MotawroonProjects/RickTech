package com.app.ricktech.uis.general_module.activity_verification_code;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityVerificationCodeBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends AppCompatActivity {
    private ActivityVerificationCodeBinding binding;
    private String lang;
    private CountDownTimer timer;
    private String smsCode;
    private Preferences preferences;
    private boolean canSend = false;
    private String type =""; ///login or sign_up ore forgetpassword
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification_code);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            userModel = (UserModel) intent.getSerializableExtra("data");
            type = intent.getStringExtra("type");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();

        Paper.init(this);
        lang = Paper.book().read("lang", "de");

        binding.setEmail(userModel.getData().getEmail());
        binding.tvResend.setOnClickListener(view -> {
            if (canSend){
                sendSmsCode();
            }
        });
        binding.btnConfirm.setOnClickListener(view -> {
            String code = binding.edtCode.getText().toString().trim();
            if (!code.isEmpty()) {
                binding.edtCode.setError(null);
                Common.CloseKeyBoard(this, binding.edtCode);
                if (type.equals("forgetpassword")){
                    checkValidCodeForForgetPassword(code);
                }else {
                    checkValidCode(code);

                }
            } else {
                binding.edtCode.setError(getString(R.string.field_required));
            }

        });
        if (type.equals("login")){
            sendSmsCode();

        }else if (type.equals("sign_up")){
            startTimer();
        }else {
            sendSmsCode();
        }
    }

    private void sendSmsCode()
    {

        startTimer();

        Api.getService(Tags.base_url)
                .sendSmsCode(lang,"Bearer "+userModel.getData().getToken())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                        if (response.isSuccessful() && response.body() != null ) {

                            if (response.body().getStatus() == 200){
                                userModel = response.body();
                            }else {

                            }

                        } else {


                        }


                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage()+"__");

                        } catch (Exception e) {

                        }
                    }
                });



    }

    private void startTimer()
    {
        canSend = false;
        binding.tvResend.setEnabled(false);
        timer = new CountDownTimer(120 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                String time = format.format(new Date(l));
                binding.tvResendCode.setText(time);
            }

            @Override
            public void onFinish() {
                canSend = true;
                binding.tvResendCode.setText("00:00");
                binding.tvResend.setEnabled(true);
            }
        };
        timer.start();
    }

    private void checkValidCode(String code)
    {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .confirmEmail("Bearer " + userModel.getData().getToken(),code)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null ) {
                                if (response.body().getStatus() == 200){
                                    preferences.create_update_userdata(VerificationCodeActivity.this,response.body());
                                    setResult(RESULT_OK);
                                    finish();
                                }else if (response.body().getStatus() == 403){
                                    Toast.makeText(VerificationCodeActivity.this, R.string.inv_code, Toast.LENGTH_SHORT).show();

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

    private void checkValidCodeForForgetPassword(String code)
    {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .checkEmailForgetPasswordValidCode("Bearer " + userModel.getData().getToken(),code)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200){
                                    Intent intent = getIntent();
                                    intent.putExtra("data", response.body());
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }else if (response.body().getStatus() == 402){
                                    Toast.makeText(VerificationCodeActivity.this, R.string.inv_code, Toast.LENGTH_SHORT).show();
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





    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
