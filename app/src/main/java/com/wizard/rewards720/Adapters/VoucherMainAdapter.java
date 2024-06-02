package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Constants;
import com.wizard.rewards720.Modals.VoucherMainModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.VoucherMainDetailActivity;
import com.wizard.rewards720.databinding.VoucherMainRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VoucherMainAdapter extends RecyclerView.Adapter<VoucherMainAdapter.Viewholder> {
    ArrayList<VoucherMainModal> voucherMainList;
    Context context;

    public VoucherMainAdapter(ArrayList<VoucherMainModal> voucherMainList, Context context) {
        this.voucherMainList = voucherMainList;
        this.context = context;
    }

    @NonNull
    @Override
    public VoucherMainAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.voucher_main_rv_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherMainAdapter.Viewholder holder, int position) {
        VoucherMainModal modal = voucherMainList.get(position);

        holder.binding.voucherName.setText(modal.getVouMainItemName());
        holder.binding.voucherCoins.setText(modal.getVouPricePerSpot()+"");
        holder.binding.voucherAmt.setText("₹" + modal.getMrp());
        holder.binding.itemLeftText.setText(modal.getSpotLeftText());
        holder.binding.progressBar.setMax(Integer.parseInt(modal.getTotalSpotText()));
        holder.binding.progressBar.setProgress(Integer.parseInt(modal.getSpotLeftText()));

        String img = Constants.VOUCHER_IMG_URL + modal.getVouMainItemImg();
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.voucherImg);

        holder.binding.voucherCnstrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VoucherMainDetailActivity.class);
                intent.putExtra("VOUCHER_MAIN_ID", modal.getVouMainItemId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (voucherMainList.size() <= 5)
            return voucherMainList.size();
        else
            return 5;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        VoucherMainRvItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            binding = VoucherMainRvItemBinding.bind(itemView);
        }
    }
}
