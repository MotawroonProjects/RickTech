package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CartBuildProductRowBinding;
import com.app.ricktech.databinding.CartItemRowBinding;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.uis.general_module.activity_cart_details.CartDetailsActivity;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Cart;

import java.util.List;

public class CartProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CartModel.SingleProduct> list;
    private Context context;
    private LayoutInflater inflater;
    private int build_parent_pos;
    private int category_parent_pos;
    private CartDetailsActivity activity;

    public CartProductsAdapter(Context context, List<CartModel.SingleProduct> list, int parent_pos, int parent_pos_level2) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.build_parent_pos = parent_pos;
        this.category_parent_pos = parent_pos_level2;
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
            CartModel.SingleProduct model = list.get(myHolder.getAdapterPosition());
            int newAmount  = model.getAmount()+1;
            model.setAmount(newAmount);
            notifyItemChanged(myHolder.getAdapterPosition());
            activity.updateBuildProduct(model,build_parent_pos,category_parent_pos,myHolder.getAdapterPosition());
        });

        myHolder.binding.decrement.setOnClickListener(v -> {
            CartModel.SingleProduct model = list.get(myHolder.getAdapterPosition());
            if (model.getAmount()>1){
                int newAmount  = model.getAmount()-1;
                model.setAmount(newAmount);
                notifyItemChanged(myHolder.getAdapterPosition());
                activity.updateBuildProduct(model,build_parent_pos,category_parent_pos,myHolder.getAdapterPosition());

            }
        });

        myHolder.binding.cardDelete.setOnClickListener(v -> {
            CartModel.SingleProduct model = list.get(myHolder.getAdapterPosition());
            activity.removeBuildProduct(build_parent_pos,category_parent_pos,myHolder.getAdapterPosition());

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
