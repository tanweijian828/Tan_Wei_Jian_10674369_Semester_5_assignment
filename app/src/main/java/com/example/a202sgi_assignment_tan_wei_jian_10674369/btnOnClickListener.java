package com.example.a202sgi_assignment_tan_wei_jian_10674369;

import android.view.View;

/**
 * Instantiated for the Edit and Delete buttons in TaskListAdapter.
 */
public class btnOnClickListener implements View.OnClickListener {
    private static final String TAG = View.OnClickListener.class.getSimpleName();

    int id, statusOfTask;
    String titleForTask, descriptionForTask, startDateForTask, startTimeForTask,
            endDateForTask, endTimeForTask, priorityOfTask;

    public btnOnClickListener(int id, String titleNameForTask, String descriptionForTask,
                              String startDateForTask, String startTimeforTask,
                              String endDateForTask, String endTimeForTask,
                              String priorityOfTask, int statusOfTask) {
        this.id = id;
        this.titleForTask = titleNameForTask;
        this.descriptionForTask = descriptionForTask;
        this.startDateForTask = startDateForTask;
        this.startTimeForTask = startTimeforTask;
        this.endDateForTask = endDateForTask;
        this.endTimeForTask = endTimeForTask;
        this.priorityOfTask = priorityOfTask;
        this.statusOfTask = statusOfTask;
    }

    public void onClick(View v) {
        // Implemented in TaskListAdapter
    }
}
