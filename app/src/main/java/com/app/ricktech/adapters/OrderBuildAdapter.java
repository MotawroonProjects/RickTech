package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CategoryRowBinding;
import com.app.ricktech.databinding.OrderBuildRowBinding;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.models.OrderModel;
import com.app.ricktech.uis.gaming_laptop_module.activity_categories.CategoriesActivity;
import com.app.ricktech.uis.orders_module.activity_order_details.OrderDetailsActivity;
import com.app.ricktech.uis.suggestions_module.activity_suggestions.SuggestionBrandActivity;

import java.util.List;

public class OrderBuildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel.OrderPc> list;
    private Context context;
    private LayoutInflater inflater;
    private OrderDetailsActivity activity;

    public OrderBuildAdapter(Context context, List<OrderModel.OrderPc> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (OrderDetailsActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        OrderBuildRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_build_row, parent, false);
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
        private OrderBuildRowBinding binding;

        public MyHolder(OrderBuildRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
