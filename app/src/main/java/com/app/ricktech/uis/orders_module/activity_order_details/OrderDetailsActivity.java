package com.app.ricktech.uis.orders_module.activity_order_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.ricktech.R;
import com.app.ricktech.adapters.NotificationAdapter;
import com.app.ricktech.adapters.OrderBuildAdapter;
import com.app.ricktech.adapters.OrderProductAdapter;
import com.app.ricktech.adapters.ProductAdapter;
import com.app.ricktech.databinding.ActivityNotificationBinding;
import com.app.ricktech.databinding.ActivityOrderDetailsBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.NotificationDataModel;
import com.app.ricktech.models.NotificationModel;
import com.app.ricktech.models.OrderModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SingleOrderModel;
import com.app.ricktech.models.SingleProductModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.orders_module.activity_order_build_details.OrderBuildDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    private ActivityOrderDetailsBinding binding;
    private String lang;
    private OrderProductAdapter productAdapter;
    private OrderBuildAdapter orderBuildAdapter;

    private UserModel userModel;
    private Preferences preferences;
    private List<OrderModel.OrderProducts> productModelList;
    private List<OrderModel.OrderPc> orderPcList;
    private String order_id="";
    private OrderModel orderModel;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        order_id = intent.getStringExtra("data");
    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        productModelList = new ArrayList<>();
        orderPcList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recViewProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new OrderProductAdapter(this, productModelList);
        binding.recViewProducts.setAdapter(productAdapter);
        binding.recViewProducts.setItemAnimator(new DefaultItemAnimator());


        binding.recViewBuild.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        orderBuildAdapter = new OrderBuildAdapter(this, orderPcList);
        binding.recViewBuild.setAdapter(orderBuildAdapter);
        binding.recViewBuild.setItemAnimator(new DefaultItemAnimator());

        binding.llBack.setOnClickListener(v -> finish());

        getOrderDetails();

    }

    private void getOrderDetails() {
        Api.getService(Tags.base_url)
                .getSingleOrder(lang,"Bearer "+userModel.getData().getToken(),order_id)
                .enqueue(new Callback<SingleOrderModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderModel> call, Response<SingleOrderModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null&&response.body().getStatus()==200 ) {
                           updateUi(response.body());
                        } else {
                            binding.progBar.setVisibility(View.GONE);

                        }


                    }

                    @Override
                    public void onFailure(Call<SingleOrderModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage()+"__");
                            binding.progBar.setVisibility(View.GONE);
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void updateUi(SingleOrderModel body) {
        orderModel = body.getData();
        binding.scrollView.setVisibility(View.VISIBLE);
        if (orderModel.getOrder_details_pcs().size()>0){
            binding.llBuild.setVisibility(View.VISIBLE);
            orderPcList.clear();
            orderPcList.addAll(orderModel.getOrder_details_pcs());
            orderBuildAdapter.notifyDataSetChanged();

        }else {
            binding.llBuild.setVisibility(View.GONE);

        }

        if (orderModel.getOrder_details_products().size()>0){
            binding.llProducts.setVisibility(View.VISIBLE);
            productModelList.clear();
            productModelList.addAll(orderModel.getOrder_details_products());
            productAdapter.notifyDataSetChanged();

        }else {
            binding.llProducts.setVisibility(View.GONE);

        }
    }

    public void setItemData(OrderModel.OrderPc orderPc) {
        Intent intent = new Intent(this, OrderBuildDetailsActivity.class);
        intent.putExtra("data", orderPc);
        startActivity(intent);

    }
}