package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.app.ricktech.R;
import com.app.ricktech.databinding.CategoryRowBinding;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.uis.gaming_laptop_module.activity_categories.CategoriesActivity;
import com.app.ricktech.uis.separate_gaming_module.separate_gaming_brand.SeparateGamingBrandActivity;
import com.app.ricktech.uis.separate_laptop_gaming_module.separate_laptop_gaming_brand.SeparateLaptopGamingBrandActivity;
import com.app.ricktech.uis.separate_not_book_module.separate_note_book_brand.SeparateNoteBookBrandActivity;
import com.app.ricktech.uis.separate_pc_module.separate_pc_brand.SeparatePcBrandActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BrandModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    public CategoryAdapter(Context context, List<BrandModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            if (activity instanceof CategoriesActivity){
                CategoriesActivity categoriesActivity=(CategoriesActivity)activity;
                categoriesActivity.setItemData(list.get(myHolder.getAdapterPosition()));
            }else if (activity instanceof SeparatePcBrandActivity){

                SeparatePcBrandActivity separatePcBrandActivity=(SeparatePcBrandActivity)activity;
                separatePcBrandActivity.setItemData(list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparateGamingBrandActivity){

                SeparateGamingBrandActivity separateGamingBrandActivity=(SeparateGamingBrandActivity)activity;
                separateGamingBrandActivity.setItemData(list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparateLaptopGamingBrandActivity){

                SeparateLaptopGamingBrandActivity separateLaptopGamingBrandActivity=(SeparateLaptopGamingBrandActivity)activity;
                separateLaptopGamingBrandActivity.setItemData(list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SeparateNoteBookBrandActivity){

                SeparateNoteBookBrandActivity separateNoteBookBrandActivity=(SeparateNoteBookBrandActivity)activity;
                separateNoteBookBrandActivity.setItemData(list.get(myHolder.getAdapterPosition()));

            }





        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private CategoryRowBinding binding;

        public MyHolder(CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
