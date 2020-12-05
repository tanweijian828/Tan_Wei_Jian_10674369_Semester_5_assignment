package com.example.a202sgi_assignment_tan_wei_jian_10674369;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ListOfDeletedTasks extends AppCompatActivity {

    private RecyclerView recyclerView;

    private com.example.a202sgi_assignment_tan_wei_jian_10674369.ListOfDeletedTasksAdapter deletedTaskAdapter;
    private TaskListOpenHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_task_list);

        mDb = new TaskListOpenHelper(this);
        SQLiteDatabase d = mDb.getReadableDatabase();

        // Create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.delList_recyclerview);
        // Create an adapter in order to display the data.
        deletedTaskAdapter = new com.example.a202sgi_assignment_tan_wei_jian_10674369.ListOfDeletedTasksAdapter(this, mDb);
        // Connect the adapter with the recycler view.
        recyclerView.setAdapter(deletedTaskAdapter);
        // Give the recycler view a default layout manager.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
