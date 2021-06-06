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
import com.app.ricktech.databinding.FragmentOffersBinding;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.uis.activity_home.HomeActivity;

import java.util.TimerTask;

import io.paperdb.Paper;

public class Fragment_Offers extends Fragment {

    private HomeActivity activity;
    private FragmentOffersBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;



    public static Fragment_Offers newInstance() {
        return new Fragment_Offers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
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










    public class MyTask extends TimerTask {
        @Override
        public void run() {
            /*activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });*/

        }
    }



}
