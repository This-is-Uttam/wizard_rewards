package com.wizard.rewards720;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wizard.rewards720.databinding.ActivityPaymentHistoryBinding;

public class PaymentHistoryActivity extends AppCompatActivity {
    ActivityPaymentHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.payHisToolbar.customToolbar.setTitle("Payment History");

    }
}