package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CartRowBinding;
import com.app.ricktech.databinding.CategoryRowBinding;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.uis.gaming_laptop_module.activity_categories.CategoriesActivity;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CartModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Cart fragment_cart;
    public CartAdapter(Context context, List<CartModel> list,Fragment_Cart fragment_cart) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment_cart = fragment_cart;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CartRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        int index = position+1;
        myHolder.binding.setIndex(index+"");
        myHolder.itemView.setOnClickListener(v -> {
            fragment_cart.setItemData(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
        });

        myHolder.binding.llDelete.setOnClickListener(v -> {
            fragment_cart.deleteItemData(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private CartRowBinding binding;

        public MyHolder(CartRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
