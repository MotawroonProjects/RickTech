package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.CategoryRowBinding;
import com.app.ricktech.databinding.GameRowBinding;
import com.app.ricktech.models.BrandModel;
import com.app.ricktech.models.SuggestionGameModel;
import com.app.ricktech.uis.gaming_laptop_module.activity_categories.CategoriesActivity;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SuggestionGameModel> list;
    private Context context;
    private LayoutInflater inflater;
    public GamesAdapter(Context context, List<SuggestionGameModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        GameRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.game_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        ComponentGameAdapter adapter = new ComponentGameAdapter(context,list.get(position).getComponents());
        myHolder.binding.recView.setLayoutManager(new LinearLayoutManager(context));
        myHolder.binding.recView.setAdapter(adapter);

        myHolder.itemView.setOnClickListener(v -> {
            if (myHolder.binding.expandedLayout.isExpanded()){
                myHolder.binding.expandedLayout.collapse(true);
                myHolder.binding.arrow.animate().setDuration(500).rotation(0).start();

            }else {
                myHolder.binding.expandedLayout.expand(true);
                myHolder.binding.arrow.animate().setDuration(500).rotation(180).start();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private GameRowBinding binding;

        public MyHolder(GameRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
