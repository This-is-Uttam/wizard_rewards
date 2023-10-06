package com.wizard.rewards720.Adapters;

import static com.wizard.rewards720.UpiAppsActivity.UPI_APP_PACKAGE_INTENT;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Modals.PayModeModal;
import com.wizard.rewards720.PaymentActivity;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.UpiAppsRvItemBinding;

import java.util.ArrayList;

public class PayModeAdapter extends RecyclerView.Adapter<PayModeAdapter.Viewholder> {
    ArrayList<PayModeModal> payModeList;
    Context context;
    final float coins;
    final int coinPrice;
    int lastCheckedPosition = 1;
    RadioButton lastButton;

    public PayModeAdapter(ArrayList<PayModeModal> payModeList, Context context,float coins, int coinPrice) {
        this.payModeList = payModeList;
        this.context = context;
        this.coins = coins;
        this.coinPrice = coinPrice;
    }

    @NonNull
    @Override
    public PayModeAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.upi_apps_rv_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayModeAdapter.Viewholder holder, int position) {
            PayModeModal modeModal = payModeList.get(position);
            holder.binding.appIcon.setImageDrawable(modeModal.getAppicon());
            holder.binding.appName.setText(modeModal.getAppName());
            String packageName = modeModal.getAppPackageName();

            holder.binding.payModeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PaymentActivity.class);
                    intent.putExtra("COINS", coins);
                    intent.putExtra("PRICE", coinPrice);
                    intent.putExtra("APP_NAME", modeModal.getAppName());
                    intent.putExtra(UPI_APP_PACKAGE_INTENT, packageName);

                    context.startActivity(intent);
                }
            });

    }


    @Override
    public int getItemCount() {
        return payModeList.size();

    }

    public class Viewholder extends RecyclerView.ViewHolder {
        UpiAppsRvItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            binding = UpiAppsRvItemBinding.bind(itemView);
        }
    }
}
