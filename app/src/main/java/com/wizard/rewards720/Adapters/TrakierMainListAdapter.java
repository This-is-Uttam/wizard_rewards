package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wizard.rewards720.Modals.TrakierMainListModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.TrakierCampaignDetailActivity;
import com.wizard.rewards720.databinding.TrakierMainListItemBinding;

import java.util.ArrayList;

public class TrakierMainListAdapter extends RecyclerView.Adapter<TrakierMainListAdapter.Viewholder> {

    ArrayList<TrakierMainListModal> trakierMainListModals;
    Context context;

    public TrakierMainListAdapter(ArrayList<TrakierMainListModal> trakierMainListModals, Context context) {
        this.trakierMainListModals = trakierMainListModals;
        this.context = context;
    }

    @NonNull
    @Override
    public TrakierMainListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trakier_main_list_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrakierMainListAdapter.Viewholder holder, int position) {
        TrakierMainListModal mainListModal = trakierMainListModals.get(position);

//        mainListModal.setAdId(mainListModal.getAdId());
        holder.binding.adTitle.setText(mainListModal.getAdTitle());
        holder.binding.adDesc.setText(mainListModal.getAdDesc());
        holder.binding.adRewardCoin.setText(mainListModal.getAdRewardCoin());
        Picasso.get()
                .load(mainListModal.getAdIcon())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.adIcon);
        Picasso.get()
                .load(mainListModal.getAdPosterImg())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.adPosterImg);

        holder.binding.campaignCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trakierDetailIntent = new Intent(context, TrakierCampaignDetailActivity.class);
                trakierDetailIntent.putExtra("campaignId", mainListModal.getAdId());

                context.startActivity(trakierDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trakierMainListModals.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TrakierMainListItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = TrakierMainListItemBinding.bind(itemView);
        }
    }
}
