package com.wizard.rewards720.Adapters;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.wizard.rewards720.Constants;
import com.wizard.rewards720.Modals.RedeemModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.RedeemViewActivity;
import com.wizard.rewards720.databinding.GiftRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RedeemAdapter extends RecyclerView.Adapter<RedeemAdapter.Viewholder> {

    ArrayList<RedeemModal> giftList;
    Context context;

    public RedeemAdapter(ArrayList<RedeemModal> giftList, Context context) {
        this.giftList = giftList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gift_rv_item, parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        RedeemModal redeemModal = giftList.get(position);

        holder.binding.redeemName.setText(redeemModal.getRedeemName());
        holder.binding.redeemCoins.setText(redeemModal.getRedeemCoins());
        holder.binding.redeemAmt.setText(redeemModal.getVoucherSymbol() + redeemModal.getRedeemAmt());

        String img = Constants.REDEEM_IMG_URL+ redeemModal.getRedeemImg();
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.redeemImg);

        holder.binding.voucherCnstrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RedeemViewActivity.class);
                intent.putExtra("REDEEM_ITEM_ID", redeemModal.getRedeemId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        GiftRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = GiftRvItemBinding.bind(itemView);


        }
    }
}
