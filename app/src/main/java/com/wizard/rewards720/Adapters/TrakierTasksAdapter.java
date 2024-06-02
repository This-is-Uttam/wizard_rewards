package com.wizard.rewards720.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wizard.rewards720.Modals.TrakierTasksModal;
import com.wizard.rewards720.R;
import com.wizard.rewards720.databinding.TrakierTaskItemBinding;

import java.util.ArrayList;

public class TrakierTasksAdapter extends RecyclerView.Adapter<TrakierTasksAdapter.Viewholder> {

    ArrayList<TrakierTasksModal> trakierTasksModalsList;
    Context context;

    public TrakierTasksAdapter(ArrayList<TrakierTasksModal> trakierTasksModalsList, Context context) {
        this.trakierTasksModalsList = trakierTasksModalsList;
        this.context = context;
    }

    @NonNull
    @Override
    public TrakierTasksAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trakier_task_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrakierTasksAdapter.Viewholder holder, int position) {

        TrakierTasksModal taskModal = trakierTasksModalsList.get(position);

        holder.binding.taskCount.setText(taskModal.getTaskCount());
        holder.binding.taskName.setText(taskModal.getTaskName());
        if (taskModal.isTaskCompleted()){
            holder.binding.taskCompletion.setAlpha(1.0f);
        }else {
            holder.binding.taskCompletion.setAlpha(0.2f);
        }
    }

    @Override
    public int getItemCount() {
        return trakierTasksModalsList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TrakierTaskItemBinding binding;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = TrakierTaskItemBinding.bind(itemView);
        }
    }
}
