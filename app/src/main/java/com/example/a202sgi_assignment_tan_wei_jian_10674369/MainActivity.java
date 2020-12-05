package com.example.a202sgi_assignment_tan_wei_jian_10674369;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.text.TextUtils;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int WORD_EDIT = 1;
    public static final int WORD_ADD = -1;

    private RecyclerView mRecyclerView;

    private TaskListAdapter mAdapter;
    private TaskListOpenHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = new TaskListOpenHelper(this);
        SQLiteDatabase d = mDb.getReadableDatabase();
        mDb.checkStatus();

        // Create recycler view.
        mRecyclerView = (RecyclerView) findViewById(R.id.list_recyclerview);

        // Create an mAdapter and display the data.
        mAdapter = new TaskListAdapter(this, mDb);

        // Connect mAdapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);

        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Add a fab button for easier creating new entries.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start empty edit activity.
                Intent intent = new Intent(getBaseContext(), CreateTaskPage.class);
                startActivityForResult(intent, WORD_EDIT);
                vibe.vibrate(100);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper
                (new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        TaskList current = mDb.query(viewHolder.getAdapterPosition());
                        int deleted = mDb.delete(current.getTaskId(), current.getTitleName(), current.getTaskDescription(),
                                current.getTaskStartDate(), current.getTaskStartTime(), current.getTaskEndDate(), current.getTaskEndTime(), current.getTaskPriority(), current.getTaskStatus());
                        if (deleted > 0) {
                            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        }
                        //Toast.makeText(this, "Task has been moved to the trash bin.", Toast.LENGTH_SHORT).show();
                    }
                });

        helper.attachToRecyclerView(mRecyclerView);
    }

    //Add words: Get the word from the user
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Add code to update the database.

        if (requestCode == WORD_EDIT) {
            if (resultCode == RESULT_OK) {
                String taskTitle = data.getStringExtra(CreateTaskPage.EXTRA_TITLE);
                String taskDescription = data.getStringExtra(CreateTaskPage.EXTRA_DESCRIPTION);
                String startDateOfTask = data.getStringExtra(CreateTaskPage.EXTRA_STARTOFDATE);
                String startTimeOfTask = data.getStringExtra(CreateTaskPage.EXTRA_STARTOFTIME);
                String endDateOfTask = data.getStringExtra(CreateTaskPage.EXTRA_ENDOFDATE);
                String endTimeOfTask = data.getStringExtra(CreateTaskPage.EXTRA_ENDOFTIME);
                String taskPriority = data.getStringExtra(CreateTaskPage.EXTRA_TASKPRIORITY);
                int taskStatus = data.getIntExtra(CreateTaskPage.EXTRA_TASKSTATUS, 0);

                if (!TextUtils.isEmpty(taskTitle)) {
                    int id = data.getIntExtra(TaskListAdapter.EXTRA_ID, -99);

                    if (id == WORD_ADD) {
                        mDb.insert(taskTitle, taskDescription, startDateOfTask, startTimeOfTask, endDateOfTask, endTimeOfTask, taskPriority, taskStatus);

                    } else if (id > 0) {
                        mDb.update(id, taskTitle, taskDescription, startDateOfTask, startTimeOfTask, endDateOfTask, endTimeOfTask, taskPriority, taskStatus);

                    }

                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleted_task: {
                Intent i = new Intent(this, ListOfDeletedTasks.class);
                startActivity(i);
                break;
            }

        }
        return true;
    }
}