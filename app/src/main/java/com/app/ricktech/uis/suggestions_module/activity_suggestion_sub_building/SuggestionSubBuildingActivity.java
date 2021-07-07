package com.app.ricktech.uis.suggestions_module.activity_suggestion_sub_building;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.ricktech.R;
import com.app.ricktech.adapters.SubBuildingAdapter;
import com.app.ricktech.databinding.ActivitySubBuildingBinding;
import com.app.ricktech.databinding.ActivitySuggestionSubBuildingBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.CategoryBuildingDataModel;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;
import com.ethanhua.skeleton.SkeletonScreen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestionSubBuildingActivity extends AppCompatActivity {

    private ActivitySuggestionSubBuildingBinding binding;
    private String lang;
    private SubBuildingAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<CategoryModel> list;
    private String  parent_id;
    private ActivityResultLauncher<Intent> launcher;
    private int selectedPos = -1;
    private CategoryModel categoryModel;
    private Map<Integer, List<ProductModel>> map;
    private int req;
    private boolean canNext = false;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_suggestion_sub_building);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        parent_id = intent.getStringExtra("data");

    }


    private void initView() {
        map = new HashMap<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
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
                                List<ProductModel> productModelList = map.get(selectedPos);
                                if (productModelList != null) {
                                    productModelList.add(productModel);
                                    map.put(selectedPos, productModelList);

                                }

                            } else {
                                List<ProductModel> productModelList = new ArrayList<>();
                                productModelList.add(productModel);
                                map.put(selectedPos, productModelList);
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


                    if(map.size()>0){
                        canNext = true;
                        binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);
                    }else {
                        canNext = true;
                        binding.btnSave.setBackgroundResource(R.drawable.small_rounded_gray77);

                    }



                }
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            if (canNext){
                List<ProductModel> data = new ArrayList<>();
                for (List<ProductModel> list :map.values()){
                    data.addAll(list);
                }

                Intent intent = getIntent();
                intent.putExtra("data", (Serializable) data);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }



    private void getSubBuildings() {
        Api.getService(Tags.base_url)
                .getSubCategoryBuilding(lang,parent_id)
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


        if(map.size()>0){
            canNext = true;
            binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);
        }else {
            canNext = true;
            binding.btnSave.setBackgroundResource(R.drawable.small_rounded_gray77);

        }

    }

}