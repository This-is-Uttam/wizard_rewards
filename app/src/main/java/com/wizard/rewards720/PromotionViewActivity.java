package com.wizard.rewards720;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.wizard.rewards720.databinding.ActivityPromotionViewBinding;

public class PromotionViewActivity extends AppCompatActivity {
    ActivityPromotionViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPromotionViewBinding.inflate(getLayoutInflater());


        Intent intent = getIntent();
        String url = intent.getStringExtra("PROMOTION_URL");
        /*binding.promotionWebview.getSettings().setJavaScriptEnabled(true);
        binding.promotionWebview.loadUrl(url);*/
        WebView webView = new WebView(getApplicationContext());
        webView.loadUrl(url);
        setContentView(webView);
    }
}