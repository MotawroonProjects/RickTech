package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CartBuildProductRowBinding;
import com.app.ricktech.databinding.OrderBuildProductRowBinding;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.models.CustomBuildModel;
import com.app.ricktech.models.SuggestionModel;
import com.app.ricktech.uis.general_module.activity_cart_details.CartDetailsActivity;

import java.util.List;

public class OrderBuildProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CustomBuildModel> list;
    private Context context;
    private LayoutInflater inflater;
    public OrderBuildProductsAdapter(Context context, List<CustomBuildModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        OrderBuildProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_build_product_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        CustomBuildModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.recViewProducts.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        OrderProductsAdapter adapter = new OrderProductsAdapter(context,model.getProductModelList());
        myHolder.binding.recViewProducts.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private OrderBuildProductRowBinding binding;

        public MyHolder(OrderBuildProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
