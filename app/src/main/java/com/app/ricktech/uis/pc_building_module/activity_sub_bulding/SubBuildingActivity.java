package com.app.ricktech.uis.pc_building_module.activity_sub_bulding;

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
import com.app.ricktech.language.Language;
import com.app.ricktech.models.CategoryBuildingDataModel;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;

import java.io.Serializable;
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
    private CategoryModel  parentModel;
    private ActivityResultLauncher<Intent> launcher;
    private int selectedPos = -1;
    private CategoryModel categoryModel;

    private Map<Integer, CategoryModel> map;
    private int req;
    private boolean canNext = false;
    private boolean hasData = false;

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
        if (parentModel.getSub_categories().size()>0){
            hasData = true;
        }
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

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 100 && result.getResultCode() == RESULT_OK && result.getData() != null) {

                ProductModel productModel = (ProductModel) result.getData().getSerializableExtra("data");
                if (productModel != null) {
                    if (selectedPos != -1) {


                        if (map.get(selectedPos) != null) {
                            CategoryModel catModel = map.get(selectedPos);

                            List<ProductModel> productModelList = new ArrayList<>(catModel.getSelectedProduct());
                            productModelList.add(productModel);
                            catModel.setSelectedProduct(productModelList);
                            map.put(selectedPos, catModel);
                            list.set(selectedPos,catModel);

                        } else {
                            List<ProductModel> list1 = new ArrayList<>(list.get(selectedPos).getSelectedProduct());
                            list1.add(productModel);
                            CategoryModel categoryModel = list.get(selectedPos);
                            categoryModel.setSelectedProduct(list1);
                            list.set(selectedPos,categoryModel);

                            map.put(selectedPos, categoryModel);
                        }


                        adapter.notifyItemChanged(selectedPos);

                    }
                }


                if(map.size()>0){
                    canNext = true;
                    binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);
                }else {
                    canNext = false;
                    binding.btnSave.setBackgroundResource(R.drawable.small_rounded_gray77);

                }



            }
        });

        binding.btnSave.setOnClickListener(v -> {
            if (canNext){
                List<CategoryModel> data = new ArrayList<>(map.values());

                Intent intent = getIntent();
                intent.putExtra("data", (Serializable) data);
                setResult(RESULT_OK,intent);
                finish();
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
                                updateData(response.body().getData());
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

    private void updateData(List<CategoryModel> data) {
        List<CategoryModel> categoryModelList = new ArrayList<>();

        for (CategoryModel categoryModel:parentModel.getSub_categories()){

            for (int index=0;index<data.size();index++){
                CategoryModel model = data.get(index);
                if (categoryModel.getId()==model.getId()){
                    model.setSelectedProduct(categoryModel.getSelectedProduct());
                    model.setSub_categories(categoryModel.getSub_categories());
                    map.put(index, model);

                }

                categoryModelList.add(model);


            }

        }

        if (categoryModelList.size()==0){
            categoryModelList.addAll(data);
        }

        list.clear();
        list.addAll(categoryModelList);
        adapter.notifyDataSetChanged();

        if(map.size()>0){
            canNext = true;
            binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);

        }else {
            canNext = false;
            binding.btnSave.setBackgroundResource(R.drawable.small_rounded_gray77);

        }


    }

    public void setItemData(int adapterPosition, CategoryModel categoryModel) {
        this.selectedPos = adapterPosition;
        this.categoryModel = categoryModel;
        if (categoryModel.getIs_final_level().equals("yes")) {
            req = 100;
            Intent intent = new Intent(this, ProductBuildingActivity.class);
            intent.putExtra("data", categoryModel.getId() + "");
            intent.putExtra("data2", (Serializable) categoryModel.getSelectedProduct());

            launcher.launch(intent);

        }


    }

    public void deleteItemData(int adapterPosition, CategoryModel categoryModel) {
        map.remove(adapterPosition);
        categoryModel.getSelectedProduct().clear();
        categoryModel.getSub_categories().clear();

        list.set(adapterPosition,categoryModel);
        adapter.notifyItemChanged(adapterPosition);


        if(map.size()>0){
            canNext = true;
            binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);
        }else {
            if (hasData){
                canNext = true;
                binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);

            }else {
                canNext = false;
                binding.btnSave.setBackgroundResource(R.drawable.small_rounded_gray77);

            }

        }

    }

}