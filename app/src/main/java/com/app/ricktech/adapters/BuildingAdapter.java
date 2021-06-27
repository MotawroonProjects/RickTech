package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.BuildingRowBinding;
import com.app.ricktech.databinding.CategoryRowBinding;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.uis.gaming_laptop_module.activity_categories.CategoriesActivity;
import com.app.ricktech.uis.pc_building_module.activity_building.BulidingActivity;

import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private BulidingActivity activity;
    public BuildingAdapter(Context context, List<CategoryModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (BulidingActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BuildingRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.building_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            activity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));
        });

        myHolder.binding.imageDelete.setOnClickListener(v -> {
            activity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private BuildingRowBinding binding;

        public MyHolder(BuildingRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
