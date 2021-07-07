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
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.uis.accessory_module.activity_accessory_products.AccessoryProductsActivity;
import com.app.ricktech.uis.gaming_laptop_module.activity_product.ProductActivity;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;
import com.app.ricktech.uis.suggestions_module.activity_suggestion_products.SuggestionProductsActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    public ProductAdapter(Context context, List<ProductModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LabtopRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.labtop_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            if (activity instanceof ProductActivity){
                ProductActivity productActivity = (ProductActivity) activity;
                productActivity.setItemData(list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof ProductBuildingActivity){
                ProductBuildingActivity  buildingActivity = (ProductBuildingActivity) activity;
                buildingActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));
            }else if (activity instanceof AccessoryProductsActivity){
                AccessoryProductsActivity  accessoryProductsActivity = (AccessoryProductsActivity) activity;
                accessoryProductsActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));
            }else if (activity instanceof SuggestionProductsActivity){
                SuggestionProductsActivity suggestionProductsActivity = (SuggestionProductsActivity) activity;
                suggestionProductsActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));
            }


        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private LabtopRowBinding binding;

        public MyHolder(LabtopRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
