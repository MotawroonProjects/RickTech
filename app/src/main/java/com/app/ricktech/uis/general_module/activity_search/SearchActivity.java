package com.app.ricktech.uis.general_module.activity_search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.app.ricktech.R;
import com.app.ricktech.adapters.ProductSearchAdapter;
import com.app.ricktech.databinding.ActivitySearchBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SearchDataModel;
import com.app.ricktech.remote.Api;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.general_module.activity_product_detials.ProductDetialsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private String lang;
    private List<ProductModel> list;
    private ProductSearchAdapter adapter;
    private String filter_type = "";
    private Call<SearchDataModel> call;
    private int currentPage = 1;



    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        filter_type = intent.getStringExtra("data");

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "de");
        list = new ArrayList<>();
        adapter = new ProductSearchAdapter(this, list);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recView.setAdapter(adapter);

        search(null, currentPage);

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    list.clear();
                    adapter.notifyDataSetChanged();
                    String query =s.toString().isEmpty()?null:s.toString();
                    currentPage =1;
                    search(query, currentPage);
                }else {
                    list.clear();
                    adapter.notifyDataSetChanged();
                    currentPage =1;
                    search(s.toString(), currentPage);

                }
            }
        });



        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0){
                    int currentPos = ((GridLayoutManager)binding.recView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    int totalItems = adapter.getItemCount();
                    if (totalItems>=10&&currentPos<=(totalItems-3)){
                        int newPage = currentPage+1;
                        String query =binding.edtSearch.getText().toString().isEmpty()?null:binding.edtSearch.getText().toString();
                        search(query,newPage);

                    }
                }
            }
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            list.clear();
            adapter.notifyDataSetChanged();
            currentPage = 1;
            String query =binding.edtSearch.getText().toString().isEmpty()?null:binding.edtSearch.getText().toString();
            search(query,currentPage);

        });
    }

    private void search(String query, int page) {
        Log.e("dd", page+"__"+query);
        binding.swipeRefresh.setRefreshing(true);

        if (call != null) {
            call.cancel();
        }
        call = Api.getService(Tags.base_url)
                .search(lang, filter_type, query, page);

        call.enqueue(new Callback<SearchDataModel>() {
            @Override
            public void onResponse(Call<SearchDataModel> call, Response<SearchDataModel> response) {
                binding.swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {

                    if (response.body().getData().size() > 0) {
                        int oldPos = list.size()-1;
                        list.addAll(response.body().getData());
                        int newPos = list.size();
                        adapter.notifyItemRangeChanged(oldPos,newPos);
                        currentPage = response.body().getCurrent_page();
                    } else {

                    }

                } else {
                    binding.swipeRefresh.setRefreshing(false);

                }


            }

            @Override
            public void onFailure(Call<SearchDataModel> call, Throwable t) {
                try {
                    binding.swipeRefresh.setRefreshing(false);

                    Log.e("error", t.getMessage() + "__");

                } catch (Exception e) {

                }
            }
        });
    }

    public void setItemData(int adapterPosition, ProductModel productModel) {
        Intent intent = new Intent(this, ProductDetialsActivity.class);
        intent.putExtra("data", String.valueOf(productModel.getId()));
        startActivity(intent);
    }
}