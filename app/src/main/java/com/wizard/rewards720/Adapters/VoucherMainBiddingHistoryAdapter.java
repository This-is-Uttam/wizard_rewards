package com.wizard.rewards720.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Constants;
import com.wizard.rewards720.Modals.BiddingHistoryModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.VoucherBidHistoryRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VoucherMainBiddingHistoryAdapter extends RecyclerView.Adapter<VoucherMainBiddingHistoryAdapter.Viewholder> {
    ArrayList<BiddingHistoryModal> biddingHistoryList;
    Context context;

    public VoucherMainBiddingHistoryAdapter(ArrayList<BiddingHistoryModal> biddingHistoryList, Context context) {
        this.biddingHistoryList = biddingHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public VoucherMainBiddingHistoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.voucher_bid_history_rv_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherMainBiddingHistoryAdapter.Viewholder holder, int position) {
        BiddingHistoryModal modal = biddingHistoryList.get(position);

        holder.binding.bidProductName.setText(modal.getBidProductName());
        holder.binding.bidCoin.setText(modal.getBidCoin());
        holder.binding.bidDiamond.setText(modal.getBidDiamond());
        holder.binding.bidDate.setText(modal.getBidDate());
        holder.binding.bidTime.setText(modal.getBidTime());
        Log.d("hell", "onBindViewHolder: winner: "+ modal.getWinner()+" code : "+ modal.getVoucherCode());
        holder.binding.bidWinningStatus.setBackground(context.getDrawable(R.drawable.blue_round_corner));

        if (modal.getWinner()==1 && modal.getVoucherCode()!="null"){
            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.white, context.getTheme()));
            holder.binding.bidWinningStatus.setBackgroundTintList(context.getColorStateList(R.color.green));
            holder.binding.bidWinningStatus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.binding.bidWinningStatus.setTypeface(Typeface.DEFAULT_BOLD);
//            holder.binding.constraintLayout6.setPadding(0,0,0,0);

//            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.winnerGreen, context.getTheme()));
            holder.binding.voucherCode.setVisibility(View.VISIBLE);
            holder.binding.voucherCode.setText(modal.getVoucherCode());
            holder.binding.vCodeTitle.setVisibility(View.VISIBLE);
            holder.binding.copyCode.setVisibility(View.VISIBLE);

        }else if (modal.getWinner()==0 && modal.getVoucherCode()=="null"){
            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.green, context.getTheme()));
            holder.binding.bidWinningStatus.setBackgroundColor(context.getResources().getColor(R.color.bid_his_item_color, context.getTheme()));
            holder.binding.bidWinningStatus.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
            holder.binding.bidWinningStatus.setTypeface(Typeface.DEFAULT_BOLD);

//            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.black, context.getTheme()));
//            holder.binding.voucherCode.setText(modal.getVoucherCode());
            holder.binding.voucherCode.setVisibility(View.GONE);
            holder.binding.vCodeTitle.setVisibility(View.GONE);
            holder.binding.copyCode.setVisibility(View.GONE);
        }else {
            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.black, context.getTheme()));
            holder.binding.bidWinningStatus.setBackgroundColor(context.getResources().getColor(R.color.bid_his_item_color, context.getTheme()));
            holder.binding.bidWinningStatus.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
            holder.binding.bidWinningStatus.setTypeface(Typeface.DEFAULT_BOLD);
            holder.binding.voucherCode.setVisibility(View.GONE);
            holder.binding.vCodeTitle.setVisibility(View.GONE);
            holder.binding.copyCode.setVisibility(View.GONE);
        }
        holder.binding.bidWinningStatus.setText(modal.getBidWinningStatus());

        String prodImg = Constants.VOUCHER_IMG_URL + modal.getBidProductImage();
        Picasso.get()
                .load(prodImg)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.bidProductImage);

        holder.binding.copyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Voucher code", holder.binding.voucherCode.getText().toString());
                manager.setPrimaryClip(clipData);
                Toast.makeText(context, "Voucher Code Copied", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return biddingHistoryList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        VoucherBidHistoryRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = VoucherBidHistoryRvItemBinding.bind(itemView);
        }
    }
}
