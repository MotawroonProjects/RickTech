package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.BuildingRowBinding;
import com.app.ricktech.databinding.SubBuildingRowBinding;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.uis.pc_building_module.activity_building.BulidingActivity;
import com.app.ricktech.uis.pc_building_module.activity_sub_bulding.SubBuildingActivity;
import com.app.ricktech.uis.suggestions_module.activity_suggestion_sub_building.SuggestionSubBuildingActivity;

import java.util.List;

public class SubBuildingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;
    public SubBuildingAdapter(Context context, List<CategoryModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SubBuildingRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.sub_building_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            if (appCompatActivity instanceof SubBuildingActivity){
                SubBuildingActivity activity = (SubBuildingActivity) appCompatActivity;
                activity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            } else if (appCompatActivity instanceof SuggestionSubBuildingActivity) {
                SuggestionSubBuildingActivity activity = (SuggestionSubBuildingActivity) appCompatActivity;
                activity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));


            }

        });

        myHolder.binding.imageDelete.setOnClickListener(v -> {
            if (appCompatActivity instanceof SubBuildingActivity){
                SubBuildingActivity activity = (SubBuildingActivity) appCompatActivity;
                activity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            } else if (appCompatActivity instanceof SuggestionSubBuildingActivity) {
                SuggestionSubBuildingActivity activity = (SuggestionSubBuildingActivity) appCompatActivity;
                activity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));


            }


        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private SubBuildingRowBinding binding;

        public MyHolder(SubBuildingRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
