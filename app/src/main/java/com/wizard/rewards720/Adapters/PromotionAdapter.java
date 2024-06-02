package com.wizard.rewards720.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Constants;
import com.wizard.rewards720.Modals.PromotionModal;
import com.wizard.rewards720.PromotionViewActivity;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.PromotionRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.Viewholder> {
    ArrayList<PromotionModal> promotionList;
    Context context;
    boolean centerCrop = true;


    public PromotionAdapter(ArrayList<PromotionModal> promotionList, Context context) {
        this.promotionList = promotionList;
        this.context = context;


    }


    @NonNull
    @Override
    public PromotionAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.promotion_rv_item,parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionAdapter.Viewholder holder, int position) {
        PromotionModal promotionModal = promotionList.get(position);

        Picasso.get()
                .load(promotionModal.getPromotionImg())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.promotionImg);

        holder.binding.promotionBannerUrl.setText(promotionModal.getPromotionImgLink());

        holder.binding.promotionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(context, PromotionViewActivity.class);
                intent.putExtra("PROMOTION_URL", promotionModal.getPromotionImgLink());
                context.startActivity(intent);*/

                Intent bannerIntent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(promotionModal.getPromotionImgLink());
                bannerIntent.setData(uri);
                context.startActivity(bannerIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        PromotionRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
//            promotionImg = itemView.findViewById(R.id.promotionImg);
            binding = PromotionRvItemBinding.bind(itemView);
        }

    }
}
