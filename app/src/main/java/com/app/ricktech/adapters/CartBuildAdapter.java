package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CartBuildRowBinding;
import com.app.ricktech.databinding.CartRowBinding;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.uis.general_module.activity_cart_details.CartDetailsActivity;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Cart;

import java.util.List;

public class CartBuildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CartModel.BuildProduct> list;
    private Context context;
    private LayoutInflater inflater;
    private CartDetailsActivity activity;
    public CartBuildAdapter(Context context, List<CartModel.BuildProduct> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (CartDetailsActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CartBuildRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_build_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        int newPos = position+1;
        myHolder.binding.setPos(newPos+"");
        myHolder.binding.recView.setLayoutManager(new LinearLayoutManager(context));
        CartBuildProductsAdapter adapter = new CartBuildProductsAdapter(context, list.get(position).getComponents(), holder.getAdapterPosition());
        myHolder.binding.recView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private CartBuildRowBinding binding;

        public MyHolder(CartBuildRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
