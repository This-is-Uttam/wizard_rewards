package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.ButtonClickListener;
import com.wizard.rewards720.Modals.WatchVidModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.WatchVidRvItemBinding;

import java.util.ArrayList;

public class WatchVidAdapter extends RecyclerView.Adapter<WatchVidAdapter.Viewholder> {
    ArrayList<WatchVidModal> watchVidList;
    Context context;

    ButtonClickListener buttonClickListener;

    public WatchVidAdapter(ArrayList<WatchVidModal> watchVidList, Context context, ButtonClickListener buttonClickListener) {
        this.watchVidList = watchVidList;
        this.context = context;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public WatchVidAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.watch_vid_rv_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchVidAdapter.Viewholder holder, int position) {
        WatchVidModal modal = watchVidList.get(position);
        holder.binding.watchVidBtn.setText(modal.getBtnTitle());
        Log.d("WatchVidAdapter", "onBindViewHolder: button enable: "+modal.isButtonEnable());
        if (modal.isButtonEnable()){
            holder.binding.watchVidBtn.setEnabled(true);
            holder.binding.watchVidBtn.setBackgroundTintList(context.getColorStateList(R.color.TertiaryColor));
            holder.binding.watchVidBtn.setTextColor(context.getResources().getColor(R.color.white, context.getTheme()));
        }else {
            holder.binding.watchVidBtn.setEnabled(false);
            holder.binding.watchVidBtn.setBackgroundTintList(context.getColorStateList(R.color.buttonDisableColor));
            holder.binding.watchVidBtn.setTextColor(context.getResources().getColor(R.color.whiteOnly, context.getTheme()));
        }

        holder.binding.watchVidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonClickListener.onButtonClick(modal.getId());

            }
        });

    }

    @Override
    public int getItemCount() {
        return watchVidList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        WatchVidRvItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = WatchVidRvItemBinding.bind(itemView);
        }
    }
}
