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
import com.app.ricktech.databinding.SuggestionBuildingRowBinding;
import com.app.ricktech.models.CategoryModel;
import com.app.ricktech.models.SuggestionModel;
import com.app.ricktech.uis.pc_building_module.activity_building.BulidingActivity;
import com.app.ricktech.uis.saved_build_module.activity_saved_buldings.SavedBuildingsActivity;
import com.app.ricktech.uis.saved_build_module.activity_saving_build.SavingBuildActivity;
import com.app.ricktech.uis.suggestions_module.activity_suggestion_buildings.SuggetionBuildingsActivity;

import java.util.List;

public class SuggestionBuildingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SuggestionModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    public SuggestionBuildingAdapter(Context context, List<SuggestionModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SuggestionBuildingRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.suggestion_building_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            if (activity instanceof SuggetionBuildingsActivity){
                SuggetionBuildingsActivity suggetionBuildingsActivity = (SuggetionBuildingsActivity) activity;

                suggetionBuildingsActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SavedBuildingsActivity){

                SavedBuildingsActivity savingBuildActivity = (SavedBuildingsActivity) activity;

                savingBuildActivity.setItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }
        });

        myHolder.binding.imageDelete.setOnClickListener(v -> {

            if (activity instanceof SuggetionBuildingsActivity){
                SuggetionBuildingsActivity suggetionBuildingsActivity = (SuggetionBuildingsActivity) activity;

                suggetionBuildingsActivity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SavedBuildingsActivity){

                SavedBuildingsActivity savingBuildActivity = (SavedBuildingsActivity) activity;
                savingBuildActivity.deleteItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));


            }
        });

        myHolder.binding.cardReset.setOnClickListener(v -> {
            if (activity instanceof SuggetionBuildingsActivity){
                SuggetionBuildingsActivity suggetionBuildingsActivity = (SuggetionBuildingsActivity) activity;
                suggetionBuildingsActivity.resetItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof SavedBuildingsActivity){

                SavedBuildingsActivity savingBuildActivity = (SavedBuildingsActivity) activity;
                savingBuildActivity.resetItemData(myHolder.getAdapterPosition(),list.get(myHolder.getAdapterPosition()));


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private SuggestionBuildingRowBinding binding;

        public MyHolder(SuggestionBuildingRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
