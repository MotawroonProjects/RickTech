package com.app.ricktech.uis.pc_building_module.activity_building;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.ricktech.R;
import com.app.ricktech.adapters.BuildingAdapter;
import com.app.ricktech.adapters.DetialsAdapter;
import com.app.ricktech.databinding.ActivityBulidingBinding;
import com.app.ricktech.databinding.ActivityBulidingProductDetailsBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.AddBuildModel;
import com.app.ricktech.models.AddCompareModel;
import com.app.ricktech.models.AddToBuildDataModel;
import com.app.ricktech.models.CategoryBuildingDataModel;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.ComponentModel;
import com.app.ricktech.models.ProductDataModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.StatusResponse;
import com.app.ricktech.models.SuggestionGameDataModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;
import com.app.ricktech.uis.pc_building_module.activity_games.GamesActivity;
import com.app.ricktech.uis.pc_building_module.activity_sub_bulding.SubBuildingActivity;
import com.ethanhua.skeleton.SkeletonScreen;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BulidingActivity extends AppCompatActivity {

    private ActivityBulidingBinding binding;
    private String lang;
    private BuildingAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<CategoryModel> list;
    private SkeletonScreen skeletonScreen;
    private ActivityResultLauncher<Intent> launcher;
    private int selectedPos = -1;
    private CategoryModel categoryModel;
    private Map<Integer, AddBuildModel> map;
    private int req;
    private boolean canNext = false;
    double total = 0;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buliding);
        initView();
    }


    private void initView() {
        map = new HashMap<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BuildingAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());

        binding.setScore("0");
        binding.setTotal("0.0");

        binding.shimmer.startShimmer();
        getBuildings();

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (req == 100 && result.getResultCode() == RESULT_OK && result.getData() != null)
                {

                    ProductModel productModel = (ProductModel) result.getData().getSerializableExtra("data");
                    if (productModel != null) {
                        if (selectedPos != -1) {


                            if (map.get(selectedPos) != null) {
                                AddBuildModel model = map.get(selectedPos);
                                if (model != null) {
                                    List<String> productModelList = model.getList();
                                    productModelList.add(productModel.getId()+"");
                                    model.setList(productModelList);
                                    map.put(selectedPos, model);

                                }

                            } else {
                                List<String> productModelList = new ArrayList<>();
                                productModelList.add(productModel.getId()+"");
                                AddBuildModel model = new AddBuildModel(categoryModel.getId()+"", productModelList);
                                map.put(selectedPos, model);
                            }

                            List<ProductModel> list1 = list.get(selectedPos).getSelectedProduct();
                            if (list1==null){
                                list1 = new ArrayList<>();
                            }
                            list1.add(productModel);
                            categoryModel.setSelectedProduct(list1);
                            adapter.notifyItemChanged(selectedPos);

                        }
                    }


                    calculateTotal_Points();

                    if (map.size()>0){
                        canNext = true;
                        binding.btnNext.setBackgroundResource(R.drawable.small_rounded_primary);
                        binding.btnCompare.setBackgroundResource(R.drawable.small_rounded_primary);

                    }else {
                        canNext = false;
                        binding.btnNext.setBackgroundResource(R.drawable.small_rounded_gray77);
                        binding.btnCompare.setBackgroundResource(R.drawable.small_rounded_gray77);

                    }
                }
                else if (req == 200 && result.getResultCode() == RESULT_OK && result.getData() != null){
                    List<ProductModel> data = (List<ProductModel>) result.getData().getSerializableExtra("data");
                    if (selectedPos!=-1){
                        if (list!=null){



                            if (map.get(selectedPos) != null) {
                                AddBuildModel model = map.get(selectedPos);
                                if (model != null) {
                                    List<String> productModelList = model.getList();
                                    productModelList.clear();
                                    for (ProductModel productModel:data){
                                        productModelList.add(productModel.getId()+"");
                                    }
                                    model.setList(productModelList);
                                    map.put(selectedPos, model);

                                }

                            } else {
                                List<String> productModelList = new ArrayList<>();

                                for (ProductModel productModel:data){
                                    productModelList.add(productModel.getId()+"");
                                }
                                AddBuildModel model = new AddBuildModel(categoryModel.getId()+"", productModelList);
                                map.put(selectedPos, model);

                            }



                            categoryModel.setSelectedProduct(data);
                            list.set(selectedPos,categoryModel);

                            adapter.notifyItemChanged(selectedPos);

                            calculateTotal_Points();

                        }
                    }
                }
            }
        });

        binding.btnNext.setOnClickListener(v -> {
            if (canNext){
                binding.flDialog.setVisibility(View.VISIBLE);
            }
        });

        binding.btnCompare.setOnClickListener(v -> {
            if (canNext){
                List<AddBuildModel> list = new ArrayList<>(map.values());
                AddCompareModel model = new AddCompareModel(list);
                compare(model);

            }
        });

        binding.cardViewClose.setOnClickListener(v -> {

            binding.flDialog.setVisibility(View.GONE);
        });
        binding.btnBuild.setOnClickListener(v -> {
            String name = binding.edtName.getText().toString();
            if (!name.isEmpty()){
                binding.edtName.setError(null);
                Common.CloseKeyBoard(this,binding.edtName);
                addToBuild(name);
            }else {
                binding.edtName.setError(getString(R.string.field_req));

            }
        });
    }

    private void compare(AddCompareModel model) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .compare(lang,model)
                .enqueue(new Callback<SuggestionGameDataModel>() {
                    @Override
                    public void onResponse(Call<SuggestionGameDataModel> call, Response<SuggestionGameDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null ) {
                                if (response.body().getStatus() == 200)
                                {
                                    Intent intent = new Intent(BulidingActivity.this, GamesActivity.class);
                                    Log.e("size", response.body().getData().size()+"__");
                                    intent.putExtra("data", (Serializable) response.body().getData());
                                    startActivity(intent);

                                }else if(response.body().getStatus() == 403){
                                    Toast.makeText(BulidingActivity.this, R.string.no_games, Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                binding.flDialog.setVisibility(View.VISIBLE);
                            }

                        } else {
                            dialog.dismiss();
                            binding.flDialog.setVisibility(View.VISIBLE);

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<SuggestionGameDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            binding.flDialog.setVisibility(View.VISIBLE);

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

    private void addToBuild(String name) {
        binding.flDialog.setVisibility(View.GONE);
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        List<AddBuildModel> list = new ArrayList<>(map.values());
        AddToBuildDataModel  model = new AddToBuildDataModel(name,total,list);


        Api.getService(Tags.base_url)
                .addToBuild(lang,"Bearer " + userModel.getData().getToken(),model)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Toast.makeText(BulidingActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                binding.flDialog.setVisibility(View.VISIBLE);
                            }

                        } else {
                            dialog.dismiss();
                            binding.flDialog.setVisibility(View.VISIBLE);

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            binding.flDialog.setVisibility(View.VISIBLE);

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


    private void getBuildings() {
        Api.getService(Tags.base_url)
                .getCategoryBuilding(lang)
                .enqueue(new Callback<CategoryBuildingDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryBuildingDataModel> call, Response<CategoryBuildingDataModel> response) {
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.llData.setVisibility(View.VISIBLE);
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            if (response.body().getData().size() > 0) {
                                list.clear();
                                list.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                binding.tvNoData.setVisibility(View.GONE);
                                binding.llTotal.setVisibility(View.VISIBLE);
                                binding.flCompare.setVisibility(View.VISIBLE);
                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);
                                binding.llTotal.setVisibility(View.GONE);
                                binding.flCompare.setVisibility(View.GONE);

                            }

                        } else {
                            binding.shimmer.stopShimmer();
                            binding.shimmer.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<CategoryBuildingDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");
                            binding.shimmer.stopShimmer();
                            binding.shimmer.setVisibility(View.GONE);

                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void setItemData(int adapterPosition, CategoryModel categoryModel) {
        this.selectedPos = adapterPosition;
        this.categoryModel = categoryModel;
        if (categoryModel.getIs_final_level().equals("yes")) {
            req = 100;
            Intent intent = new Intent(this, ProductBuildingActivity.class);
            intent.putExtra("data", categoryModel.getId() + "");
            launcher.launch(intent);

        } else {
            req = 200;
            Intent intent = new Intent(this, SubBuildingActivity.class);
            intent.putExtra("data", categoryModel);
            launcher.launch(intent);

        }


    }

    public void deleteItemData(int adapterPosition, CategoryModel categoryModel) {
        map.remove(adapterPosition);
        categoryModel.getSelectedProduct().clear();

        list.set(adapterPosition,categoryModel);
        adapter.notifyItemChanged(adapterPosition);

        if (map.size()>0){
            canNext = true;
            binding.btnNext.setBackgroundResource(R.drawable.small_rounded_primary);
            binding.btnCompare.setBackgroundResource(R.drawable.small_rounded_primary);

        }else {
            canNext = false;
            binding.btnNext.setBackgroundResource(R.drawable.small_rounded_gray77);
            binding.btnCompare.setBackgroundResource(R.drawable.small_rounded_gray77);

        }
        calculateTotal_Points();
    }

    private void calculateTotal_Points() {
        total = 0;
        double points = 0;
        for (Integer key :map.keySet()){
            CategoryModel model = list.get(key);
            for (ProductModel productModel:model.getSelectedProduct()){
                total+= productModel.getPrice();
                points += productModel.getPoints();
            }
        }

        binding.setTotal(total+"");
        binding.setScore(points+"");
    }
}