package com.app.ricktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ricktech.R;
import com.app.ricktech.databinding.DetialsRowBinding;
import com.app.ricktech.databinding.LabtopRowBinding;
import com.app.ricktech.models.ComponentModel;

import java.util.List;

public class DetialsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ComponentModel> list;
    private Context context;
    private LayoutInflater inflater;
    public DetialsAdapter(Context context, List<ComponentModel> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DetialsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.detials_row, parent, false);
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
        private DetialsRowBinding binding;

        public MyHolder(DetialsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
