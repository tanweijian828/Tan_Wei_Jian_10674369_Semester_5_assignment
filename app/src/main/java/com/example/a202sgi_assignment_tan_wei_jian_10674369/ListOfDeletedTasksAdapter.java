package com.example.a202sgi_assignment_tan_wei_jian_10674369;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class ListOfDeletedTasksAdapter extends RecyclerView.Adapter<com.example.a202sgi_assignment_tan_wei_jian_10674369.ListOfDeletedTasksAdapter.DeletedTaskViewHolder> {

    class DeletedTaskViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvDeleteTitle;
        public final TextView tvDeleteDescription;
        ImageButton recoverBtn;
        ImageButton permanentDeleteBtn;
        ImageView priorityImage;
        ImageView statusImage;

        public DeletedTaskViewHolder(View itemView) {
            super(itemView);
            tvDeleteTitle = (TextView)itemView.findViewById(R.id.tvDeleteTitle);
            tvDeleteDescription = (TextView)itemView.findViewById(R.id.tvDeleteDescription);
            recoverBtn = (ImageButton)itemView.findViewById(R.id.btnRecover);
            permanentDeleteBtn = (ImageButton)itemView.findViewById(R.id.btnDeletePermanent);
            priorityImage = (ImageView)itemView.findViewById(R.id.priorityImage);
            statusImage = (ImageView)itemView.findViewById(R.id.statusImage);


        }
    }
    private final LayoutInflater mInflater;
    Context mContext;
    TaskListOpenHelper mDB;

    public ListOfDeletedTasksAdapter(Context context, TaskListOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public DeletedTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.del_tasklist_item, parent, false);
        return new DeletedTaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeletedTaskViewHolder holder, int position) {
        TaskList current = mDB.del_query(position);
        holder.priorityImage.setImageDrawable(null);
        if(current.getTaskPriority().equalsIgnoreCase("High")){
            holder.priorityImage.setImageResource(R.drawable.red);
        }
        else if(current.getTaskPriority().equalsIgnoreCase("Medium")){
            holder.priorityImage.setImageResource(R.drawable.orange);
        }
        else{
            holder.priorityImage.setImageResource(R.drawable.green);
        }

        holder.statusImage.setImageDrawable(null);
        if(current.getTaskStatus() == 1){
            holder.statusImage.setImageResource(R.drawable.done);
        }
        else if(current.getTaskStatus() == 0){
            holder.statusImage.setImageResource(R.drawable.in_progress);
        }
        else{
            holder.statusImage.setImageResource(R.drawable.deadline);
        }

        String tempTitle = current.getTitleName();
        String tempDescription = current.getTaskDescription();
        if(tempTitle.length() > 15){
            tempTitle = tempTitle.substring(0,15);
            tempTitle = tempTitle + "...";
            holder.tvDeleteTitle.setText(tempTitle);
        }
        else{
            holder.tvDeleteTitle.setText(tempTitle);
        }
        if(tempDescription.length() > 15){
            tempDescription = tempDescription.substring(0,15);
            tempDescription = tempDescription + "...";
            holder.tvDeleteDescription.setText(tempDescription);
        }
        else{
            holder.tvDeleteDescription.setText(tempDescription);
        }

        final DeletedTaskViewHolder h = holder;

        holder.recoverBtn.setOnClickListener(
                new com.example.a202sgi_assignment_tan_wei_jian_10674369.btnOnClickListener(current.getTaskId(),null,null,null,null,null,null,null,0){
                    public void onClick(View v){
                        mDB.del_insert(id);
                        int deleted = mDB.delete_forever(id);
                        if(deleted > 0){
                            notifyItemRemoved(h.getAdapterPosition());
                        }
                        Toast.makeText(mContext, "Task is successfully recovered.", Toast.LENGTH_SHORT).show();
                    }
                });



        holder.permanentDeleteBtn.setOnClickListener(
                new com.example.a202sgi_assignment_tan_wei_jian_10674369.btnOnClickListener(current.getTaskId(),null,null,null,null,null,null,null,0){
                    public void onClick(View v){
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Task Delete last Warning!!");
                        alert.setMessage("Permanently delete this task? You may lost important information.");

                        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int deleted = mDB.delete_forever(id);
                                if(deleted > 0){
                                    notifyItemRemoved(h.getAdapterPosition());
                                }
                                Toast.makeText(mContext, "Task is successfully permanent removed from Task Management.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, "Task remove action has been cancelled.Task had successfully recovered to Task Management.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alert.setCancelable(false);
                        alert.show();
                    }
                });
    }

    @Override
    public int getItemCount() {

        return (int)mDB.delete_count();

    }
}


