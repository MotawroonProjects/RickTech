package com.app.ricktech.uis.saved_build_module.activity_saving_build;

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
import com.app.ricktech.adapters.SavedAdapter;
import com.app.ricktech.databinding.ActivitySavingBuildBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.AddBuildModel;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SavedProductDataModel;
import com.app.ricktech.models.StatusResponse;
import com.app.ricktech.models.SuggestionModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.saved_build_module.activity_saved_buldings.SavedBuildingsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavingBuildActivity extends AppCompatActivity {
    private ActivitySavingBuildBinding binding;
    private String lang;
    private SavedAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private List<SavedProductDataModel.Data> list;
    private ActivityResultLauncher<Intent> launcher;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saving_build);
        initView();
    }



    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        list = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavedAdapter(this, list);
        binding.recView.setAdapter(adapter);
        binding.recView.setItemAnimator(new DefaultItemAnimator());
        binding.llBack.setOnClickListener(v -> finish());
        binding.shimmer.startShimmer();
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(this::getSavingBuilds);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()==RESULT_OK){
                list.clear();
                adapter.notifyDataSetChanged();
                binding.shimmer.startShimmer();
                binding.shimmer.setVisibility(View.VISIBLE);
                getSavingBuilds();
            }
        });
        getSavingBuilds();

    }

    private void getSavingBuilds() {
        Api.getService(Tags.base_url)
                .getSavedBuilding(lang,"Bearer "+userModel.getData().getToken())
                .enqueue(new Callback<SavedProductDataModel>() {
                    @Override
                    public void onResponse(Call<SavedProductDataModel> call, Response<SavedProductDataModel> response) {
                        binding.shimmer.stopShimmer();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null&&response.body().getStatus()==200 ) {
                            if (response.body().getData().size() > 0) {
                                list.clear();
                                list.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                binding.tvNoData.setVisibility(View.GONE);
                            } else{
                                binding.tvNoData.setVisibility(View.VISIBLE);

                            }

                        } else {
                            binding.swipeRefresh.setRefreshing(false);
                            binding.shimmer.stopShimmer();
                            binding.shimmer.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<SavedProductDataModel> call, Throwable t) {
                        try {
                            Log.e("error", t.getMessage()+"__");
                            binding.swipeRefresh.setRefreshing(false);
                            binding.shimmer.stopShimmer();
                            binding.shimmer.setVisibility(View.GONE);
                        } catch (Exception e) {

                        }
                    }
                });
    }


    public void setItemData(SavedProductDataModel.Data data) {
        Intent intent = new Intent(this, SavedBuildingsActivity.class);
        intent.putExtra("data", data.getId());
        intent.putExtra("data2", data.getTrans_title());

        launcher.launch(intent);

    }

    public void deleteItem(SavedProductDataModel.Data data, int adapterPosition) {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .deleteSavedBuilding(lang,data.getId())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                       dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null&&response.body().getStatus()==200 ) {
                            list.remove(adapterPosition);
                            adapter.notifyItemRemoved(adapterPosition);

                            if (list.size()==0){
                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            dialog.dismiss();

                        }


                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            Log.e("error", t.getMessage()+"__");

                        } catch (Exception e) {

                        }
                    }
                });

    }



}