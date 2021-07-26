package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CartBuildProductRowBinding;
import com.app.ricktech.databinding.CartBuildRowBinding;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.uis.general_module.activity_cart_details.CartDetailsActivity;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Cart;

import java.util.List;

public class CartBuildProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CartModel.Component> list;
    private Context context;
    private LayoutInflater inflater;
    private int build_parent_pos;
    private  CartDetailsActivity activity;
    public CartBuildProductsAdapter(Context context, List<CartModel.Component> list, int parent_pos) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.build_parent_pos = parent_pos;
        activity = (CartDetailsActivity) context;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CartBuildProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_build_product_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        CartModel.Component component = list.get(position);
        myHolder.binding.setModel(component);
        myHolder.binding.recViewProducts.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        CartProductsAdapter adapter = new CartProductsAdapter(context,component.getComponentItemList(),build_parent_pos,myHolder.getAdapterPosition());
        myHolder.binding.recViewProducts.setAdapter(adapter);


        myHolder.binding.llExpand.setOnClickListener(v -> {
            if (myHolder.binding.expandedLayout.isExpanded()){
                myHolder.binding.expandedLayout.collapse(true);

            }else {
                myHolder.binding.expandedLayout.expand(true);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private CartBuildProductRowBinding binding;

        public MyHolder(CartBuildProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
