package com.app.ricktech.uis.separate_gaming_module.separate_gaming_categories;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ricktech.R;
import com.app.ricktech.adapters.BuildingAdapter;
import com.app.ricktech.databinding.ActivitySeparatePcCategoriesBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.AddBuildModel;
import com.app.ricktech.models.AddCompareModel;
import com.app.ricktech.models.AddToBuildDataModel;
import com.app.ricktech.models.CategoryBuildingDataModel;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.ManageCartModel;
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
import com.app.ricktech.uis.separate_gaming_module.activity_separate_gaming_sub_building.SeparateGamingSubBuildingActivity;
import com.app.ricktech.uis.separate_pc_module.activity_separate_pc_sub_building.SeparatePcSubBuildingActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeparateGamingCategoriesActivity extends AppCompatActivity {

    private ActivitySeparatePcCategoriesBinding binding;
    private String lang;
    private BuildingAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<CategoryModel> list;
    private ActivityResultLauncher<Intent> launcher;
    private int selectedPos = -1;
    private CategoryModel selectedMainCategoryModel;
    private int req;
    private boolean canNext = false;
    double total = 0;
    private ManageCartModel manageCartModel;
    private String brand_id = "";


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_separate_pc_categories);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        brand_id = intent.getStringExtra("data");

    }


    private void initView() {
        manageCartModel = ManageCartModel.getInstance();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "de");
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
                if (req == 100 && result.getResultCode() == RESULT_OK && result.getData() != null) {

                    ProductModel productModel = (ProductModel) result.getData().getSerializableExtra("data");
                    if (productModel != null) {
                        if (selectedPos != -1) {
                            if (selectedMainCategoryModel != null) {
                                List<ProductModel> selectedProductList = new ArrayList<>(selectedMainCategoryModel.getSelectedProduct());

                                selectedProductList.add(productModel);

                                selectedMainCategoryModel.setSelectedProduct(selectedProductList);


                            }

                            list.set(selectedPos, selectedMainCategoryModel);

                            adapter.notifyItemChanged(selectedPos);

                        }
                    }


                    calculateTotal_Points();

                    canNext = true;
                    binding.btnNext.setBackgroundResource(R.drawable.small_rounded_primary);
                    binding.btnCompare.setBackgroundResource(R.drawable.small_rounded_primary);

                } else if (req == 200 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                    List<CategoryModel> data = (List<CategoryModel>) result.getData().getSerializableExtra("data");


                    if (selectedPos != -1) {
                        if (data != null) {


                            if (data.size() > 0) {
                                List<ProductModel> list1 = new ArrayList<>();
                                for (CategoryModel categoryModel : data) {
                                    list1.addAll(categoryModel.getSelectedProduct());
                                }

                                selectedMainCategoryModel.setSelectedProduct(new ArrayList<>(list1));

                            } else {

                                selectedMainCategoryModel.getSelectedProduct().clear();
                                selectedMainCategoryModel.getSub_categories().clear();
                            }


                            selectedMainCategoryModel.setSub_categories(new ArrayList<>(data));
                            list.set(selectedPos, selectedMainCategoryModel);

                            adapter.notifyItemChanged(selectedPos);

                            calculateTotal_Points();

                            if (isListHasData()) {
                                canNext = true;
                                binding.btnNext.setBackgroundResource(R.drawable.small_rounded_primary);
                                binding.btnCompare.setBackgroundResource(R.drawable.small_rounded_primary);

                            } else {
                                canNext = false;
                                binding.btnNext.setBackgroundResource(R.drawable.small_rounded_gray77);
                                binding.btnCompare.setBackgroundResource(R.drawable.small_rounded_gray77);

                            }
                        }
                    }
                }
            }
        });

        binding.btnNext.setOnClickListener(v -> {
            if (canNext) {
                binding.flDialog.setVisibility(View.VISIBLE);
            }
        });

        binding.btnCompare.setOnClickListener(v -> {
            if (canNext) {
                List<AddBuildModel> list = new ArrayList<>();

                List<CategoryModel> categoryModelList = new ArrayList<>(getSelectedCategory());
                for (CategoryModel categoryModel : categoryModelList) {
                    if (categoryModel.getIs_final_level().equals("yes")) {

                        List<String> products_ids = new ArrayList<>();
                        List<ProductModel> productModelList = new ArrayList<>();
                        for (ProductModel model : categoryModel.getSelectedProduct()) {
                            products_ids.add(model.getId() + "");
                            productModelList.add(model);
                        }

                        AddBuildModel addBuildModel = new AddBuildModel(categoryModel.getId() + "", products_ids);
                        addBuildModel.setCategory_id(categoryModel.getId() + "");
                        addBuildModel.setCategory_name(categoryModel.getTrans_title());
                        addBuildModel.setCategory_image(categoryModel.getImage());
                        addBuildModel.setProductModelList(productModelList);
                        list.add(addBuildModel);

                    } else {

                        for (CategoryModel model : categoryModel.getSub_categories()) {
                            Log.e("eee2", model.getTrans_title());
                            List<String> sub_products_ids = new ArrayList<>();
                            List<ProductModel> subProductModelList = new ArrayList<>();

                            for (ProductModel productModel : model.getSelectedProduct()) {
                                Log.e("titlexx", productModel.getTrans_title());
                                sub_products_ids.add(productModel.getId() + "");
                                subProductModelList.add(productModel);
                            }

                            AddBuildModel addBuildModel = new AddBuildModel(model.getId() + "", sub_products_ids);
                            addBuildModel.setCategory_id(model.getId() + "");
                            addBuildModel.setCategory_name(model.getTrans_title());
                            addBuildModel.setCategory_image(model.getImage());
                            addBuildModel.setProductModelList(subProductModelList);
                            list.add(addBuildModel);
                        }


                    }

                }


                Log.e("efdfd", list.size() + "_");

                AddCompareModel model = new AddCompareModel(list);
                compare(model);

            }
        });

        binding.cardViewClose.setOnClickListener(v -> {
            Common.CloseKeyBoard(this, binding.edtName);

            binding.flDialog.setVisibility(View.GONE);
        });

        binding.flAddToCart.setOnClickListener(v -> {
            String name = binding.edtName.getText().toString();
            if (!name.isEmpty()) {
                binding.edtName.setError(null);
                Common.CloseKeyBoard(this, binding.edtName);
                addToCart(name);
            } else {
                binding.edtName.setError(getString(R.string.field_req));

            }
        });
        binding.btnBuild.setOnClickListener(v -> {
            String name = binding.edtName.getText().toString();
            if (!name.isEmpty()) {
                binding.edtName.setError(null);
                Common.CloseKeyBoard(this, binding.edtName);
                addToBuild(name);
            } else {
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
                .compare(lang, model)
                .enqueue(new Callback<SuggestionGameDataModel>() {
                    @Override
                    public void onResponse(Call<SuggestionGameDataModel> call, Response<SuggestionGameDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    Intent intent = new Intent(SeparateGamingCategoriesActivity.this, GamesActivity.class);
                                    Log.e("size", response.body().getData().size() + "__");
                                    intent.putExtra("data", (Serializable) response.body().getData());
                                    startActivity(intent);

                                } else if (response.body().getStatus() == 403) {
                                    Toast.makeText(SeparateGamingCategoriesActivity.this, R.string.no_games, Toast.LENGTH_SHORT).show();
                                }
                            } else {
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


        List<AddBuildModel> list = new ArrayList<>();

        List<CategoryModel> categoryModelList = new ArrayList<>(getSelectedCategory());
        for (CategoryModel categoryModel : categoryModelList) {
            Log.e("eee", categoryModel.getTrans_title());


            if (categoryModel.getIs_final_level().equals("yes")) {

                List<String> products_ids = new ArrayList<>();
                List<ProductModel> productModelList = new ArrayList<>();
                for (ProductModel model : categoryModel.getSelectedProduct()) {
                    products_ids.add(model.getId() + "");
                    productModelList.add(model);
                }

                AddBuildModel addBuildModel = new AddBuildModel(categoryModel.getId() + "", products_ids);
                addBuildModel.setCategory_id(categoryModel.getId() + "");
                addBuildModel.setCategory_name(categoryModel.getTrans_title());
                addBuildModel.setCategory_image(categoryModel.getImage());
                addBuildModel.setProductModelList(productModelList);
                list.add(addBuildModel);

            } else {


                for (CategoryModel model : categoryModel.getSub_categories()) {

                    List<String> sub_products_ids = new ArrayList<>();
                    List<ProductModel> subProductModelList = new ArrayList<>();
                    Log.e("eee2rr", model.getTrans_title());

                    for (ProductModel productModel : model.getSelectedProduct()) {
                        Log.e("titlevbvb", productModel.getTrans_title());
                        sub_products_ids.add(productModel.getId() + "");
                        subProductModelList.add(productModel);
                    }

                    AddBuildModel addBuildModel = new AddBuildModel(model.getId() + "", sub_products_ids);
                    addBuildModel.setCategory_id(model.getId() + "");
                    addBuildModel.setCategory_name(model.getTrans_title());
                    addBuildModel.setCategory_image(model.getImage());
                    addBuildModel.setProductModelList(subProductModelList);
                    list.add(addBuildModel);
                }


            }


        }


        AddToBuildDataModel model = new AddToBuildDataModel(name, total, list);


        Api.getService(Tags.base_url)
                .addToBuild(lang, "Bearer " + userModel.getData().getToken(), model)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Toast.makeText(SeparateGamingCategoriesActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
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

    private void addToCart(String name) {

        List<AddBuildModel> list = new ArrayList<>();

        List<CategoryModel> categoryModelList = new ArrayList<>(getSelectedCategory());
        for (CategoryModel categoryModel : categoryModelList) {

            List<String> products_ids = new ArrayList<>();
            List<ProductModel> productModelList = new ArrayList<>();
            for (ProductModel model : categoryModel.getSelectedProduct()) {
                products_ids.add(model.getId() + "");
                productModelList.add(model);
            }

            if (categoryModel.getIs_final_level().equals("yes")) {
                AddBuildModel addBuildModel = new AddBuildModel(categoryModel.getId() + "", products_ids);
                addBuildModel.setCategory_id(categoryModel.getId() + "");
                addBuildModel.setCategory_name(categoryModel.getTrans_title());
                addBuildModel.setCategory_image(categoryModel.getImage());
                addBuildModel.setProductModelList(productModelList);
                list.add(addBuildModel);

            } else {

                for (CategoryModel model : categoryModel.getSub_categories()) {
                    List<String> sub_products_ids = new ArrayList<>();
                    List<ProductModel> subProductModelList = new ArrayList<>();

                    Log.e("tit", model.getTrans_title() + "__" + model.getSelectedProduct().size());
                    for (ProductModel productModel : model.getSelectedProduct()) {
                        sub_products_ids.add(productModel.getId() + "");
                        subProductModelList.add(productModel);
                    }

                    AddBuildModel addBuildModel = new AddBuildModel(model.getId() + "", sub_products_ids);
                    addBuildModel.setCategory_id(model.getId() + "");
                    addBuildModel.setCategory_name(model.getTrans_title());
                    addBuildModel.setCategory_image(model.getImage());
                    addBuildModel.setProductModelList(subProductModelList);
                    list.add(addBuildModel);
                }


            }


        }


        for (AddBuildModel addBuildModel : list) {
            Log.e("a1", addBuildModel.getCategory_id() + "__" + addBuildModel.getCategory_name());
            List<ProductModel> productModelList = addBuildModel.getProductModelList();
            for (ProductModel productModel : productModelList) {
                Log.e("b1", productModel.getTrans_title() + "__" + productModel.getPrice() + "__" + productModel.getCount());
            }
        }

        manageCartModel.addBuildProduct(this, list, name, 1);
        binding.flDialog.setVisibility(View.GONE);
        Toast.makeText(this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
    }

    private void getBuildings() {
        Api.getService(Tags.base_url)
                .getSeparateGamingCategoryBuildingByBrandId(lang, brand_id)
                .enqueue(new Callback<CategoryBuildingDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryBuildingDataModel> call, Response<CategoryBuildingDataModel> response) {
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.llData.setVisibility(View.VISIBLE);
                        //Log.e("code", response.body().getStatus()+"__");

                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            Log.e("yy", "yy");
                            if (response.body().getData().size() > 0) {
                                Log.e("ddd", "tt");
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
                            try {
                                Log.e("error", response.errorBody().string() + "__");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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
        this.selectedMainCategoryModel = categoryModel;
        if (categoryModel.getIs_final_level().equals("yes")) {
            req = 100;
            Intent intent = new Intent(this, ProductBuildingActivity.class);
            intent.putExtra("data", categoryModel.getId() + "");
            intent.putExtra("data2", (Serializable) categoryModel.getSelectedProduct());

            launcher.launch(intent);

        } else {
            req = 200;
            Intent intent = new Intent(this, SeparateGamingSubBuildingActivity.class);
            intent.putExtra("data", categoryModel);
            intent.putExtra("data2", brand_id);
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
            binding.btnNext.setBackgroundResource(R.drawable.small_rounded_primary);
            binding.btnCompare.setBackgroundResource(R.drawable.small_rounded_primary);

        } else {
            canNext = false;
            binding.btnNext.setBackgroundResource(R.drawable.small_rounded_gray77);
            binding.btnCompare.setBackgroundResource(R.drawable.small_rounded_gray77);

        }
        calculateTotal_Points();
    }

    private void calculateTotal_Points() {
        total = 0;
        double points = 0;

        for (CategoryModel categoryModel : list) {
            if (categoryModel.getSelectedProduct().size() > 0) {
                for (ProductModel productModel : categoryModel.getSelectedProduct()) {
                    total += productModel.getPrice();
                    points += productModel.getPoints();
                }
            }


        }

        binding.setTotal(total + "");
        binding.setScore(points + "");
    }

    private boolean isListHasData() {
        boolean hasData = false;
        for (CategoryModel model : list) {

            if (model.getSelectedProduct().size() > 0) {
                hasData = true;
            }


        }
        return hasData;
    }

    private List<CategoryModel> getSelectedCategory() {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (CategoryModel categoryModel : list) {
            if (categoryModel.getSelectedProduct().size() > 0) {
                categoryModelList.add(categoryModel);
            }
        }

        return categoryModelList;
    }
}