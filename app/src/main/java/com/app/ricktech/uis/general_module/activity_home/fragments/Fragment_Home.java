package com.app.ricktech.uis.general_module.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.ricktech.R;

import com.app.ricktech.adapters.SliderAdapter;
import com.app.ricktech.databinding.FragmentHomeBinding;

import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SliderModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.gaming_laptop_module.activity_categories.CategoriesActivity;
import com.app.ricktech.uis.activity_accessories.AccessoriesActivity;
import com.app.ricktech.uis.general_module.activity_home.HomeActivity;
import com.app.ricktech.uis.general_module.activity_product_detials.ProductDetialsActivity;
import com.app.ricktech.uis.pc_building_module.activity_building.BulidingActivity;
import com.ethanhua.skeleton.ViewSkeletonScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {

    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private SliderAdapter sliderAdapter;
    private List<SliderModel.Data> sliderModelList;
    private ViewSkeletonScreen skeletonScreen;

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

        binding.cardAccessories.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AccessoriesActivity.class);
            startActivity(intent);
        });
        binding.flViewLapTop.setOnClickListener(v -> {
            Intent intent = new Intent(activity, CategoriesActivity.class);
            startActivity(intent);
        });
        binding.flViewPc.setOnClickListener(v -> {
            Intent intent = new Intent(activity, BulidingActivity.class);
            startActivity(intent);
        });


        getSlider();
    }


    private void getSlider() {
        Api.getService(Tags.base_url)
                .getSlider(lang)
                .enqueue(new Callback<SliderModel>() {
                    @Override
                    public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                        binding.progBarSlider.setVisibility(View.GONE);
                        Log.e("0", "0");

                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            Log.e("1", "1");

                            if (response.body().getData().size() > 0) {
                                updateSliderUi(response.body().getData());
                                Log.e("2", "2");

                            } else {
                                Log.e("3", "3");

                                binding.flSlider.setVisibility(View.GONE);
                                binding.progBarSlider.setVisibility(View.GONE);
                            }

                        } else {
                            Log.e("4", "4");

                            binding.flSlider.setVisibility(View.GONE);
                            binding.progBarSlider.setVisibility(View.GONE);


                        }


                    }

                    @Override
                    public void onFailure(Call<SliderModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage()+"__");
                            binding.flSlider.setVisibility(View.GONE);
                            binding.progBarSlider.setVisibility(View.GONE);
                        } catch (Exception e) {

                        }
                    }
                });
    }


    private void updateSliderUi(List<SliderModel.Data> data) {
        sliderModelList.addAll(data);
        sliderAdapter = new SliderAdapter(sliderModelList, activity, this);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(90, 8, 90, 8);
        binding.pager.setPageMargin(24);
        binding.pager.setOffscreenPageLimit(sliderModelList.size());
        binding.flSlider.setVisibility(View.VISIBLE);
        binding.pager.setVisibility(View.VISIBLE);

        if (data.size() > 1) {
            timer = new Timer();
            timerTask = new MyTask();
            timer.scheduleAtFixedRate(timerTask, 6000, 6000);
        }
    }

    public void setSliderItemData(ProductModel productModel) {
        if (productModel!=null){
            if (productModel.getType().equals("complete")){
                Intent intent = new Intent(activity, ProductDetialsActivity.class);
                intent.putExtra("data", String.valueOf(productModel.getId()));
                startActivity(intent);
            }else {

            }
        }
    }


    public class MyTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

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
