package com.app.ricktech.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.ricktech.R;
import com.app.ricktech.models.SliderModel;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.general_module.activity_home.fragments.Fragment_Home;
import com.squareup.picasso.Picasso;


import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private List<SliderModel.Data> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Home fragment_home;
    public SliderAdapter(List<SliderModel.Data> list, Context context,Fragment_Home fragment_home) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment_home = fragment_home;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.slider_row, container, false);
        ImageView imageView = view.findViewById(R.id.image);
        String url = Tags.IMAGE_URL + list.get(position).getImage();
        Picasso.get().load(Uri.parse(url)).fit().into(imageView);
        view.setOnClickListener(v -> {
            fragment_home.setSliderItemData(list.get(position).getProduct());
        });
        container.addView(view);

        return view;

    }

    @Override
    public boolean isViewFromObject(@NonNull  View view, @NonNull  Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull  ViewGroup container, int position, @NonNull  Object object) {
        container.removeView((View) object);
    }
}
