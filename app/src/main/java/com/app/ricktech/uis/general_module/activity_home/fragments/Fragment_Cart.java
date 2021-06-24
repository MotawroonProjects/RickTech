package com.app.ricktech.uis.general_module.activity_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.ricktech.R;
import com.app.ricktech.databinding.FragmentCartBinding;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.uis.general_module.activity_checkout.CheckoutActivity;
import com.app.ricktech.uis.general_module.activity_home.HomeActivity;


import io.paperdb.Paper;

public class Fragment_Cart extends Fragment {

    private HomeActivity activity;
    private FragmentCartBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private ActivityResultLauncher<Intent> launcher;



    public static Fragment_Cart newInstance() {
        return new Fragment_Cart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        initView();

        return binding.getRoot();
    }


    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");

        binding.btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(activity, CheckoutActivity.class);
            launcher.launch(intent);
        });
    }

    private void clearCart() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()== Activity.RESULT_OK){
                    clearCart();
                }
            }


        });
    }
}
