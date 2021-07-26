package com.app.ricktech.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.OfferRowBinding;
import com.app.ricktech.databinding.SavedProductRowBinding;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SavedProductDataModel;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Offers;
import com.app.ricktech.uis.saved_build_module.activity_saving_build.SavingBuildActivity;

import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SavedProductDataModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;
    public SavedAdapter(Context context, List<SavedProductDataModel.Data> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SavedProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.saved_product_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.llDetails.setOnClickListener(v -> {
            if (appCompatActivity instanceof SavingBuildActivity){
                SavingBuildActivity savingBuildActivity = (SavingBuildActivity) appCompatActivity;
                savingBuildActivity.setItemData(list.get(myHolder.getAdapterPosition()));
            }
        });

        myHolder.binding.llDelete.setOnClickListener(v -> {
            if (appCompatActivity instanceof SavingBuildActivity){
                SavingBuildActivity savingBuildActivity = (SavingBuildActivity) appCompatActivity;
                savingBuildActivity.deleteItem(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private SavedProductRowBinding binding;

        public MyHolder(SavedProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
