package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Modals.LeaderBoardModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.LeaderBoardRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.Viewholder> {
    ArrayList<LeaderBoardModal> list;
    Context context;

    public LeaderBoardAdapter(ArrayList<LeaderBoardModal> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LeaderBoardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leader_board_rv_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardAdapter.Viewholder holder, int position) {
        LeaderBoardModal leaderBoardModal = list.get(position);

        if (position==0||position==1||position==2)
            holder.binding.crown.setVisibility(View.VISIBLE);
        else
            holder.binding.crown.setVisibility(View.INVISIBLE);

        holder.binding.winnerCount2.setText(String.valueOf(position+1));
        holder.binding.winnerName.setText(leaderBoardModal.getWinnerName2());
        holder.binding.refferCount2.setText(leaderBoardModal.getReferCount2()+"");

        Picasso.get()
                .load(leaderBoardModal.getWinnerImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.profileImageView);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        LeaderBoardRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = LeaderBoardRvItemBinding.bind(itemView);
        }
    }
}
