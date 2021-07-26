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
import com.app.ricktech.databinding.ProductRowBinding;
import com.app.ricktech.models.OrderModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.uis.accessory_module.activity_accessory_products.AccessoryProductsActivity;
import com.app.ricktech.uis.gaming_laptop_module.activity_product.ProductActivity;
import com.app.ricktech.uis.pc_building_module.activity_building_products.ProductBuildingActivity;
import com.app.ricktech.uis.suggestions_module.activity_suggestion_products.SuggestionProductsActivity;

import java.util.List;

import io.paperdb.Paper;

public class OrderProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel.OrderProducts> list;
    private Context context;
    private LayoutInflater inflater;
    public OrderProductAdapter(Context context, List<OrderModel.OrderProducts> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.product_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ProductRowBinding binding;

        public MyHolder(ProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
