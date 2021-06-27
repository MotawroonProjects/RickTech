package com.app.ricktech.uis.pc_building_module.activity_sub_bulding;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.ricktech.R;
import com.app.ricktech.adapters.BuildingAdapter;
import com.app.ricktech.adapters.SubBuildingAdapter;
import com.app.ricktech.databinding.ActivitySubBuildingBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.AddBuildModel;
import com.app.ricktech.models.CategoryBuildingDataModel;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;
import com.ethanhua.skeleton.SkeletonScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubBuildingActivity extends AppCompatActivity {
    private ActivitySubBuildingBinding binding;
    private String lang;
    private SubBuildingAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<CategoryModel> list;
    private SkeletonScreen skeletonScreen;
    private CategoryModel  parentModel;
    private ActivityResultLauncher<Intent> launcher;
    private int selectedPos = -1;
    private CategoryModel categoryModel;
    private Map<Integer, AddBuildModel> map;
    private int req;
    private boolean canNext = false;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_building);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        parentModel = (CategoryModel) intent.getSerializableExtra("data");

    }


    private void initView() {
        map = new HashMap<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setTitle(parentModel.getTrans_title());
        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new SubBuildingAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());


        binding.shimmer.startShimmer();
        getSubBuildings();

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (req == 100 && result.getResultCode() == RESULT_OK && result.getData() != null) {

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


                }
            }
        });

    }



    private void getSubBuildings() {
        Api.getService(Tags.base_url)
                .getSubCategoryBuilding(lang,parentModel.getId()+"")
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

                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);


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

        }


    }

    public void deleteItemData(int adapterPosition, CategoryModel categoryModel) {
        map.remove(adapterPosition);
        categoryModel.getSelectedProduct().clear();

        list.set(adapterPosition,categoryModel);
        adapter.notifyItemChanged(adapterPosition);


        calculateTotal_Points();
    }

    private void calculateTotal_Points() {
        double total = 0;
        double points = 0;
        for (Integer key :map.keySet()){
            CategoryModel model = list.get(key);
            for (ProductModel productModel:model.getSelectedProduct()){
                total+= productModel.getPrice();
                points += productModel.getPoints();
            }
        }

    }
}