package com.app.ricktech.uis.activity_verification_code;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityVerificationCodeBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.SliderModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
    private boolean sendOtp = false;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
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
            sendOtp = intent.getBooleanExtra("sendOtp", false);
        }
    }

    private void initView() {
        preferences = Preferences.getInstance();

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
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
                checkValidCode(code);
            } else {
                binding.edtCode.setError(getString(R.string.field_required));
            }

        });
        if (sendOtp){
            sendSmsCode();

        }else {
            startTimer();
        }
    }

    private void sendSmsCode() {

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

    private void startTimer() {
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


    private void checkValidCode(String code) {
        if (code.equals(userModel.getData().getEmail_activation_code())){
            preferences.create_update_userdata(this,userModel);
            setResult(RESULT_OK);
            finish();
        }else {
            Toast.makeText(this, R.string.inv_code, Toast.LENGTH_SHORT).show();
        }

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
