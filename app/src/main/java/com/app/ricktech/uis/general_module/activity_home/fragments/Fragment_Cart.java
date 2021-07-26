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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ricktech.R;
import com.app.ricktech.adapters.CartAdapter;
import com.app.ricktech.databinding.FragmentCartBinding;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.models.ManageCartModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.uis.general_module.activity_cart_details.CartDetailsActivity;
import com.app.ricktech.uis.general_module.activity_checkout.CheckoutActivity;
import com.app.ricktech.uis.general_module.activity_home.HomeActivity;
import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Fragment_Cart extends Fragment {

    private HomeActivity activity;
    private FragmentCartBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;
    private ActivityResultLauncher<Intent> launcher;
    private ManageCartModel manageCartModel;
    private List<CartModel> list;
    private CartAdapter adapter;
    private int selectedPos = -1;


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
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        Glide.with(activity).asGif().load(R.drawable.small_computer).into(binding.image);
        updateCart();

    }

    public void updateCart() {
        manageCartModel = ManageCartModel.getInstance();

        list.clear();

        if (adapter!=null){
            adapter.notifyDataSetChanged();

        }
        list.addAll(manageCartModel.getCartList(activity));


        if (list.size() > 0) {
            adapter = new CartAdapter(activity, list, this);
            binding.recView.setLayoutManager(new LinearLayoutManager(activity));
            binding.recView.setAdapter(adapter);
            binding.tvNoData.setVisibility(View.GONE);
            binding.image.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);
            binding.image.setVisibility(View.GONE);

        }
    }

    private void clearCart() {
        manageCartModel.clearCart(activity);
        list.remove(selectedPos);
        adapter.notifyItemRemoved(selectedPos);
        if (list.size() == 0) {
            binding.tvNoData.setVisibility(View.VISIBLE);
            binding.image.setVisibility(View.GONE);

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    updateCart();
                }
            }


        });
    }

    public void setItemData(CartModel cartModel, int adapterPosition) {
        selectedPos = adapterPosition;
        Intent intent = new Intent(activity, CartDetailsActivity.class);
        launcher.launch(intent);

    }

    public void deleteItemData(CartModel cartModel, int adapterPosition) {
        selectedPos = adapterPosition;

        clearCart();
    }
}
