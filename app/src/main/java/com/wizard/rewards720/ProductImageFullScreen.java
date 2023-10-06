package com.wizard.rewards720;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.wizard.rewards720.Adapters.FullImageAdapter;
import com.wizard.rewards720.databinding.ActivityProductImageFullScreenBinding;

import java.util.ArrayList;

public class ProductImageFullScreen extends AppCompatActivity {
    ActivityProductImageFullScreenBinding binding;
    ArrayList<String> productItemImgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductImageFullScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent imgIntent = getIntent();
        int imgPostion = imgIntent.getIntExtra("POSITION",0);
        productItemImgList = imgIntent.getStringArrayListExtra("ImgList");
        /*productItemImgList = new ArrayList<>();

        productItemImgList.add(R.drawable.noise1);
        productItemImgList.add(R.drawable.noise2);
        productItemImgList.add(R.drawable.noise3);
        productItemImgList.add(R.drawable.noise4);
        productItemImgList.add(R.drawable.noise5);
        productItemImgList.add(R.drawable.noise6);*/

        FullImageAdapter fullImageAdapter = new FullImageAdapter(productItemImgList,ProductImageFullScreen.this);
        binding.fullImgViewpager.setAdapter(fullImageAdapter);
        binding.fullImgViewpager.setCurrentItem(imgPostion);
        binding.dot.attachTo(binding.fullImgViewpager);
    }
}