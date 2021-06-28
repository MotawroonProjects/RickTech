package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CategoryRowBinding;
import com.app.ricktech.databinding.ComponentGameRowBinding;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.models.CompareComponentModel;
import com.app.ricktech.uis.gaming_laptop_module.activity_categories.CategoriesActivity;

import java.util.List;

public class ComponentGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CompareComponentModel> list;
    private Context context;
    private LayoutInflater inflater;
    public ComponentGameAdapter(Context context, List<CompareComponentModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ComponentGameRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.component_game_row, parent, false);
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
        private ComponentGameRowBinding binding;

        public MyHolder(ComponentGameRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
