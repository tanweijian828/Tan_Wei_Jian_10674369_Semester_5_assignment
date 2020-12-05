package com.example.a202sgi_assignment_tan_wei_jian_10674369;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


public class TaskListAdapter extends
        RecyclerView.Adapter<com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskListAdapter.TaskViewHolder> {



    class TaskViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvTitle;
        public final TextView tvDescription;
        ImageButton btnEdit;
        ImageButton btnDelete;
        ImageView imagePriority;
        ImageView imageStatus;

        public TaskViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitleName);
            tvDescription = (TextView)itemView.findViewById(R.id.tvDescription);
            btnEdit = (ImageButton)itemView.findViewById(R.id.btnEdit);
            btnDelete = (ImageButton)itemView.findViewById(R.id.btnDelete);
            imagePriority = (ImageView)itemView.findViewById(R.id.imagePriority);
            imageStatus = (ImageView)itemView.findViewById(R.id.imageStatus);


        }
    }

    private static final String TAG = com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskListAdapter.class.getSimpleName();

    public static final String EXTRA_ID = "com.example.a202sgi_assignment_tan_wei_jian_10674369.ID";
    public static final String EXTRA_TITLE = "com.example.a202sgi_assignment_tan_wei_jian_10674369.TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.a202sgi_assignment_tan_wei_jian_10674369.DESCRIPTION";
    public static final String EXTRA_STARTOFDATE = "com.example.a202sgi_assignment_tan_wei_jian_10674369.STARTOFDATE";
    public static final String EXTRA_STARTOFTIME = "com.example.a202sgi_assignment_tan_wei_jian_10674369.STARTOFTIME";
    public static final String EXTRA_ENDOFDATE = "com.example.a202sgi_assignment_tan_wei_jian_10674369.ENDOFDATE";
    public static final String EXTRA_ENDOFTIME = "com.example.a202sgi_assignment_tan_wei_jian_10674369.ENDOFTIME";
    public static final String EXTRA_TASKPRIORITY = "com.example.a202sgi_assignment_tan_wei_jian_10674369.TASKPRIORITY";
    public static final String EXTRA_TASKSTATUS = "com.example.a202sgi_assignment_tan_wei_jian_10674369.TASKSTATUS";

    private final LayoutInflater mInflater;
    Context mContext;
    com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskListOpenHelper mDB;

    public TaskListAdapter(Context context, com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskListOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.tasklist_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, int position) {
        com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskList current = mDB.query(position);
        holder.imagePriority.setImageDrawable(null);
        if(current.getTaskPriority().equalsIgnoreCase("High")){
            holder.imagePriority.setImageResource(R.drawable.red);
        }
        else if(current.getTaskPriority().equalsIgnoreCase("Medium")){
            holder.imagePriority.setImageResource(R.drawable.orange);
        }
        else{
            holder.imagePriority.setImageResource(R.drawable.green);
        }

        holder.imageStatus.setImageDrawable(null);
        if(current.getTaskStatus() == 1){
            holder.imageStatus.setImageResource(R.drawable.done);
        }
        else if(current.getTaskStatus() == 0){
            holder.imageStatus.setImageResource(R.drawable.in_progress);
        }
        else{
            holder.imageStatus.setImageResource(R.drawable.deadline);
        }

        String tempTitleName = current.getTitleName();
        String tempDescription = current.getTaskDescription();
        if(tempTitleName.length() > 15){
            tempTitleName = tempTitleName.substring(0,15);
            tempTitleName = tempTitleName + "...";
            holder.tvTitle.setText(tempTitleName);
        }
        else{
            holder.tvTitle.setText(tempTitleName);
        }
        if(tempDescription.length() > 15){
            tempDescription = tempDescription.substring(0,15);
            tempDescription = tempDescription + "...";
            holder.tvDescription.setText(tempDescription);
        }
        else{
            holder.tvDescription.setText(tempDescription);
        }

        holder.btnEdit.setOnClickListener(
                new btnOnClickListener(current.getTaskId(),current.getTitleName(),
                        current.getTaskDescription(), current.getTaskStartDate(),
                        current.getTaskStartTime(), current.getTaskEndDate(),
                        current.getTaskEndTime(), current.getTaskPriority(),
                        current.getTaskStatus()){
                    public void onClick(View v){
                        Intent i = new Intent(mContext,CreateTaskPage.class);
                        i.putExtra(EXTRA_ID,id);
                        i.putExtra(EXTRA_TITLE, titleForTask);
                        i.putExtra(EXTRA_DESCRIPTION, descriptionForTask);
                        i.putExtra(EXTRA_STARTOFDATE, startDateForTask);
                        i.putExtra(EXTRA_STARTOFTIME, startTimeForTask);
                        i.putExtra(EXTRA_ENDOFDATE, endDateForTask);
                        i.putExtra(EXTRA_ENDOFTIME, endTimeForTask);
                        i.putExtra(EXTRA_TASKPRIORITY, priorityOfTask);
                        i.putExtra(EXTRA_TASKSTATUS, statusOfTask);

                        ((Activity)(mContext)).startActivityForResult(i,MainActivity.WORD_EDIT);
                    }
                });

        final TaskViewHolder h = holder;

        holder.btnDelete.setOnClickListener(

                new btnOnClickListener(current.getTaskId(),current.getTitleName(),
                        current.getTaskDescription(), current.getTaskStartDate(),
                        current.getTaskStartTime(), current.getTaskEndDate(),
                        current.getTaskEndTime(), current.getTaskPriority(),
                        current.getTaskStatus()){
                    public void onClick(View v){

                        int deleted = mDB.delete(id, titleForTask, descriptionForTask,
                                startDateForTask, startTimeForTask, endDateForTask,
                                endTimeForTask, priorityOfTask, statusOfTask);
                        if(deleted > 0){
                           notifyItemRemoved(h.getAdapterPosition());

                        }
                        Toast.makeText(mContext, "Task has been moved to the recover " +
                                "bin successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return (int)mDB.count();
    }
}

