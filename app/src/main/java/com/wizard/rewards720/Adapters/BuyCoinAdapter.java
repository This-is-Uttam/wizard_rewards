package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Modals.BuyCoinModal;
import com.wizard.rewards720.PaymentActivity;
import com.wizard.rewards720.R;
import com.wizard.rewards720.UpiAppsActivity;
import com.wizard.rewards720.databinding.BuyCoinItemBinding;

import java.util.ArrayList;

public class BuyCoinAdapter extends RecyclerView.Adapter<BuyCoinAdapter.Viewholder> {
    ArrayList<BuyCoinModal> buyCoinList;
    Context context;

    public BuyCoinAdapter(ArrayList<BuyCoinModal> buyCoinList, Context context) {
        this.buyCoinList = buyCoinList;
        this.context = context;
    }


    @NonNull
    @Override
    public BuyCoinAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.buy_coin_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyCoinAdapter.Viewholder holder, int position) {
        BuyCoinModal modal = buyCoinList.get(position);
        float coins = modal.getNoOfCoins();
//        float coins = 5230;

//    change 1000 int K
        if (coins > 999){
            float coinsF = (coins/1000);
            if ((long)coinsF == coinsF){
                holder.binding.noOfCoins.setText((long)coinsF+"K Coins");
            }else {
                holder.binding.noOfCoins.setText(coinsF+"K Coins");
            }
            Log.d("qqq", "onBindViewHolder: coins: "+coins +"coinsF: "+coinsF +"coinsF Long: "+(long) coinsF);
        }else {
            holder.binding.noOfCoins.setText((int) coins+" Coins");
        }

        holder.binding.buyCoinPrice.setText("₹"+modal.getBuyCoinPrice());

        holder.binding.buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpiAppsActivity.class);
                intent.putExtra("COINS", coins);
                intent.putExtra("PRICE", modal.getBuyCoinPrice());
                intent.putExtra("BUY_COIN_ID", modal.getId());

                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return buyCoinList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        BuyCoinItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = BuyCoinItemBinding.bind(itemView);
        }
    }
}
