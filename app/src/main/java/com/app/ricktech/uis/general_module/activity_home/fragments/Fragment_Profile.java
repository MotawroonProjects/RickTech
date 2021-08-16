package com.app.ricktech.uis.general_module.activity_home.fragments;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.ricktech.R;
import com.app.ricktech.databinding.FragmentProfileBinding;

import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.uis.general_module.activity_home.HomeActivity;
import com.app.ricktech.uis.general_module.activity_language.LanguageActivity;
import com.app.ricktech.uis.general_module.activity_sign_up.SignUpActivity;
import com.app.ricktech.uis.orders_module.activity_orders.OrdersActivity;
import com.app.ricktech.uis.saved_build_module.activity_saving_build.SavingBuildActivity;
import com.app.ricktech.uis.general_module.activity_web_view.WebViewActivity;


import io.paperdb.Paper;

public class Fragment_Profile extends Fragment {

    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(userModel);


        if(Preferences.getInstance().getDarkMode(activity).equals("yes")){
          binding.switchtheme.setChecked(true);
        }
        else {
            binding.switchtheme.setChecked(false);}
        binding.llLogout.setOnClickListener(v -> {
            activity.logout();
        });

        binding.llLanguage.setOnClickListener(v -> {
            req=1;
            Intent intent = new Intent(activity, LanguageActivity.class);

            launcher.launch(intent);

        });

        binding.llEdit.setOnClickListener(v -> {
            req=2;
            Intent intent = new Intent(activity, SignUpActivity.class);

            launcher.launch(intent);


        });

        binding.switchtheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.setDarkMode(activity,"yes");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    preferences.setDarkMode(activity,"No");

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


        binding.imageEdit.setOnClickListener(v -> {
            req=2;
            Intent intent = new Intent(activity, SignUpActivity.class);

            launcher.launch(intent);


        });


        binding.llAbout.setOnClickListener(v -> {
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent);
        });

        binding.llSaved.setOnClickListener(v -> {
            Intent intent = new Intent(activity, SavingBuildActivity.class);
            startActivity(intent);
        });

        binding.llCurrentOrder.setOnClickListener(v -> {
            navigateToOrderActivity(1);
        });

        binding.llPreviousOrder.setOnClickListener(v -> {
            navigateToOrderActivity(2);
        });


    }


    private void navigateToOrderActivity(int type) {
        Intent intent = new Intent(activity, OrdersActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);

    }
    
    @Override
    public void onAttach(@NonNull  Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (req==1){
                    if (result.getResultCode()== Activity.RESULT_OK&&result.getData()!=null){
                        String lang = result.getData().getStringExtra("lang");
                        activity.refreshActivity(lang);
                    }
                }else if (req==2){
                    if (result.getResultCode()== Activity.RESULT_OK){
                        userModel = preferences.getUserData(activity);
                        binding.setModel(userModel);
                    }
                }

            }
        });
    }


}
