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
import com.wizard.rewards720.databinding.VoucherDetailRvItemBinding;
import com.wizard.rewards720.databinding.VoucherMainRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VoucherDetailAdapter extends RecyclerView.Adapter<VoucherDetailAdapter.Viewholder> {
    ArrayList<VoucherMainModal> voucherList;
    Context context;

    public VoucherDetailAdapter(ArrayList<VoucherMainModal> voucherList, Context context) {
        this.voucherList = voucherList;
        this.context = context;
    }

    @NonNull
    @Override
    public VoucherDetailAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.voucher_main_rv_item, parent, false);
        return new VoucherDetailAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherDetailAdapter.Viewholder holder, int position) {
        VoucherMainModal voucherMainModal = voucherList.get(position);

        holder.binding.voucherName.setText(voucherMainModal.getVouMainItemName());
        holder.binding.voucherAmt.setText("₹ "+voucherMainModal.getMrp());
        holder.binding.voucherCoins.setText(voucherMainModal.getVouPricePerSpot());
//        Item left
        holder.binding.progressBar.setMax(Integer.parseInt(voucherMainModal.getTotalSpotText()));
        holder.binding.progressBar.setProgress(Integer.parseInt(voucherMainModal.getSpotLeftText()));
        holder.binding.itemLeftText.setText(voucherMainModal.getSpotLeftText());

        String img = Constants.VOUCHER_IMG_URL+ voucherMainModal.getVouMainItemImg();
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.voucherImg);

        holder.binding.voucherCnstrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VoucherMainDetailActivity.class);
                intent.putExtra("VOUCHER_MAIN_ID", voucherMainModal.getVouMainItemId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        VoucherMainRvItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = VoucherMainRvItemBinding.bind(itemView);

        }
    }
}
