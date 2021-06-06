package com.app.ricktech.uis.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityHomeBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.uis.activity_home.fragments.Fragment_Cart;
import com.app.ricktech.uis.activity_home.fragments.Fragment_Home;
import com.app.ricktech.uis.activity_home.fragments.Fragment_Offers;
import com.app.ricktech.uis.activity_home.fragments.Fragment_Profile;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_Profile fragment_profile;
    private Fragment_Cart fragment_cart;
    private Fragment_Offers fragment_offers;
    private UserModel userModel;
    private String lang;
    private boolean backPressed= false;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);




        displayFragmentMain();


        updateFirebaseToken();
        binding.flNotification.setOnClickListener(v -> {
           /* Intent intent=new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);*/
        });
//        if (userModel != null) {
//            EventBus.getDefault().register(this);
//
//        }

        binding.bottomNavView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id){
                case R.id.offer:
                    displayFragmentOffer();
                    break;
                case R.id.cart:
                    displayFragmentCart();
                    break;
                case R.id.profile:
                    displayFragmentProfile();
                    break;
                default:
                    if (!backPressed){
                        displayFragmentMain();
                    }
                    break;
            }
            return true;
        });



    }


    public void displayFragmentMain() {
        try {
            if (fragment_home == null) {
                fragment_home = Fragment_Home.newInstance();
            }
            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_cart != null && fragment_cart.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_cart).commit();
            }

            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }

            if (fragment_home.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_home).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").commit();

            }
        } catch (Exception e) {
        }

    }

    public void displayFragmentOffer() {

        try {
            if (fragment_offers == null) {
                fragment_offers = Fragment_Offers.newInstance();
            }


            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }

            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_cart != null && fragment_cart.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_cart).commit();
            }

            if (fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_offers).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_offers, "fragment_offers").commit();

            }
            binding.setTitle(getString(R.string.offers));
        } catch (Exception e) {
        }
    }

    public void displayFragmentCart() {

        try {
            if (fragment_cart == null) {
                fragment_cart = Fragment_Cart.newInstance();
            }


            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }

            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }

            if (fragment_cart.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_cart).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_cart, "fragment_cart").commit();

            }
            binding.setTitle(getString(R.string.offers));
        } catch (Exception e) {
        }
    }


    public void displayFragmentProfile() {

        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }


            if (fragment_home != null && fragment_home.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_home).commit();
            }

            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_cart != null && fragment_cart.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_cart).commit();
            }

            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").commit();

            }
            binding.setTitle(getString(R.string.profile));
        } catch (Exception e) {
        }
    }



    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    private void getNotificationCount() {

    }

    @Override
    public void onBackPressed() {
        backPressed = true;
        binding.bottomNavView.setSelectedItemId(R.id.home);
        backPressed = false;

        if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
            finish();
        } else {
            displayFragmentMain();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void updateFirebaseToken() {
      /*  FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        try {
                            Api.getService(Tags.base_url)
                                    .updatePhoneToken(userModel.getUser().getToken(), token, userModel.getUser().getId(), "android")
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                userModel.getUser().setFirebaseToken(token);
                                                preferences.create_update_userdata(HomeActivity.this, userModel);

                                                Log.e("token", "updated successfully");
                                            } else {
                                                try {

                                                    Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            try {

                                                if (t.getMessage() != null) {
                                                    Log.e("errorToken2", t.getMessage());
                                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                        Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        } catch (Exception e) {

                        }
                    }
                });
*/
    }

    public void logout() {

        if (userModel==null){
            finish();
            return;
        }
      /*  ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .logout("Bearer " + userModel.getData().getToken())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                navigateToSignInActivity();
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
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
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
                });*/

    }


    private void navigateToSignInActivity() {
      /*  preferences.clear(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();*/
    }


}