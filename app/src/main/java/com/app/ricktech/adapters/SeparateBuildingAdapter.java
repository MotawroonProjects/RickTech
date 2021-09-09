package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.SeparateBuildingRowBinding;
import com.app.ricktech.databinding.SuggestionBuildingRowBinding;
import com.app.ricktech.models.SuggestionModel;
import com.app.ricktech.uis.saved_build_module.activity_saved_buldings.SavedBuildingsActivity;
import com.app.ricktech.uis.suggestions_module.activity_suggestion_buildings.SuggetionBuildingsActivity;

import java.util.List;

public class SeparateBuildingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SuggestionModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    public SeparateBuildingAdapter(Context context, List<SuggestionModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SeparateBuildingRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.separate_building_row, parent, false);
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
        private SeparateBuildingRowBinding binding;

        public MyHolder(SeparateBuildingRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
