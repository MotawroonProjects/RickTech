package com.app.ricktech.uis.general_module.activity_home;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityHomeBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.NotificationCount;
import com.app.ricktech.models.StatusResponse;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Cart;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Home;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Offers;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Profile;
import com.app.ricktech.uis.general_module.activity_language.LanguageActivity;
import com.app.ricktech.uis.general_module.activity_login.LoginActivity;
import com.app.ricktech.uis.general_module.activity_notifications.NotificationActivity;
import com.app.ricktech.uis.general_module.activity_search.SearchActivity;
import com.app.ricktech.uis.separate_gaming_module.separate_gaming_brand.SeparateGamingBrandActivity;
import com.app.ricktech.uis.separate_laptop_gaming_module.separate_laptop_gaming_brand.SeparateLaptopGamingBrandActivity;
import com.app.ricktech.uis.separate_not_book_module.separate_note_book_brand.SeparateNoteBookBrandActivity;
import com.app.ricktech.uis.separate_pc_module.separate_pc_brand.SeparatePcBrandActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private boolean backPressed = false;
    private ActionBarDrawerToggle toggle;
    private ActivityResultLauncher<Intent> launcher;
    private Bundle savedInstanceState;
    private int req;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
      //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        initView();


    }



    private void initView() {


        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "de");
        binding.setLang(lang);

        if (userModel != null) {
            binding.setModel(userModel);
        }


        displayFragmentMain();
        binding.bottomNavView.setSelectedItemId(R.id.home);

        toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);
        toggle.setDrawerIndicatorEnabled(false);

        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);

        toggle.setToolbarNavigationClickListener(v -> {
            if (binding.drawer.isDrawerVisible(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START);
            } else {
                binding.drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.syncState();
        binding.toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.flNotification.setOnClickListener(v -> {
            binding.setNotCount(0);
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
        if (userModel != null) {
            //EventBus.getDefault().register(this);
            updateFirebaseToken();
            getUnreadNotificationCount();

        }
           launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (req==1){
                    if (result.getResultCode()== Activity.RESULT_OK&&result.getData()!=null){
                        String lang = result.getData().getStringExtra("lang");
                      refreshActivity(lang);
                    }
                }
            }
           });
        binding.imLanguage.setOnClickListener(v -> {
            req=1;
            Intent intent = new Intent(HomeActivity.this, LanguageActivity.class);

            launcher.launch(intent);
        });

        binding.llSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("data", "all");
            startActivity(intent);

        });
        binding.bottomNavView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
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
                    if (!backPressed) {
                        displayFragmentMain();
                    }
                    break;
            }
            return true;
        });


        binding.llParts.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("data", "part");
            startActivity(intent);
        });

        binding.llRickTick.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("data", "complete");
            startActivity(intent);
        });

        binding.llPc.setOnClickListener(v -> {
            Intent intent = new Intent(this, SeparatePcBrandActivity.class);
            startActivity(intent);
        });

        binding.llGamingPcs.setOnClickListener(v -> {
            Intent intent = new Intent(this, SeparateGamingBrandActivity.class);
            startActivity(intent);
        });

        binding.llLaptopGamingPcs.setOnClickListener(v -> {
            Intent intent = new Intent(this, SeparateLaptopGamingBrandActivity.class);
            startActivity(intent);
        });

        binding.llNoteBook.setOnClickListener(v -> {
            Intent intent = new Intent(this, SeparateNoteBookBrandActivity.class);
            startActivity(intent);
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
                fragment_cart.updateCart();
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


    private void getUnreadNotificationCount() {
        Api.getService(Tags.base_url)
                .getUnreadNotificationCount(lang, "Bearer " + userModel.getData().getToken())
                .enqueue(new Callback<NotificationCount>() {
                    @Override
                    public void onResponse(Call<NotificationCount> call, Response<NotificationCount> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                binding.setNotCount(response.body().getData());
                            }

                        } else {
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
                    public void onFailure(Call<NotificationCount> call, Throwable t) {
                        try {
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

    public void refreshActivityTheme() {

        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);





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


    @Override
    public void onBackPressed() {

        if (binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START);
        }else {
            backPressed = true;
            binding.bottomNavView.setSelectedItemId(R.id.home);
            backPressed = false;

            if (fragment_home != null && fragment_home.isAdded() && fragment_home.isVisible()) {
                finish();
            } else {
                displayFragmentMain();
            }
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
        FirebaseMessaging.getInstance()
                .getToken()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        try {
                            Api.getService(Tags.base_url)
                                    .updateFirebaseToken(userModel.getData().getId(), token, "android")
                                    .enqueue(new Callback<StatusResponse>() {
                                        @Override
                                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                            if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                                                userModel.getData().setFirebase_token(token);
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
                                        public void onFailure(Call<StatusResponse> call, Throwable t) {
                                            try {

                                                if (t.getMessage() != null) {
                                                    Log.e("errorToken2", t.getMessage());

                                                }

                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void logout() {

        if (userModel == null) {
            finish();
            return;
        }
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
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
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                manager.cancel(Tags.not_tag, Tags.not_id);
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
                });

    }


    private void navigateToSignInActivity() {
        preferences.clear(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}