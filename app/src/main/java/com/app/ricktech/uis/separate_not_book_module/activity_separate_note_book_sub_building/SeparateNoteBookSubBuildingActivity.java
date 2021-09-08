package com.app.ricktech.uis.separate_not_book_module.activity_separate_note_book_sub_building;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.ricktech.R;
import com.app.ricktech.adapters.SubBuildingAdapter;
import com.app.ricktech.databinding.ActivitySeparatePcSubBuildingBinding;
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
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeparateNoteBookSubBuildingActivity extends AppCompatActivity {

    private ActivitySeparatePcSubBuildingBinding binding;
    private String lang;
    private SubBuildingAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<CategoryModel> list;
    private CategoryModel parentModel;
    private String brand_id ="";
    private ActivityResultLauncher<Intent> launcher;
    private int selectedPos = -1;
    private CategoryModel categoryModel;
    private int req;
    private boolean canNext = false;
    private boolean hasData = false;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_separate_pc_sub_building);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        parentModel = (CategoryModel) intent.getSerializableExtra("data");
        brand_id = intent.getStringExtra("data2");

        if (parentModel.getSub_categories().size() > 0) {
            hasData = true;
        }
    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "de");
        binding.setLang(lang);
        binding.setTitle(parentModel.getTrans_title());
        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
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


                        if (categoryModel != null) {
                            List<ProductModel> selectedProductList = new ArrayList<>(categoryModel.getSelectedProduct());


                            selectedProductList.add(productModel);

                            categoryModel.setSelectedProduct(new ArrayList<>(selectedProductList));



                        }

                        list.set(selectedPos, categoryModel);


                        adapter.notifyItemChanged(selectedPos);

                    }
                }


                canNext = true;
                binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);



            }
        });

        binding.btnSave.setOnClickListener(v -> {
            if (canNext) {
                List<CategoryModel> data = new ArrayList<>(getSelectedSubCategory());

                Log.e("dara", data.size() + "_");
                Intent intent = getIntent();
                intent.putExtra("data", (Serializable) data);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    private void getSubBuildings() {
        Api.getService(Tags.base_url)
                .getSeparateNoteBookSubCategoryBuilding(lang, parentModel.getId() + "",brand_id)
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

        for (CategoryModel categoryModel : data) {
            for (CategoryModel model : parentModel.getSub_categories()) {
                if (categoryModel.getId() == model.getId()) {
                    categoryModel.setSelectedProduct(model.getSelectedProduct());


                }

            }
            categoryModelList.add(categoryModel);

        }


        if (categoryModelList.size() == 0) {
            categoryModelList.addAll(data);
        }

        list.clear();
        list.addAll(categoryModelList);
        adapter.notifyDataSetChanged();

        if (isListHasData()) {
            canNext = true;
            binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);

        } else {
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

        categoryModel.getSelectedProduct().clear();
        categoryModel.getSub_categories().clear();
        list.set(adapterPosition, categoryModel);
        adapter.notifyItemChanged(adapterPosition);


        if (isListHasData()) {
            canNext = true;
            binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);
        } else {
            if (hasData) {
                canNext = true;
                binding.btnSave.setBackgroundResource(R.drawable.small_rounded_primary);

            } else {
                canNext = false;
                binding.btnSave.setBackgroundResource(R.drawable.small_rounded_gray77);

            }

        }

    }

    private boolean isListHasData(){
        boolean hasData = false;
        for (CategoryModel model : list) {

            if (model.getSelectedProduct().size()>0){
                hasData = true;
            }


        }
        return hasData;
    }

    private List<CategoryModel> getSelectedSubCategory(){
        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (CategoryModel categoryModel:list){
            if (categoryModel.getSelectedProduct().size()>0){
                categoryModelList.add(categoryModel);
            }
        }
        return categoryModelList;
    }


}