package com.wizard.rewards720;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wizard.rewards720.databinding.ActivityLeaderboardDetailBinding;


public class LeaderboardDetailActivity extends AppCompatActivity {
    ActivityLeaderboardDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderboardDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.leaderboardToolbar.customToolbar.setTitle("Full Details");
        setSupportActionBar(binding.leaderboardToolbar.customToolbar);
//        getWindow().setStatusBarColor(getResources().getColor(R.color.blue,getTheme()));
    }
}