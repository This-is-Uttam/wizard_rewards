package com.wizard.rewards720;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wizard.rewards720.Adapters.WinnersViewpager;
import com.wizard.rewards720.databinding.ActivityWinnerDetailBinding;

public class WinnerDetailActivity extends AppCompatActivity {
    ActivityWinnerDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWinnerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        getWindow().setStatusBarColor(getResources().getColor(R.color.blue,getTheme()));
        binding.winnerDetailToolbar.setTitle("The Winners");
//        binding.winnerDetailToolbar.setTitleTextColor(getResources().getColor(R.color.black,getTheme()));



        binding.winnerViewpager.setAdapter(new WinnersViewpager(getSupportFragmentManager()));
        binding.winnerTabLayout.setupWithViewPager(binding.winnerViewpager);

    }
}