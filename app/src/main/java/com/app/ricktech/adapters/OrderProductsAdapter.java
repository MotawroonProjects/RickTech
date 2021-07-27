package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CartItemRowBinding;
import com.app.ricktech.databinding.OrderProductItemRowBinding;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.models.OrderModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SuggestionModel;
import com.app.ricktech.uis.general_module.activity_cart_details.CartDetailsActivity;

import java.util.List;

public class OrderProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel.Product> list;
    private Context context;
    private LayoutInflater inflater;


    public OrderProductsAdapter(Context context, List<OrderModel.Product> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        OrderProductItemRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_product_item_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        OrderModel.Product product = list.get(position);
        myHolder.binding.setModel(product);




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private OrderProductItemRowBinding binding;

        public MyHolder(OrderProductItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
