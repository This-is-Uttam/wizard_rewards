package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Modals.PaymentHistoryModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.PaymentHisRvItemBinding;

import java.util.ArrayList;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.Viewholder> {
    ArrayList<PaymentHistoryModal> paymentHistoryList;
    Context context;

    public PaymentHistoryAdapter(ArrayList<PaymentHistoryModal> paymentHistoryList, Context context) {
        this.paymentHistoryList = paymentHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentHistoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_his_rv_item,parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistoryAdapter.Viewholder holder, int position) {
        PaymentHistoryModal modal = paymentHistoryList.get(position);

        holder.binding.purchaseAmt.setText("₹" +modal.getPurchaseAmt());
        holder.binding.purchaseCoin.setText(modal.getPurchaseCoin());
        holder.binding.purchaseDateTime.setText(modal.getPurchaseDateTime());
        holder.binding.purchaseTxnId.setText(modal.getPurchaseTxnId());

        // Payment Status
        int paymentStatus = modal.getPurchaseStatus();

        if (paymentStatus == 0) {
            // Payment Initiated

            holder.binding.payStatus.setText("PAYMENT INITIATED");
            holder.binding.payStatus.setTextColor(context.getResources().getColor(R.color.orange_dull, context.getTheme()));

        } else if (paymentStatus == 1) {
            // Payment Success
            holder.binding.payStatus.setText("PAYMENT SUCCESS");
            holder.binding.payStatus.setTextColor(context.getResources().getColor(R.color.green, context.getTheme()));
        } else if (paymentStatus == 2) {
            // Payment Pending or Fail
            holder.binding.payStatus.setText("PAYMENT PENDING OR FAILED");
            holder.binding.payStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark, context.getTheme()));
        }

    }

    @Override
    public int getItemCount() {
        return paymentHistoryList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        PaymentHisRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = PaymentHisRvItemBinding.bind(itemView);
        }
    }
}
