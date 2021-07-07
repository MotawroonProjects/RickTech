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
import com.app.ricktech.uis.suggestions_module.activity_suggestions.SuggestionBrandActivity;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BrandModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;

    public BrandAdapter(Context context, List<BrandModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
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
            if (appCompatActivity instanceof CategoriesActivity) {
                CategoriesActivity categoriesActivity = (CategoriesActivity) appCompatActivity;
                categoriesActivity.setItemData(list.get(myHolder.getAdapterPosition()));

            }else if (appCompatActivity instanceof SuggestionBrandActivity) {
                SuggestionBrandActivity activity = (SuggestionBrandActivity) appCompatActivity;
                activity.setItemData(list.get(myHolder.getAdapterPosition()));

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
