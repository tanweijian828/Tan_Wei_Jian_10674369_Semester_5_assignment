package com.example.a202sgi_assignment_tan_wei_jian_10674369;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class CreateTaskPage extends AppCompatActivity {

    private static final String TAG = com.example.a202sgi_assignment_tan_wei_jian_10674369.CreateTaskPage.class.getSimpleName();

    private static final int NO_ID = -99;
    private static final String NO_WORD = "";

    private EditText etTitle;
    private EditText etDescription;
    private TextView tvStartDate, tvEndDate, tvStartTime, tvEndTime;
    private ImageButton btnStartDate, btnStartTime;
    private ImageButton btnEndDate, btnEndTime;
    private RadioGroup rgTaskPriority;
    private RadioButton rbPriorityHigh, rbPriorityMedium, rbPriorityLow;
    private Button btnSave, btnStatus;

    String priorityOfTask;
    int currentStatusLevel;

    public static final String EXTRA_TITLE = "com.example.windows10.ibm2105_cw2_source_g28.TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.windows10.ibm2105_cw2_source_g28.DESCRIPTION";
    public static final String EXTRA_STARTOFDATE = "com.example.windows10.ibm2105_cw2_source_g28.STARTOFDATE";
    public static final String EXTRA_STARTOFTIME = "com.example.windows10.ibm2105_cw2_source_g28.STARTOFTIME";
    public static final String EXTRA_ENDOFDATE = "com.example.windows10.ibm2105_cw2_source_g28.ENDOFDATE ";
    public static final String EXTRA_ENDOFTIME = "com.example.windows10.ibm2105_cw2_source_g28.ENDOFTIME";
    public static final String EXTRA_TASKPRIORITY = "com.example.windows10.ibm2105_cw2_source_g28.TASKPRIORITY ";
    public static final String EXTRA_TASKSTATUS = "com.example.windows10.ibm2105_cw2_source_g28.TASKSTATUS";

    int mId = MainActivity.WORD_ADD;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //empty
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String titleUserInput = etTitle.getText().toString().trim();
            String startOfTaskDate = tvStartDate.getText().toString();
            String startOfTaskTime = tvStartTime.getText().toString();
            String endOfTaskDate = tvEndDate.getText().toString();
            String endOfTaskTime = tvEndTime.getText().toString();

            try{
                Date startDateforTask = sdf.parse(startOfTaskDate + " " + startOfTaskTime);
                Date endDateforTask = sdf.parse(endOfTaskDate + " " + endOfTaskTime);

                if(startDateforTask.compareTo(endDateforTask) > 0){
                    currentStatusLevel = -1;
                    btnStatus.setEnabled(false);
                    btnStatus.setText("Overdue");
                }
                else if(startDateforTask.compareTo(endDateforTask) < 0){
                    currentStatusLevel = 0;
                    btnStatus.setEnabled(true);
                    btnStatus.setText("Incomplete");
                }
            }
            catch(Exception e){
                Log.d("CreateTaskPage","Date: " + e.getMessage());
            }

            btnSave.setEnabled(!titleUserInput.isEmpty() && !startOfTaskDate.isEmpty() &&
                    !startOfTaskTime.isEmpty() && !endOfTaskDate.isEmpty() &&
                    !endOfTaskTime.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnStatus = (Button)findViewById(R.id.btnStatus);
        tvStartDate = (TextView)findViewById(R.id.tvStartDate);
        tvEndDate = (TextView)findViewById(R.id.tvEndDate);
        tvStartTime = (TextView)findViewById(R.id.tvStartTime);
        tvEndTime = (TextView)findViewById(R.id.tvEndTime);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etDescription = (EditText)findViewById(R.id.etDescription);
        btnStartDate = (ImageButton)findViewById(R.id.btnStartDate);
        btnEndDate = (ImageButton)findViewById(R.id.btnEndDate);
        btnStartTime = (ImageButton)findViewById(R.id.btnStartTime);
        btnEndTime = (ImageButton)findViewById(R.id.btnEndTime);
        rgTaskPriority = (RadioGroup)findViewById(R.id.rgTaskPriority);
        rbPriorityHigh = (RadioButton)findViewById(R.id.rbPriorityHigh);
        rbPriorityMedium = (RadioButton)findViewById(R.id.rbPriorityMedium);
        rbPriorityLow = (RadioButton)findViewById(R.id.rbPriorityLow);
        btnSave.setEnabled(false);
        rbPriorityHigh.setChecked(true);
        priorityOfTask = "High";
        currentStatusLevel = 0;

        etTitle.addTextChangedListener(textWatcher);
        tvStartDate.addTextChangedListener(textWatcher);
        tvStartTime.addTextChangedListener(textWatcher);
        tvEndDate.addTextChangedListener(textWatcher);
        tvEndTime.addTextChangedListener(textWatcher);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int id = extras.getInt(TaskListAdapter.EXTRA_ID, NO_ID);
            String titleOfTask = extras.getString(TaskListAdapter.EXTRA_TITLE, NO_WORD);
            String description = extras.getString(TaskListAdapter.EXTRA_DESCRIPTION, NO_WORD);
            String startDate = extras.getString(TaskListAdapter.EXTRA_STARTOFDATE, NO_WORD);
            String startTime = extras.getString(TaskListAdapter.EXTRA_STARTOFTIME, NO_WORD);
            String endDate = extras.getString(TaskListAdapter.EXTRA_ENDOFDATE, NO_WORD);
            String endTime = extras.getString(TaskListAdapter.EXTRA_ENDOFTIME, NO_WORD);
            String priority = extras.getString(TaskListAdapter.EXTRA_TASKPRIORITY, NO_WORD);
            int status = extras.getInt(TaskListAdapter.EXTRA_TASKSTATUS, 0);

            if (id != NO_ID && titleOfTask != NO_WORD && priority != NO_WORD) {
                mId = id;
                etTitle.setText(titleOfTask);
                etDescription.setText(description);
                tvStartDate.setText(startDate);
                tvStartTime.setText(startTime);
                tvEndDate.setText(endDate);
                tvEndTime.setText(endTime);
                if(priority.equalsIgnoreCase("High")){
                    priorityOfTask = "High";
                    rbPriorityHigh.setChecked(true);
                }
                else if(priority.equalsIgnoreCase("Medium")){
                    priorityOfTask = "Medium";
                    rbPriorityMedium.setChecked(true);
                }
                else{
                    priorityOfTask = "Low";
                    rbPriorityLow.setChecked(true);
                }

                if(status == 0){
                    currentStatusLevel = 0;
                    btnStatus.setText("Incomplete");
                }
                else if(status == 1){
                    currentStatusLevel = 1;
                    btnStatus.setText("Completed");
                }
                else if(status == -1){
                    currentStatusLevel = -1;
                    btnStatus.setEnabled(false);
                    btnStatus.setText("Overdue");
                }

            }

        } // Otherwise, start with empty fields.
    }

    public void returnReply(View view) {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String startOfDate = tvStartDate.getText().toString();
        String startOfTime = tvStartTime.getText().toString();
        String endOfDate = tvEndDate.getText().toString();
        String endOfTime = tvEndTime.getText().toString();
        String priorityForTask = priorityOfTask;
        int statusLevel = currentStatusLevel;

        Intent replyIntent = new Intent();
        replyIntent.putExtra(TaskListAdapter.EXTRA_ID, mId);
        replyIntent.putExtra(EXTRA_TITLE, title);
        replyIntent.putExtra(EXTRA_DESCRIPTION, description);
        replyIntent.putExtra(EXTRA_STARTOFDATE, startOfDate);
        replyIntent.putExtra(EXTRA_STARTOFTIME, startOfTime);
        replyIntent.putExtra(EXTRA_ENDOFDATE, endOfDate);
        replyIntent.putExtra(EXTRA_ENDOFTIME, endOfTime);
        replyIntent.putExtra(EXTRA_TASKPRIORITY, priorityForTask);
        replyIntent.putExtra(EXTRA_TASKSTATUS, statusLevel);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    public void rbPriority_clicked(View view) {
        int rbId = rgTaskPriority.getCheckedRadioButtonId();
        RadioButton rbPriority = findViewById(rbId);
        priorityOfTask = rbPriority.getText().toString();
    }

    public void btnStartDate_clicked(View view) {
        DatePickerFragment1 df = new DatePickerFragment1();
        df.show(getSupportFragmentManager(),"Pick a Date");
    }

    public void setDate(int year, int month, int day){
        String newDate = day + "/" + month + "/" + year;
        tvStartDate.setText(newDate);
    }

    public void btnEndDate_clicked(View view) {
        DatePickerFragment2 df = new DatePickerFragment2();
        df.show(getSupportFragmentManager(),"Pick a Date");
    }

    public void setDate2(int year, int month, int day){
        String newDate = day + "/" + month + "/" + year;
        tvEndDate.setText(newDate);
    }

    public void btnStartTime_clicked(View view) {
        TimePickerFragment1 tf = new TimePickerFragment1();
        tf.show(getSupportFragmentManager(), "Pick a Time");
    }

    public void setTime(int hours, int minutes){
        String newTime = hours + ":" + minutes;
        if(minutes < 10){
            newTime = hours + ":0" + minutes;
        }
        tvStartTime.setText(newTime);
    }

    public void btnEndTime_clicked(View view) {
        TimePickerFragment2 tf = new TimePickerFragment2();
        tf.show(getSupportFragmentManager(), "Pick a Time");
    }

    public void setTime2(int hours, int minutes){
        String newTime = hours + ":" + minutes;
        if(minutes < 10){
            newTime = hours + ":0" + minutes;
        }
        tvEndTime.setText(newTime);
    }

    public void btnStatus_clicked(View view) {
        if(currentStatusLevel == 0){
            currentStatusLevel = 1;
            btnStatus.setText("Status: Completed");
            Toast.makeText(this, "Task set to completed!", Toast.LENGTH_SHORT).show();
        }
        else{
            currentStatusLevel = 0;
            btnStatus.setText("Status: Incomplete");
            Toast.makeText(this, "Task set to incomplete!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.create_task,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuShare:{

                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String startDate = tvStartDate.getText().toString();
                String startTime = tvStartTime.getText().toString();
                String endDate = tvEndDate.getText().toString();
                String endTime = tvEndTime.getText().toString();
                String priority = priorityOfTask;
                int status = currentStatusLevel;
                String tempStatus = "";
                if(status == 1){
                    tempStatus = "Status: Completed";
                }
                else if(status == 0){
                    tempStatus = "Status: Incomplete";
                }
                else if(status == -1){
                    tempStatus = "Status: Overdue";
                }

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String shareSubject = "My Task to share";
                String shareBody = "Task Details" + "\n" +
                        "Title of this Task: " + title + "\n" +
                        "Description of this Task: " + description + "\n" +
                        "Starting date of Current Task: " + startDate + "\n" +
                        "Starting time of Current Task: " + startTime + "\n" +
                        "Ending date of Current Task: " + endDate + "\n" +
                        "Ending time of Current Task: " + endTime + "\n" +
                        "Priority Level of Task: " + priority + "\n" +
                        "Status of Task: " + tempStatus;



                share.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
                share.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(share, "Share : "));

                break;
            }

        }
        return true;
    }


}