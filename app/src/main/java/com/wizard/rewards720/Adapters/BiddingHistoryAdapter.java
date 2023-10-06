package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.ClaimUserDetailActivity;
import com.wizard.rewards720.Constants;
import com.wizard.rewards720.Modals.BiddingHistoryModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.BidHistoryRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BiddingHistoryAdapter extends RecyclerView.Adapter<BiddingHistoryAdapter.Viewholder> {
    ArrayList<BiddingHistoryModal> biddingHistoryList;
    Context context;

    public BiddingHistoryAdapter(ArrayList<BiddingHistoryModal> biddingHistoryList, Context context) {
        this.biddingHistoryList = biddingHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public BiddingHistoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bid_history_rv_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BiddingHistoryAdapter.Viewholder holder, int position) {
        BiddingHistoryModal modal = biddingHistoryList.get(position);

        holder.binding.bidProductName.setText(modal.getBidProductName());
        holder.binding.bidCoin.setText(modal.getBidCoin());
        holder.binding.bidDiamond.setText(modal.getBidDiamond());
        holder.binding.bidDate.setText(modal.getBidDate());
        holder.binding.bidTime.setText(modal.getBidTime());
        holder.binding.bidWinningStatus.setText(modal.getBidWinningStatus());
        holder.binding.bidWinningStatus.setBackground(context.getDrawable(R.drawable.blue_round_corner));

        if (modal.getWinner()==1){
            if (!modal.getWinnerAddress().equals("null")){
                holder.binding.claimText.setText("Your details are submitted, wait for us to contact you.");
                holder.binding.claimBtn.setVisibility(View.GONE);
            }else {
                holder.binding.claimText.setText("Claim product by providing us some details.");
                holder.binding.claimBtn.setVisibility(View.VISIBLE);
            }
            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.white, context.getTheme()));
            holder.binding.bidWinningStatus.setBackgroundTintList(context.getColorStateList(R.color.green));
            holder.binding.bidWinningStatus.setGravity(View.TEXT_ALIGNMENT_CENTER);
            holder.binding.constraintLayout6.setPadding(0,0,0,0);
//            claim view
            holder.binding.constrntLayoutClaim.setVisibility(View.VISIBLE);
        }else if (modal.getWinner()==2) {
            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.green, context.getTheme()));
            holder.binding.bidWinningStatus.setBackgroundTintList(context.getColorStateList(R.color.bid_his_item_color));
            holder.binding.bidWinningStatus.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
            holder.binding.constrntLayoutClaim.setVisibility(View.GONE);
        } else {
            holder.binding.bidWinningStatus.setTextColor(context.getResources().getColor(R.color.black, context.getTheme()));
            holder.binding.bidWinningStatus.setBackgroundTintList(context.getColorStateList(R.color.bid_his_item_color));
            holder.binding.bidWinningStatus.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
            holder.binding.constrntLayoutClaim.setVisibility(View.GONE);
        }

        String prodImg = Constants.PRODUCT_IMG_URL + modal.getBidProductImage();
        Picasso.get()
                .load(prodImg)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.bidProductImage);
        
        holder.binding.claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClaimUserDetailActivity.class);
                intent.putExtra("BID_ID", modal.getBidId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return biddingHistoryList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        BidHistoryRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = BidHistoryRvItemBinding.bind(itemView);
        }
    }
}
