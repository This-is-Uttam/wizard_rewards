package com.wizard.rewards720;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wizard.rewards720.databinding.ActivityWinnerBinding;
import com.squareup.picasso.Picasso;

public class WinnerActivity extends AppCompatActivity {
    ActivityWinnerBinding binding;
    TextView winnerDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityWinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent_blue,getTheme()));

        winnerDesc = findViewById(R.id.winnerDescription2);
        Intent intent = getIntent();
        String winnerFirstLetter = intent.getStringExtra("WINNER_NAME");
        String winnerDescription = intent.getStringExtra("WINNER_DESC");
        int winnerProductImg = intent.getIntExtra("WINNER_PRODUCT_IMG",R.drawable.placeholder);

        Picasso.get()
                .load(winnerProductImg)
                .placeholder(R.drawable.placeholder)
                .into(binding.winnerProductImg2);
        binding.winnerFirstLetter2.setText(winnerFirstLetter);
        binding.winnerDescription2.setText(winnerDescription);

        binding.animationView.setAnimation(R.raw.winner_animation);
        binding.animationView.playAnimation();


    }
}