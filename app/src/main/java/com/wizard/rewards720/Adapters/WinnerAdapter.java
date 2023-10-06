package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Modals.WinnerModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.WinnerActivity;
import com.wizard.rewards720.databinding.WinnerRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WinnerAdapter extends RecyclerView.Adapter<WinnerAdapter.ViewHolder> {
    ArrayList<WinnerModal> winnerList;
    Context context;

    public WinnerAdapter(ArrayList<WinnerModal> winnerList, Context context) {
        this.winnerList = winnerList;
        this.context = context;
    }

    @NonNull
    @Override
    public WinnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.winner_rv_item,parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WinnerAdapter.ViewHolder holder, int position) {
        WinnerModal winnerModal = winnerList.get(position);

        holder.binding.winnerCount.setText(winnerModal.getWinnerCount());
//        holder.binding.winnerDescription.setText(winnerModal.getWinnerDescription());
        Picasso.get()
                .load(winnerModal.getWinnerProductImg())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.winnerProductImg);
//        if (holder.binding.winnerImg !=""){
//            holder.binding.winnerFirstLetter.setVisibility(View.INVISIBLE);
//
//            Picasso.get()
//                    .load(winnerModal.getWinnerImg())
//                    .placeholder(R.drawable.placeholder)
//                    .into(holder.binding.winnerImg);
//        }else {
            holder.binding.winnerImg.setColorFilter(context.getResources().getColor(R.color.blue, context.getTheme()));
            holder.binding.winnerCLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent winnerIntent =new Intent(context, WinnerActivity.class);
                    winnerIntent.putExtra("WINNER_NAME",winnerModal.getWinnerFirstLetter());
                    winnerIntent.putExtra("WINNER_DESC",winnerModal.getWinnerDescription());
                        winnerIntent.putExtra("WINNER_PRODUCT_IMG",winnerModal.getWinnerProductImg());
                    context.startActivity(winnerIntent);
                }
            });
//        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WinnerRvItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = WinnerRvItemBinding.bind(itemView);
        }
    }
}
