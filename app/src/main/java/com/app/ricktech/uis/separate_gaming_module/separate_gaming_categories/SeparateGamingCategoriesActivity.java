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
import com.app.ricktech.adapters.SeparateBuildingAdapter;
import com.app.ricktech.databinding.ActivitySeparatePcCategoriesBinding;
import com.app.ricktech.databinding.ActivitySuggetionBuildingsBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.AddBuildModel;
import com.app.ricktech.models.AddCompareModel;
import com.app.ricktech.models.AddToBuildDataModel;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.models.CategoryBuildingDataModel;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.ManageCartModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.StatusResponse;
import com.app.ricktech.models.SuggestionGameDataModel;
import com.app.ricktech.models.SuggestionModel;
import com.app.ricktech.models.SuggestionsDataModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;
import com.app.ricktech.uis.pc_building_module.activity_games.GamesActivity;
import com.app.ricktech.uis.separate_pc_module.separate_pc_categories.SeparatePcCategoriesActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeparateGamingCategoriesActivity extends AppCompatActivity {
    private ActivitySuggetionBuildingsBinding binding;
    private String lang;
    private SeparateBuildingAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<SuggestionModel> list;
    private int selectedPos = -1;
    private SuggestionModel suggestionModel;
    private String brand_id = "";
    private BrandModel brandModel;
    private int req;
    private boolean canNext = false;
    double total = 0;
    private List<SuggestionModel> categoryHasSubCategoryList = new ArrayList<>();

    private boolean hasSuggestions = false;

    private ManageCartModel manageCartModel;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_suggetion_buildings);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        brandModel = (BrandModel) intent.getSerializableExtra("data1");
        brand_id = brandModel.getId() + "";
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
        adapter = new SeparateBuildingAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());

        binding.setScore("0");
        binding.setTotal("0.0");
        binding.setModel(brandModel);
        binding.shimmer.startShimmer();
        getBuildings();


        binding.btnNext.setOnClickListener(v -> {
            if (canNext) {
                binding.flDialog.setVisibility(View.VISIBLE);
            }
        });

        binding.btnCompare.setOnClickListener(v -> {
            if (canNext) {


                List<AddBuildModel> list = new ArrayList<>();

                List<SuggestionModel> selectedSuggestion = new ArrayList<>(getSelectedSuggestion());
                for (SuggestionModel suggestionModel :selectedSuggestion){
                    if (suggestionModel.getIs_final_level().equals("yes")){

                        List<String> products_ids = new ArrayList<>();
                        List<ProductModel> productModelList = new ArrayList<>();
                        for (SuggestionModel.Products model :suggestionModel.getSuggestions().getSelectedProducts()){
                            products_ids.add(model.getProduct().getId()+"");
                            productModelList.add(model.getProduct());
                        }

                        AddBuildModel addBuildModel = new AddBuildModel(suggestionModel.getId()+"",products_ids);
                        addBuildModel.setCategory_id(suggestionModel.getId()+"");
                        addBuildModel.setCategory_name(suggestionModel.getTrans_title());
                        addBuildModel.setCategory_image(suggestionModel.getImage());
                        addBuildModel.setProductModelList(productModelList);
                        list.add(addBuildModel);

                    }else {

                        for (CategoryModel model:suggestionModel.getSub_categoryModel()){
                            Log.e("eee2", model.getTrans_title());
                            List<String> sub_products_ids = new ArrayList<>();
                            List<ProductModel> subProductModelList = new ArrayList<>();

                            for (ProductModel productModel:model.getSelectedProduct()){
                                Log.e("title", productModel.getTrans_title());
                                sub_products_ids.add(productModel.getId()+"");
                                subProductModelList.add(productModel);
                            }

                            AddBuildModel addBuildModel = new AddBuildModel(model.getId()+"",sub_products_ids);
                            addBuildModel.setCategory_id(model.getId()+"");
                            addBuildModel.setCategory_name(model.getTrans_title());
                            addBuildModel.setCategory_image(model.getImage());
                            addBuildModel.setProductModelList(subProductModelList);
                            list.add(addBuildModel);
                        }



                    }

                }


                AddCompareModel model = new AddCompareModel(list);
                compare(model);
            }
        });

        binding.cardViewClose.setOnClickListener(v -> {
            Common.CloseKeyBoard(this, binding.edtName);

            binding.flDialog.setVisibility(View.GONE);
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
    }

    private void getBuildings() {
        Api.getService(Tags.base_url)
                .getSeparateGamingCategoryBuildingByBrandId(lang, brand_id)
                .enqueue(new Callback<SuggestionsDataModel>() {
                    @Override
                    public void onResponse(Call<SuggestionsDataModel> call, Response<SuggestionsDataModel> response) {

                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            if (response.body().getData().size() > 0) {
                                updateData(response.body().getData());
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
                    public void onFailure(Call<SuggestionsDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage() + "__");
                            binding.shimmer.stopShimmer();
                            binding.shimmer.setVisibility(View.GONE);

                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void updateData(List<SuggestionModel> data) {
        list.clear();


        for (int index = 0; index < data.size(); index++) {
            SuggestionModel model = data.get(index);
            SuggestionModel.Suggestions suggestions = model.getSuggestions();

            if (suggestions != null) {
                List<SuggestionModel.Products> productModelList = new ArrayList<>();
                if (suggestions.getProducts() != null && suggestions.getProducts().size() > 0) {
                    hasSuggestions = true;
                    productModelList.addAll(suggestions.getProducts());
                }

                suggestions.setSelectedProducts(productModelList);
                suggestions.setDefaultSelectedProducts(new ArrayList<>(productModelList));
                model.setSuggestions(suggestions);

                if (model.getIs_final_level().equals("no")) {
                    hasSuggestions = true;
                    categoryHasSubCategoryList.add(model);
                }
                list.add(model);


            }

        }


        if (categoryHasSubCategoryList.size() > 0) {
            getSubCategory(0);
        } else {
            calculateTotal_Points();
            adapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.GONE);
            binding.llTotal.setVisibility(View.VISIBLE);
            binding.flCompare.setVisibility(View.VISIBLE);

            if (hasSuggestions) {
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

    private void getSubCategory(int index) {

        if (index < categoryHasSubCategoryList.size()) {

            SuggestionModel suggestionModel = categoryHasSubCategoryList.get(index);

            Api.getService(Tags.base_url)
                    .getSeparateGamingSubCategoryBuilding(lang,suggestionModel.getId() + "",brand_id )
                    .enqueue(new Callback<SuggestionsDataModel>() {
                        @Override
                        public void onResponse(Call<SuggestionsDataModel> call, Response<SuggestionsDataModel> response) {

                            if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData().size() > 0) {

                                    List<SuggestionModel.Products> products = new ArrayList<>();
                                    List<CategoryModel> allSubCategoryList = new ArrayList<>();
                                    for (SuggestionModel model : response.body().getData()) {
                                        SuggestionModel.Suggestions suggestions = model.getSuggestions();

                                        if (suggestions.getProducts().size() > 0) {
                                            List<ProductModel> productModelList = new ArrayList<>();

                                            for (SuggestionModel.Products products1 : suggestions.getProducts()) {
                                                productModelList.add(products1.getProduct());
                                            }

                                            CategoryModel subCategoryModel = new CategoryModel(model.getId(), model.getDesc(), model.getImage(), model.getType(), model.getIs_final_level(), model.getParent_id(), model.getIs_in_compare(), model.getTrans_title(), new ArrayList<>(), productModelList,new ArrayList<>(productModelList));
                                            allSubCategoryList.add(subCategoryModel);
                                            suggestionModel.getSuggestions().setDefaultData(true);
                                            products.addAll(suggestions.getProducts());

                                            Log.e("ggg", model.getTrans_title() + "___" + list.size());

                                        }

                                    }

                                    suggestionModel.setDefault_sub_categoryModel(new ArrayList<>(allSubCategoryList));
                                    suggestionModel.setSub_categoryModel(new ArrayList<>(allSubCategoryList));

                                    SuggestionModel.Suggestions suggestions = suggestionModel.getSuggestions();
                                    if (suggestions != null) {
                                        suggestions.setSelectedProducts(products);
                                        suggestions.setDefaultSelectedProducts(new ArrayList<>(products));
                                        suggestionModel.setSuggestions(suggestions);
                                    }


                                    int newIndex = index + 1;
                                    categoryHasSubCategoryList.set(index, suggestionModel);

                                    getSubCategory(newIndex);

                                }

                            }


                        }

                        @Override
                        public void onFailure(Call<SuggestionsDataModel> call, Throwable t) {
                            try {
                                Log.e("error", t.getMessage() + "__");


                            } catch (Exception e) {

                            }
                        }
                    });
        } else {
            for (SuggestionModel model : categoryHasSubCategoryList) {
                int pos = getCategoryHasSubCategoryPos(model);
                if (pos != -1) {
                    SuggestionModel suggestionModel = list.get(pos);
                    suggestionModel.setSub_categoryModel(new ArrayList<>(model.getSub_categoryModel()));
                    suggestionModel.setDefault_sub_categoryModel(new ArrayList<>(model.getDefault_sub_categoryModel()));
                    SuggestionModel.Suggestions suggestions = suggestionModel.getSuggestions();
                    if (suggestions != null) {
                        suggestions.setDefaultData(true);
                        suggestions.setSelectedProducts(model.getSuggestions().getSelectedProducts());
                        suggestions.setDefaultSelectedProducts(new ArrayList<>(model.getSuggestions().getDefaultSelectedProducts()));
                        suggestions.setProducts(model.getSuggestions().getProducts());
                    }
                    list.set(pos, suggestionModel);
                }
            }

            binding.shimmer.stopShimmer();
            binding.shimmer.setVisibility(View.GONE);
            binding.llData.setVisibility(View.VISIBLE);
            calculateTotal_Points();
            adapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.GONE);
            binding.llTotal.setVisibility(View.VISIBLE);
            binding.flCompare.setVisibility(View.VISIBLE);

            if (hasSuggestions) {
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
                                    intent.putExtra("data", (Serializable) response.body().getData());
                                    startActivity(intent);

                                } else if (response.body().getStatus() == 403) {
                                    Toast.makeText(SeparateGamingCategoriesActivity.this, R.string.no_games, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.dismiss();
                            }

                        } else {
                            dialog.dismiss();

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

        List<SuggestionModel> selectedSuggestion = new ArrayList<>(getSelectedSuggestion());
        for (SuggestionModel suggestionModel :selectedSuggestion){
            if (suggestionModel.getIs_final_level().equals("yes")){

                List<String> products_ids = new ArrayList<>();
                List<ProductModel> productModelList = new ArrayList<>();
                for (SuggestionModel.Products model :suggestionModel.getSuggestions().getSelectedProducts()){
                    products_ids.add(model.getProduct().getId()+"");
                    productModelList.add(model.getProduct());
                }

                AddBuildModel addBuildModel = new AddBuildModel(suggestionModel.getId()+"",products_ids);
                addBuildModel.setCategory_id(suggestionModel.getId()+"");
                addBuildModel.setCategory_name(suggestionModel.getTrans_title());
                addBuildModel.setCategory_image(suggestionModel.getImage());
                addBuildModel.setProductModelList(productModelList);
                list.add(addBuildModel);

            }else {

                for (CategoryModel model:suggestionModel.getSub_categoryModel()){
                    Log.e("eee2", model.getTrans_title());
                    List<String> sub_products_ids = new ArrayList<>();
                    List<ProductModel> subProductModelList = new ArrayList<>();

                    for (ProductModel productModel:model.getSelectedProduct()){
                        Log.e("title", productModel.getTrans_title());
                        sub_products_ids.add(productModel.getId()+"");
                        subProductModelList.add(productModel);
                    }

                    AddBuildModel addBuildModel = new AddBuildModel(model.getId()+"",sub_products_ids);
                    addBuildModel.setCategory_id(model.getId()+"");
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

        List<SuggestionModel> selectedSuggestion = new ArrayList<>(getSelectedSuggestion());
        for (SuggestionModel suggestionModel :selectedSuggestion){
            if (suggestionModel.getIs_final_level().equals("yes")){

                List<String> products_ids = new ArrayList<>();
                List<ProductModel> productModelList = new ArrayList<>();
                for (SuggestionModel.Products model :suggestionModel.getSuggestions().getSelectedProducts()){
                    products_ids.add(model.getProduct().getId()+"");
                    productModelList.add(model.getProduct());
                }

                AddBuildModel addBuildModel = new AddBuildModel(suggestionModel.getId()+"",products_ids);
                addBuildModel.setCategory_id(suggestionModel.getId()+"");
                addBuildModel.setCategory_name(suggestionModel.getTrans_title());
                addBuildModel.setCategory_image(suggestionModel.getImage());
                addBuildModel.setProductModelList(productModelList);
                list.add(addBuildModel);

            }else {

                for (CategoryModel model:suggestionModel.getSub_categoryModel()){
                    Log.e("eee2", model.getTrans_title());
                    List<String> sub_products_ids = new ArrayList<>();
                    List<ProductModel> subProductModelList = new ArrayList<>();

                    for (ProductModel productModel:model.getSelectedProduct()){
                        Log.e("title", productModel.getTrans_title());
                        sub_products_ids.add(productModel.getId()+"");
                        subProductModelList.add(productModel);
                    }

                    AddBuildModel addBuildModel = new AddBuildModel(model.getId()+"",sub_products_ids);
                    addBuildModel.setCategory_id(model.getId()+"");
                    addBuildModel.setCategory_name(model.getTrans_title());
                    addBuildModel.setCategory_image(model.getImage());
                    addBuildModel.setProductModelList(subProductModelList);
                    list.add(addBuildModel);
                }



            }

        }







        manageCartModel.addBuildProduct(this, list, name, 1);
        binding.flDialog.setVisibility(View.GONE);
        Toast.makeText(this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
    }


    public void setItemData(int adapterPosition, SuggestionModel suggestionModel) {
        this.selectedPos = adapterPosition;
        this.suggestionModel = suggestionModel;



    }



    private void calculateTotal_Points() {
        total = 0;
        double points = 0;
        for (SuggestionModel model : list) {

            for (SuggestionModel.Products productModel : model.getSuggestions().getSelectedProducts()) {
                Log.e("s", model.getTrans_title() + "___" + productModel.getProduct().getPrice() + "__");

                total += productModel.getProduct().getPrice();
                points += productModel.getProduct().getPoints();

            }


        }


        binding.setTotal(total + "");
        binding.setScore(points + "");
    }

    private int getCategoryHasSubCategoryPos(SuggestionModel suggestionModel) {
        int pos = -1;
        for (int index = 0; index < list.size(); index++) {
            SuggestionModel model = list.get(index);
            if (model.getId() == suggestionModel.getId()) {
                pos = index;
                return pos;
            }
        }
        return pos;
    }

    private boolean isListHasData(){
        boolean hasData = false;
        for (SuggestionModel model : list) {

            for (SuggestionModel.Products productModel : model.getSuggestions().getSelectedProducts()) {
                hasData = true;
            }


        }
        return hasData;
    }

    private List<SuggestionModel> getSelectedSuggestion(){
        List<SuggestionModel> categoryModelList = new ArrayList<>();
        for (SuggestionModel suggestionModel:list){
            if (suggestionModel.getSuggestions().getSelectedProducts().size()>0){
                categoryModelList.add(suggestionModel);
            }
        }
        return categoryModelList;
    }

}