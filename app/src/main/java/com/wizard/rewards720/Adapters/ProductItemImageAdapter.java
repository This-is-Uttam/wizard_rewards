package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.wizard.rewards720.Constants;
import com.wizard.rewards720.ProductImageFullScreen;
import com.wizard.rewards720.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductItemImageAdapter extends PagerAdapter {
    ArrayList<String> imgList;
    Context context;
    View view;

    public ProductItemImageAdapter(ArrayList<String> imgList, Context context) {
        this.imgList = imgList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       view = LayoutInflater.from(context).inflate(R.layout.product_img_layout_item,container,false);

        ImageView productItemImg = view.findViewById(R.id.productItemImg);
        String productImg = Constants.PRODUCT_IMG_URL +imgList.get(position);

        Picasso.get()
                .load(productImg)
                .placeholder(R.drawable.placeholder)
                .into(productItemImg);

        container.addView(view);

        productItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imgIntent = new Intent(context, ProductImageFullScreen.class);
                imgIntent.putExtra("POSITION",position);
                imgIntent.putExtra("ImgList", imgList);
                context.startActivity(imgIntent);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }
}
