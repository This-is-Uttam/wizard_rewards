package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Constants;
import com.wizard.rewards720.Modals.VoucherWinModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.WinnerRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VoucherAllWinnersAdapter extends RecyclerView.Adapter<VoucherAllWinnersAdapter.Viewholder> {
    ArrayList<VoucherWinModal> voucherWinList;
    Context context;
    boolean showAll;

    public VoucherAllWinnersAdapter(ArrayList<VoucherWinModal> voucherWinList, Context context, boolean showAll) {
        this.voucherWinList = voucherWinList;
        this.context = context;
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public VoucherAllWinnersAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.winner_rv_item, parent, false);
        return new VoucherAllWinnersAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAllWinnersAdapter.Viewholder holder, int position) {
        VoucherWinModal modal = voucherWinList.get(position);

        holder.binding.winnerCount.setText("#" + modal.getVoucherWinnerCount());
        holder.binding.prodWinnerName.setText(modal.getUserName());
        holder.binding.prodWinningPrice.setText(modal.getVoucherName());
        holder.binding.prodWinPriceValue.setText("₹"+modal.getMrp());

        String prodImg = Constants.VOUCHER_IMG_URL + modal.getVoucherImage();
        Picasso.get()
                .load(prodImg)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.winnerProductImg);
        Picasso.get()
                .load(modal.getUserImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.winnerImg);

    }

    @Override
    public int getItemCount() {
        /////// Changes......
      /*  if (voucherWinList.size() == 0){

        }else {
            return 1;
        }
        return 0;*/

        if (!showAll)
            return 1;
        else
            return voucherWinList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        WinnerRvItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = WinnerRvItemBinding.bind(itemView);
        }
    }
}
