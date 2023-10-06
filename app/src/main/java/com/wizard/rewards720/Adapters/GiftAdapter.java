package com.wizard.rewards720.Adapters;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.wizard.rewards720.Modals.GiftModal;
import com.wizard.rewards720.R;

import java.util.ArrayList;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.Viewholder> {

    ArrayList<GiftModal> giftList;
    Context context;

    public GiftAdapter(ArrayList<GiftModal> giftList, Context context) {
        this.giftList = giftList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.gift_rv_item, parent,false);
//        return new Viewholder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        GiftModal giftModal = giftList.get(position);

        /*holder.giftCoins.setText(giftModal.getGiftCoins());
        holder.giftRuppees.setText(giftModal.getGiftRuppees());

        Picasso.get()
                .load(giftModal.getGiftImg())
                .placeholder(R.drawable.placeholder)
                .into(holder.giftImg);*/

    }

    @Override
    public int getItemCount() {
        return giftList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView giftCoins, giftRuppees;
        ImageView giftImg;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            /*giftCoins = itemView.findViewById(R.id.giftCoins);
            giftImg = itemView.findViewById(R.id.giftImg);
            giftRuppees = itemView.findViewById(R.id.giftRuppees);*/


        }
    }
}
