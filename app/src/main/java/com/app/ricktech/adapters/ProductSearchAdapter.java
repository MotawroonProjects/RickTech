package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.LabtopRowBinding;
import com.app.ricktech.databinding.ProductRow2Binding;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.uis.accessory_module.activity_accessory_products.AccessoryProductsActivity;
import com.app.ricktech.uis.gaming_laptop_module.activity_product.ProductActivity;
import com.app.ricktech.uis.general_module.activity_search.SearchActivity;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;
import com.app.ricktech.uis.suggestions_module.activity_suggestion_products.SuggestionProductsActivity;

import java.util.List;

import io.paperdb.Paper;

public class ProductSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    private String lang;
    public ProductSearchAdapter(Context context, List<ProductModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
        Paper.init(context);
        lang = Paper.book().read("lang", "de");
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProductRow2Binding binding = DataBindingUtil.inflate(inflater, R.layout.product_row2, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);
        myHolder.itemView.setOnClickListener(v -> {
            if (activity instanceof SearchActivity){
                SearchActivity searchActivity = (SearchActivity) activity;
                searchActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));
            }


        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ProductRow2Binding binding;

        public MyHolder(ProductRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
