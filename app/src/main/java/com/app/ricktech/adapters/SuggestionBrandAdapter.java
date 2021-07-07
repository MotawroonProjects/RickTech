package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.BrandRowBinding;
import com.app.ricktech.databinding.SubBuildingRowBinding;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.SuggestionBrandDataModel;
import com.app.ricktech.uis.accessory_module.activity_accessories.AccessoriesActivity;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Home;

import java.util.List;

public class SuggestionBrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SuggestionBrandDataModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Home fragment_home;
    public SuggestionBrandAdapter(Context context, List<SuggestionBrandDataModel.Data> list,Fragment_Home fragment_home) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment_home = fragment_home;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BrandRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.brand_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            fragment_home.setBrandItemData(list.get(myHolder.getAdapterPosition()));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private BrandRowBinding binding;

        public MyHolder(BrandRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
