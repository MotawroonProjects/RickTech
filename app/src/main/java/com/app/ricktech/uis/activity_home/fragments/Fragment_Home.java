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

import com.app.ricktech.databinding.FragmentHomeBinding;

import com.app.ricktech.models.SliderModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class Fragment_Home extends Fragment {

    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
   /* private SliderAdapter sliderAdapter;
   */
   private List<SliderModel> sliderModelList;

    private Timer timer;
    private TimerTask timerTask;

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();

        return binding.getRoot();
    }


    private void initView() {
        sliderModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        getSlider();
    }


    private void getSlider() {
     /*   Api.getService(Tags.base_url)
                .getSlider()
                .enqueue(new Callback<SliderDataModel>() {
                    @Override
                    public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
                        binding.progBarSlider.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData().size() > 0) {
                                updateSliderUi(response.body().getData());

                            } else {
                                binding.flSlider.setVisibility(View.GONE);

                            }

                        } else {
                            binding.flSlider.setVisibility(View.GONE);
                            binding.progBarSlider.setVisibility(View.GONE);

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SliderDataModel> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                            binding.progBarSlider.setVisibility(View.GONE);
                        } catch (Exception e) {

                        }
                    }
                });*/
    }


    private void updateSliderUi(List<SliderModel> data) {
      /*  sliderModelList.addAll(data);
        sliderAdapter = new SliderAdapter(sliderModelList, activity,this);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(90, 8, 90, 8);
        binding.pager.setPageMargin(24);
        binding.pager.setVisibility(View.VISIBLE);

        if (data.size() > 1) {
            timer = new Timer();
            timerTask = new MyTask();
            timer.scheduleAtFixedRate(timerTask, 6000, 6000);
        }*/
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }

    }

}
