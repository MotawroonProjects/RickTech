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
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.uis.pc_building_module.activity_building.BulidingActivity;
import com.app.ricktech.uis.separate_gaming_module.separate_gaming_categories.SeparateGamingCategoriesActivity;
import com.app.ricktech.uis.separate_laptop_gaming_module.separate_laptop_gaming_categories.SeparateLaptopGamingCategoriesActivity;
import com.app.ricktech.uis.separate_not_book_module.separate_note_book_categories.SeparateNoteBookCategoriesActivity;
import com.app.ricktech.uis.separate_pc_module.separate_pc_categories.SeparatePcCategoriesActivity;

import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    public BuildingAdapter(Context context, List<CategoryModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
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
            if (activity instanceof BulidingActivity){
                BulidingActivity bulidingActivity = (BulidingActivity) activity;
                bulidingActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparatePcCategoriesActivity){
                SeparatePcCategoriesActivity separatePcCategoriesActivity = (SeparatePcCategoriesActivity) activity;
                separatePcCategoriesActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparateGamingCategoriesActivity){
                SeparateGamingCategoriesActivity separateGamingCategoriesActivity = (SeparateGamingCategoriesActivity) activity;
                separateGamingCategoriesActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparateLaptopGamingCategoriesActivity){
                SeparateLaptopGamingCategoriesActivity separateLaptopGamingCategoriesActivity = (SeparateLaptopGamingCategoriesActivity) activity;
                separateLaptopGamingCategoriesActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparateNoteBookCategoriesActivity){
                SeparateNoteBookCategoriesActivity separateNoteBookCategoriesActivity = (SeparateNoteBookCategoriesActivity) activity;
                separateNoteBookCategoriesActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }

        });

        myHolder.binding.imageDelete.setOnClickListener(v -> {
            if (activity instanceof BulidingActivity){
                BulidingActivity bulidingActivity = (BulidingActivity) activity;
                bulidingActivity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparatePcCategoriesActivity){
                SeparatePcCategoriesActivity separatePcCategoriesActivity = (SeparatePcCategoriesActivity) activity;
                separatePcCategoriesActivity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparateGamingCategoriesActivity){
                SeparateGamingCategoriesActivity separateGamingCategoriesActivity = (SeparateGamingCategoriesActivity) activity;
                separateGamingCategoriesActivity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparateLaptopGamingCategoriesActivity){
                SeparateLaptopGamingCategoriesActivity separateLaptopGamingCategoriesActivity = (SeparateLaptopGamingCategoriesActivity) activity;
                separateLaptopGamingCategoriesActivity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparateNoteBookCategoriesActivity){
                SeparateNoteBookCategoriesActivity separateNoteBookCategoriesActivity = (SeparateNoteBookCategoriesActivity) activity;
                separateNoteBookCategoriesActivity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }

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
