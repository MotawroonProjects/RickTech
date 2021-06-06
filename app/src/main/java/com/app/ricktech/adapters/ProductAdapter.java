package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CategoryRowBinding;
import com.app.ricktech.databinding.LabtopRowBinding;
import com.app.ricktech.uis.activity_categories.CategoriesActivity;
import com.app.ricktech.uis.activity_product.ProductActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    public ProductAdapter(Context context, List<Object> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
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

        myHolder.itemView.setOnClickListener(v -> {
            ProductActivity categoriesActivity=(ProductActivity) context;
            categoriesActivity.show();
        });

    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private LabtopRowBinding binding;

        public MyHolder(LabtopRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
