package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.wizard.rewards720.Constants;
import com.wizard.rewards720.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FullImageAdapter extends PagerAdapter {

    ArrayList<String> imageList;
    Context context;
    View view;

    public FullImageAdapter(ArrayList<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        view = LayoutInflater.from(context).inflate(R.layout.full_image_layout_item,container,false);
        String image = Constants.PRODUCT_IMG_URL +imageList.get(position);
        ImageView imageView = view.findViewById(R.id.photo_view);

        Picasso.get()
                .load(image)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }
}
