package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Constants;
import com.wizard.rewards720.Modals.TrendingModal;
import com.wizard.rewards720.ProductItemDetailsActivity;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.TrendingDetailRvItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TrendingDetailAdapter extends RecyclerView.Adapter<TrendingDetailAdapter.Viewholder> {
    ArrayList<TrendingModal> trendItemList;
    Context context;
    public TrendingDetailAdapter(ArrayList<TrendingModal> trendItemList, Context context) {
        this.trendItemList = trendItemList;
        this.context = context;


    }

    @NonNull
    @Override
    public TrendingDetailAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_detail_rv_item, parent,false);
        return new TrendingDetailAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingDetailAdapter.Viewholder holder, int position) {
        TrendingModal trendingModal = trendItemList.get(position);

//        days Left in current month

        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DATE);


//        Calendar calendar1 = new GregorianCalendar(
//                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        Calendar calendar1 = new GregorianCalendar(
                Calendar.YEAR,Calendar.MONTH,Calendar.DATE);
        int daysInMonth = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);

        int daysLeft = daysInMonth - today;

        holder.binding.daysLeftText.setText("Likely to close in " +daysLeft+ " days");
        holder.binding.spotLeftText.setText(trendingModal.getSpotLeftText());
        holder.binding.trendItemName.setText(trendingModal.getTrendItemName());
        holder.binding.trendItemPrice.setText(trendingModal.getPricePerSpot()+"/spot");
        String image = Constants.PRODUCT_IMG_URL+ trendingModal.getTrendItemImg();
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.trendItemImg);
        int progressValue = Integer.parseInt(holder.binding.spotLeftText.getText().toString());

        holder.binding.progressBar.setMax(Integer.parseInt(trendingModal.getTotalSpotText()));
        holder.binding.progressBar.setProgress(progressValue);

        holder.binding.trendingDetailItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductItemDetailsActivity.class);
                intent.putExtra("TrendItemId",trendingModal.getTrendItemId());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return trendItemList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        TrendingDetailRvItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            binding = TrendingDetailRvItemBinding.bind(itemView);
        }
    }
}
