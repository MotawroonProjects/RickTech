package com.app.ricktech.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CartItemRowBinding;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.uis.general_module.activity_cart_details.CartDetailsActivity;

import java.util.List;

public class CartSingleProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CartModel.SingleProduct> list;
    private Context context;
    private LayoutInflater inflater;
    private CartDetailsActivity activity;

    public CartSingleProductsAdapter(Context context, List<CartModel.SingleProduct> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (CartDetailsActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CartItemRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_item_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        CartModel.SingleProduct product = list.get(position);
        myHolder.binding.setModel(product);
        myHolder.binding.increment.setOnClickListener(v -> {
            Log.e("ddd", "rr");
            CartModel.SingleProduct model = list.get(myHolder.getAdapterPosition());
            int newAmount = model.getAmount()+1;
            model.setAmount(newAmount);
            list.set(myHolder.getAdapterPosition(),model);
            notifyItemChanged(myHolder.getAdapterPosition());
            activity.updateSingleProduct(model);

        });
        myHolder.binding.decrement.setOnClickListener(v -> {

            CartModel.SingleProduct model = list.get(myHolder.getAdapterPosition());
            if (model.getAmount()>1){
                int newAmount = model.getAmount()-1;
                model.setAmount(newAmount);
                list.set(myHolder.getAdapterPosition(),model);
                notifyItemChanged(myHolder.getAdapterPosition());
                activity.updateSingleProduct(model);
            }


        });

        myHolder.binding.cardDelete.setOnClickListener(v -> {
            CartModel.SingleProduct model = list.get(myHolder.getAdapterPosition());
            activity.removeSingleProduct(myHolder.getAdapterPosition(),model);


        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private CartItemRowBinding binding;

        public MyHolder(CartItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
