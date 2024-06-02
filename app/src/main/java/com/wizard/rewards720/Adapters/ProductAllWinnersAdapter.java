package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Constants;
import com.wizard.rewards720.Modals.ProductWinModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.WinnerRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAllWinnersAdapter extends RecyclerView.Adapter<ProductAllWinnersAdapter.Viewholder> {
    ArrayList<ProductWinModal> productWinList;
    Context context;
    boolean showAll;

    public ProductAllWinnersAdapter(ArrayList<ProductWinModal> productWinList, Context context, boolean showAll) {
        this.productWinList = productWinList;
        this.context = context;
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public ProductAllWinnersAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.winner_rv_item, parent, false);
        return new ProductAllWinnersAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAllWinnersAdapter.Viewholder holder, int position) {
        ProductWinModal modal = productWinList.get(position);

        holder.binding.winnerCount.setText("#" + modal.getProductWinnerCount());

        holder.binding.prodWinnerName.setText(modal.getUserName());
        holder.binding.prodWinningPrice.setText(modal.getProductName());
        holder.binding.prodWinPriceValue.setText("₹" + modal.getMrp());
        holder.binding.winMonth.setText(modal.getWinMonth());

        String prodImg = Constants.PRODUCT_IMG_URL + modal.getProductImage();
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
     /*   if (productWinList.size() == 0){


        }else {
            return 1;
        }
        return 0;*/

        if (!showAll)
            return 1;
        else
            return productWinList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        WinnerRvItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = WinnerRvItemBinding.bind(itemView);
        }
    }
}
