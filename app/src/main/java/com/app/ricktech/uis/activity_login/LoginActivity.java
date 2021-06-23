package com.app.ricktech.uis.activity_login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityLoginBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.LoginModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.activity_forget_password.ForgetPasswordActivity;
import com.app.ricktech.uis.activity_home.HomeActivity;
import com.app.ricktech.uis.activity_sign_up.SignUpActivity;
import com.app.ricktech.uis.activity_verification_code.VerificationCodeActivity;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private String lang;
    private LoginModel loginModel;
    private Preferences preferences;
    private ActivityResultLauncher<Intent> launcher;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        loginModel = new LoginModel();
        binding.setModel(loginModel);
        binding.tvSignUp.setText(Html.fromHtml(getString(R.string.create_account)));

        binding.btnLogin.setOnClickListener(view -> {
            if (loginModel.isDataValid(this)) {
                Common.CloseKeyBoard(this, binding.edtUsername);
                binding.btnLogin.setEnabled(false);
                login();
            }
        });


        binding.tvSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        binding.tvForgetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()==RESULT_OK){
                navigateToHomeActivity();
            }
        });


    }

    private void animateLoginButton(int width, int reqWidth) {
        ValueAnimator animator = ValueAnimator.ofInt(width, reqWidth);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = binding.btnLogin.getLayoutParams();
            params.width = value;
            binding.btnLogin.requestLayout();
        });
        animator.setDuration(250);
        animator.start();
    }

    private int getFabDimens() {
        return (int) getResources().getDimension(R.dimen.fab_size);
    }

    private int getFabWidth() {
        return (int) getResources().getDimension(R.dimen.fab_width);
    }

    private void fadeOutTextAndShowProgressDialog() {

        binding.tvText.animate().alpha(0)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        showProgressDialog();
                    }
                }).start();
    }
    private void fadeOutProgressDialog() {

        binding.progBar.animate().alpha(0)
                .setDuration(250).start();
    }

    private void fadeInTextAndHideProgressDialog() {

        binding.tvText.animate().alpha(1)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        hideProgressDialog();
                    }
                }).start();
    }

    private void showProgressDialog() {
        binding.progBar.setAlpha(1);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        binding.progBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressDialog() {
        binding.progBar.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void revealButton(){
        fadeOutProgressDialog();
        binding.reval.setVisibility(View.VISIBLE);
        int cx = binding.reval.getWidth();
        int cy = binding.reval.getHeight();

        int x = (int) (getFabDimens()/2 + binding.btnLogin.getX());
        int y = (int) (getFabDimens()/2 + binding.btnLogin.getY());


        float radius = Math.max(cx,cy)*1.3f;
        Animator animator = ViewAnimationUtils.createCircularReveal(binding.reval,x,y,getFabDimens(),radius);
        animator.setDuration(1000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                navigateToHomeActivity();
            }
        });
        animator.start();
    }




    private void login() {
        animateLoginButton(getFabWidth(), getFabDimens());
        fadeOutTextAndShowProgressDialog();

        Api.getService(Tags.base_url)
                .login(loginModel.getUser_name(), loginModel.getPassword())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                preferences.create_update_userdata(LoginActivity.this,response.body());

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    revealButton();
                                }else {
                                    navigateToHomeActivity();
                                }
                            } else if (response.body().getStatus() == 404) {
                                onFailed(getString(R.string.us_pas_inc));


                            } else if (response.body().getStatus() == 406) {
                                onFailed(getString(R.string.user_blocked));

                            } else if (response.body().getStatus() == 407) {

                                navigateToVerificationCode(response.body());



                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        onFailed("");


                    }
                });
    }


    private void navigateToVerificationCode(UserModel userModel) {
        Log.e("code", userModel.getData().getEmail_activation_code()+"_");
        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra("data", userModel);
        intent.putExtra("sendOtp", false);

        launcher.launch(intent);

    }

    protected void onFailed(String msg){
        if (!msg.isEmpty()){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        animateLoginButton(getFabDimens(),getFabWidth());
        fadeOutProgressDialog();
        fadeInTextAndHideProgressDialog();
        binding.btnLogin.setEnabled(true);
    }
    private void navigateToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}