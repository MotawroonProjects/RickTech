package com.app.ricktech.uis.suggestions_module.activity_suggestion_buildings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.ricktech.R;
import com.app.ricktech.adapters.BuildingAdapter;
import com.app.ricktech.adapters.SubBuildingAdapter;
import com.app.ricktech.adapters.SuggestionBuildingAdapter;
import com.app.ricktech.databinding.ActivitySuggetionBuildingsBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.AddBuildModel;
import com.app.ricktech.models.AddCompareModel;
import com.app.ricktech.models.AddToBuildDataModel;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.models.CategoryBuildingDataModel;
import com.app.ricktech.models.CategoryModel;
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
import com.app.ricktech.uis.pc_building_module.activity_building.BulidingActivity;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;
import com.app.ricktech.uis.pc_building_module.activity_games.GamesActivity;
import com.app.ricktech.uis.pc_building_module.activity_sub_bulding.SubBuildingActivity;
import com.app.ricktech.uis.suggestions_module.activity_suggestion_sub_building.SuggestionSubBuildingActivity;
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

public class SuggetionBuildingsActivity extends AppCompatActivity {

    private ActivitySuggetionBuildingsBinding binding;
    private String lang;
    private SuggestionBuildingAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<SuggestionModel> list;
    private ActivityResultLauncher<Intent> launcher;
    private int selectedPos = -1;
    private SuggestionModel suggestionModel;
    private String brand_id = "", suggestion_id = "";
    private BrandModel brandModel;
    private int req;
    private boolean canNext = false;
    double total = 0;
    private Map<Integer,AddBuildModel> map;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
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
        suggestion_id = intent.getStringExtra("data2");
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
        adapter = new SuggestionBuildingAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());

        binding.setScore("0");
        binding.setTotal("0.0");
        binding.setModel(brandModel);
        binding.shimmer.startShimmer();
        getBuildings();

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
                                AddBuildModel model = new AddBuildModel(brandModel.getId()+"", productModelList);
                                map.put(selectedPos, model);
                            }

                            List<SuggestionModel.Products> list1 = list.get(selectedPos).getSuggestions().getSelectedProducts();
                            if (list1==null){
                                list1 = new ArrayList<>();
                            }
                            SuggestionModel.Products products = new SuggestionModel.Products();
                            products.setProduct(productModel);
                            list1.add(products);
                            SuggestionModel.Suggestions suggestions = suggestionModel.getSuggestions();
                            suggestions.setSelectedProducts(list1);
                            suggestions.setDefaultData(false);

                            suggestionModel.setSuggestions(suggestions);
                            list.set(selectedPos, suggestionModel);
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
                } else if (req == 200 && result.getResultCode() == RESULT_OK && result.getData() != null) {
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
                                AddBuildModel model = new AddBuildModel(brandModel.getId()+"", productModelList);
                                map.put(selectedPos, model);

                            }

                            List<SuggestionModel.Products> productsList = new ArrayList<>();
                            for (ProductModel productModel:data){
                                SuggestionModel.Products products = new SuggestionModel.Products();
                                products.setProduct(productModel);
                                productsList.add(products);
                            }
                            SuggestionModel.Suggestions suggestions =suggestionModel.getSuggestions();
                            suggestions.setSelectedProducts(productsList);
                            suggestions.setDefaultData(false);
                            suggestionModel.setSuggestions(suggestions);

                            list.set(selectedPos, suggestionModel);

                            adapter.notifyItemChanged(selectedPos);

                            calculateTotal_Points();

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
                .compare(lang,model)
                .enqueue(new Callback<SuggestionGameDataModel>() {
                    @Override
                    public void onResponse(Call<SuggestionGameDataModel> call, Response<SuggestionGameDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null ) {
                                if (response.body().getStatus() == 200)
                                {
                                    Intent intent = new Intent(SuggetionBuildingsActivity.this, GamesActivity.class);
                                    Log.e("size", response.body().getData().size()+"__");
                                    intent.putExtra("data", (Serializable) response.body().getData());
                                    startActivity(intent);

                                }else if(response.body().getStatus() == 403){
                                    Toast.makeText(SuggetionBuildingsActivity.this, R.string.no_games, Toast.LENGTH_SHORT).show();
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
        AddToBuildDataModel model = new AddToBuildDataModel(name,total,list);


        Api.getService(Tags.base_url)
                .addToBuild(lang,"Bearer " + userModel.getData().getToken(),model)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Toast.makeText(SuggetionBuildingsActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();
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
                .getCategorySuggestions(lang, brand_id, suggestion_id)
                .enqueue(new Callback<SuggestionsDataModel>() {
                    @Override
                    public void onResponse(Call<SuggestionsDataModel> call, Response<SuggestionsDataModel> response) {
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.llData.setVisibility(View.VISIBLE);
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


        for (int index=0;index<data.size();index++){
            SuggestionModel model = data.get(index);
            SuggestionModel.Suggestions suggestions = model.getSuggestions();
            List<SuggestionModel.Products> productModelList = new ArrayList<>();

            if (suggestions!=null&&suggestions.getProducts()!=null&&suggestions.getProducts().size()>0){

                productModelList.addAll(suggestions.getProducts());
            }

            suggestions.setSelectedProducts(productModelList);
            model.setSuggestions(suggestions);
            list.add(model);
            if (suggestions.getProducts().size()>0){
                List<String> modelList = new ArrayList<>();
                for (SuggestionModel.Products products:suggestions.getProducts()){
                    modelList.add(products.getProduct().getId()+"");
                }

                AddBuildModel addBuildModel = new AddBuildModel(model.getId()+"", modelList);
                map.put(index, addBuildModel);

            }
        }


        calculateTotal_Points();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        binding.llTotal.setVisibility(View.VISIBLE);
        binding.flCompare.setVisibility(View.VISIBLE);

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

    public void setItemData(int adapterPosition, SuggestionModel suggestionModel) {
        this.selectedPos = adapterPosition;
        this.suggestionModel = suggestionModel;
        if (suggestionModel.getIs_final_level().equals("yes")) {
            req = 100;
            Intent intent = new Intent(this, ProductBuildingActivity.class);
            intent.putExtra("data", suggestionModel.getId()+"");
            launcher.launch(intent);

        } else {
            req = 200;
            Intent intent = new Intent(this, SuggestionSubBuildingActivity.class);
            intent.putExtra("data", suggestionModel.getSuggestions().getCategory_id_to_get_next_level());
            launcher.launch(intent);

        }


    }

    public void deleteItemData(int adapterPosition, SuggestionModel suggestionModel) {
        map.remove(adapterPosition);
        suggestionModel.getSuggestions().getSelectedProducts().clear();

        list.set(adapterPosition,suggestionModel);
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
    public void resetItemData(int adapterPosition, SuggestionModel suggestionModel) {
        SuggestionModel.Suggestions suggestions = suggestionModel.getSuggestions();

        if (suggestions.getProducts().size()>0){
            List<String> modelList = new ArrayList<>();
            for (SuggestionModel.Products products:suggestions.getProducts()){
                modelList.add(products.getProduct().getId()+"");
            }

            AddBuildModel addBuildModel = new AddBuildModel(suggestionModel.getId()+"", modelList);
            map.put(adapterPosition, addBuildModel);

        }

        Log.e("siizzz", suggestions.getProducts().size()+"___");
        suggestions.setDefaultData(true);
        suggestions.getSelectedProducts().clear();
        suggestions.setSelectedProducts(suggestions.getProducts());
        suggestionModel.setSuggestions(suggestions);
        Log.e("dddd", suggestions.getSelectedProducts().size()+"__");

        list.set(adapterPosition,suggestionModel);
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
            SuggestionModel model = list.get(key);
            for (SuggestionModel.Products productModel:model.getSuggestions().getSelectedProducts()){
                total+= productModel.getProduct().getPrice();
                points += productModel.getProduct().getPoints();
            }
        }

        binding.setTotal(total + "");
        binding.setScore(points + "");
    }
}