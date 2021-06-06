package com.app.ricktech.uis.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.ricktech.R;
import com.app.ricktech.databinding.FragmentCartBinding;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.uis.activity_home.HomeActivity;

import io.paperdb.Paper;

public class Fragment_Cart extends Fragment {

    private HomeActivity activity;
    private FragmentCartBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;



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

    }













}
