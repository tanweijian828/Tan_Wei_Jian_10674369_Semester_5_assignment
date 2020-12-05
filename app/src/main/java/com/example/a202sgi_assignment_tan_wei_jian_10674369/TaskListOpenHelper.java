package com.example.a202sgi_assignment_tan_wei_jian_10674369;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TaskListOpenHelper extends SQLiteOpenHelper {

    //has to be 1 first time or app will crash
    private static final String DATABASE_NAME = "taskList";
    private static final int DB_VERSION = 1;

    //column names...
    private static final String TASK_LIST_TABLE = "task_entries";
    private static final String TASK_ID = "task_id";
    private static final String TASK_TITLENAME = "task_title";
    private static final String TASK_DESCRIPTION = "task_desc";
    private static final String TASK_START_OF_DATE = "task_start_date";
    private static final String TASK_END_OF_DATE = "task_end_date";
    private static final String TASK_START_OF_TIME = "task_start_time";
    private static final String TASK_END_OF_TIME = "task_end_time";
    private static final String TASK_PRIORITY = "task_priority";
    private static final String TASK_STATUS = "task_status";

    private static final String DEL_TASK_LIST_TABLE = "del_task_entries";
    private static final String DEL_TASK_ID = "del_task_id";
    private static final String DEL_TASK_TITLENAME = "del_task_title";
    private static final String DEL_TASK_DESCRIPTION = "del_task_desc";
    private static final String DEL_TASK_START_OF_DATE = "del_task_start_date";
    private static final String DEL_TASK_END_OF_DATE = "del_task_end_date";
    private static final String DEL_TASK_START_OF_TIME = "del_task_start_time";
    private static final String DEL_TASK_END_OF_TIME = "del_task_end_time";
    private static final String DEL_TASK_PRIORITY = "del_task_priority";
    private static final String DEL_TASK_STATUS = "del_task_status";

    Context mContext;

    //Reference to database
    public SQLiteDatabase mReadableDB,mWritableDB;



    public TaskListOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DB_VERSION);
    }

    //Build the SQL query that creates the tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TASK_LIST_TABLE + "(" + TASK_ID + " INTEGER PRIMARY KEY,"
                + TASK_TITLENAME + " TEXT, " + TASK_DESCRIPTION + " TEXT, "
                + TASK_START_OF_DATE + " TEXT, " + TASK_START_OF_TIME + " TEXT, "
                + TASK_END_OF_DATE + " TEXT, " + TASK_END_OF_TIME + " TEXT, " + TASK_PRIORITY + " TEXT, " + TASK_STATUS + " INTEGER );";

        String del_sql = "CREATE TABLE " + DEL_TASK_LIST_TABLE + "(" + DEL_TASK_ID + " INTEGER PRIMARY KEY,"
                + DEL_TASK_TITLENAME + " TEXT, " + DEL_TASK_DESCRIPTION + " TEXT, "
                + DEL_TASK_START_OF_DATE + " TEXT, " + DEL_TASK_START_OF_TIME + " TEXT, "
                + DEL_TASK_END_OF_DATE + " TEXT, " + DEL_TASK_END_OF_TIME + " TEXT, " + DEL_TASK_PRIORITY + " TEXT, " + DEL_TASK_STATUS + " INTEGER );";

        db.execSQL(sql);
        db.execSQL(del_sql);
    }

    public void checkStatus(){
        String status_sql = "SELECT * FROM " + TASK_LIST_TABLE + ";";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat sdfCurrDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdfCurrTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdfCurrTime.setTimeZone(TimeZone.getDefault());
        int tempStatus = -1;
        Cursor cursor = null;

        try{
            if(mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(status_sql,null);
            if (cursor.moveToFirst()) {
                do {
                    try{
                        String currentDate = sdfCurrDate.format(new Date());
                        String currentTime = sdfCurrTime.format(new Date());
                        Date today = sdf.parse(currentDate + " " + currentTime);
                        Date ending = sdf.parse(cursor.getString(cursor.getColumnIndex(TASK_END_OF_DATE)) + " " + cursor.getString(cursor.getColumnIndex(TASK_END_OF_TIME)));

                        int tempId = cursor.getInt(cursor.getColumnIndex(TASK_ID));
                        int statusCompleted = cursor.getInt(cursor.getColumnIndex(TASK_STATUS));

                        if(statusCompleted == 0){

                            if(today.compareTo(ending) > 0){
                                if(mWritableDB == null){
                                    mWritableDB = getWritableDatabase();
                                }

                                //Create a container for the data
                                ContentValues values = new ContentValues();

                                values.put(TASK_STATUS,tempStatus);
                                mWritableDB.update(TASK_LIST_TABLE,values,TASK_ID + "=?",new String[] {String.valueOf(tempId)});
                            }
                            else if(today.compareTo(ending) < 0){
                                if(mWritableDB == null){
                                    mWritableDB = getWritableDatabase();
                                }

                                ContentValues values = new ContentValues();
                                values.put(TASK_STATUS,0);
                                mWritableDB.update(TASK_LIST_TABLE,values,TASK_ID + "=?",new String[] {String.valueOf(tempId)});
                            }
                        }
                    }
                    catch(Exception e){
                        Log.d("TaskListOpenHelper","Check Status: " + e.getMessage());
                    }

                } while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            Log.d("TaskListOpenHelper","Check Status: " + e.getMessage());
        }
    }

    //SELECT query method
    public com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskList query(int position){
        String sql = "SELECT * FROM " + TASK_LIST_TABLE + " ORDER BY " + TASK_STATUS + " DESC " + "LIMIT " + position + ",1";

        Cursor cursor = null;
        com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskList entry = new com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskList();

        try{
            if(mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(sql,null);
            cursor.moveToFirst();
            entry.setTaskId(cursor.getInt(cursor.getColumnIndex(TASK_ID)));
            entry.setTitleName(cursor.getString(cursor.getColumnIndex(TASK_TITLENAME)));
            entry.setTaskDescription(cursor.getString(cursor.getColumnIndex(TASK_DESCRIPTION)));
            entry.setTaskStartDate(cursor.getString(cursor.getColumnIndex(TASK_START_OF_DATE)));
            entry.setTaskStartTime(cursor.getString(cursor.getColumnIndex(TASK_START_OF_TIME)));
            entry.setTaskEndDate(cursor.getString(cursor.getColumnIndex(TASK_END_OF_DATE)));
            entry.setTaskEndTime(cursor.getString(cursor.getColumnIndex(TASK_END_OF_TIME)));
            entry.setTaskPriority(cursor.getString(cursor.getColumnIndex(TASK_PRIORITY)));
            entry.setTaskStatus(cursor.getInt(cursor.getColumnIndex(TASK_STATUS)));

        }
        catch(Exception e){
            Log.d("TaskListOpenHelper", "Query: " + e.getMessage());
        }
        finally{
            cursor.close();
            return entry;
        }
    }

    public com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskList del_query(int position){
        String sql = "SELECT * FROM " + DEL_TASK_LIST_TABLE + " ORDER BY " + DEL_TASK_STATUS + " DESC " + "LIMIT " + position + ",1";

        Cursor cursor = null;
        com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskList entry = new com.example.a202sgi_assignment_tan_wei_jian_10674369.TaskList();

        try{
            if(mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(sql,null);
            cursor.moveToFirst();
            entry.setTaskId(cursor.getInt(cursor.getColumnIndex(DEL_TASK_ID)));
            entry.setTitleName(cursor.getString(cursor.getColumnIndex(DEL_TASK_TITLENAME)));
            entry.setTaskDescription(cursor.getString(cursor.getColumnIndex(DEL_TASK_DESCRIPTION)));
            entry.setTaskStartDate(cursor.getString(cursor.getColumnIndex(DEL_TASK_START_OF_DATE)));
            entry.setTaskStartTime(cursor.getString(cursor.getColumnIndex(DEL_TASK_START_OF_TIME)));
            entry.setTaskEndDate(cursor.getString(cursor.getColumnIndex(DEL_TASK_END_OF_DATE)));
            entry.setTaskEndTime(cursor.getString(cursor.getColumnIndex(DEL_TASK_END_OF_TIME)));
            entry.setTaskPriority(cursor.getString(cursor.getColumnIndex(DEL_TASK_PRIORITY)));
            entry.setTaskStatus(cursor.getInt(cursor.getColumnIndex(DEL_TASK_STATUS)));

        }
        catch(Exception e){
            Log.d("TaskListOpenHelper", "Query delete: " + e.getMessage());
        }
        finally{
            cursor.close();
            return entry;
        }

    }

    public long insert(String title, String desc, String startDate, String startTime, String endDate, String endTime, String priority, int status){
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(TASK_TITLENAME,title);
        values.put(TASK_DESCRIPTION,desc);
        values.put(TASK_START_OF_DATE,startDate);
        values.put(TASK_START_OF_TIME,startTime);
        values.put(TASK_END_OF_DATE,endDate);
        values.put(TASK_END_OF_TIME,endTime);
        values.put(TASK_PRIORITY,priority);
        values.put(TASK_STATUS,status);

        try{
            if(mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(TASK_LIST_TABLE,null,values);
        }
        catch(Exception e){
            Log.d("TaskListOpenHelper","Insert: " + e.getMessage());
        }
        finally {
            return newId;
        }
    }

    public long del_insert(int id){
        String del_sql = "SELECT * FROM " + DEL_TASK_LIST_TABLE + " WHERE " + DEL_TASK_ID + " = " + id;

        Cursor cursor = null;
        cursor = mReadableDB.rawQuery(del_sql,null);
        cursor.moveToFirst();
        int del_id = cursor.getInt(cursor.getColumnIndex(DEL_TASK_ID));
        String del_title = cursor.getString(cursor.getColumnIndex(DEL_TASK_TITLENAME));
        String del_desc = cursor.getString(cursor.getColumnIndex(DEL_TASK_DESCRIPTION));
        String del_startdate = cursor.getString(cursor.getColumnIndex(DEL_TASK_START_OF_DATE));
        String del_starttime = cursor.getString(cursor.getColumnIndex(DEL_TASK_START_OF_TIME));
        String del_enddate = cursor.getString(cursor.getColumnIndex(DEL_TASK_END_OF_DATE));
        String del_endtime = cursor.getString(cursor.getColumnIndex(DEL_TASK_END_OF_TIME));
        String del_priority = cursor.getString(cursor.getColumnIndex(DEL_TASK_PRIORITY));
        int del_status = cursor.getInt(cursor.getColumnIndex(DEL_TASK_STATUS));


        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(TASK_TITLENAME,del_title);
        values.put(TASK_DESCRIPTION,del_desc);
        values.put(TASK_START_OF_DATE,del_startdate);
        values.put(TASK_START_OF_TIME,del_starttime);
        values.put(TASK_END_OF_DATE,del_enddate);
        values.put(TASK_END_OF_TIME,del_endtime);
        values.put(TASK_PRIORITY,del_priority);
        values.put(TASK_STATUS,del_status);

        try{
            if(mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(TASK_LIST_TABLE,null,values);
        }
        catch(Exception e){
            Log.d("TaskListOpenHelper","Insert: " + e.getMessage());
        }
        finally {
            return newId;
        }
    }

    //return the number of items in the table
    public long count(){
        if (mReadableDB == null){
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB,TASK_LIST_TABLE);
    }

    public long delete_count(){
        if (mReadableDB == null){
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB,DEL_TASK_LIST_TABLE);
    }

    public int update(int id, String title, String desc, String startDate, String startTime, String endDate, String endTime, String priority, int status){
        int numOfRowUpdated = -1;

        ContentValues values = new ContentValues();
        values.put(TASK_TITLENAME,title);
        values.put(TASK_DESCRIPTION,desc);
        values.put(TASK_START_OF_DATE,startDate);
        values.put(TASK_START_OF_TIME,startTime);
        values.put(TASK_END_OF_DATE,endDate);
        values.put(TASK_END_OF_TIME,endTime);
        values.put(TASK_PRIORITY,priority);
        values.put(TASK_STATUS,status);

        try{
            if(mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }

            numOfRowUpdated = mWritableDB.update(TASK_LIST_TABLE,values,TASK_ID + "=?",new String[] {String.valueOf(id)});
        }
        catch(Exception e){
            Log.d("TaskListOpenHelper","update: " + e.getMessage());
        }
        finally {
            return numOfRowUpdated;
        }

    }

    public int delete(int id, String title, String desc, String startDate, String startTime, String endDate, String endTime, String priority, int status){
        int deleted = 0;

        try{
            if(mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }
            insert_del(title,desc,startDate,startTime,endDate,endTime,priority,status);

            deleted = mWritableDB.delete(TASK_LIST_TABLE,TASK_ID + "=?", new String[] {String.valueOf(id)});
        }
        catch(Exception e){
            Log.d("TaskListOpenHelper", "Delete: " + e.getMessage());
        }

        return deleted;

    }

    public long insert_del(String title, String desc, String startDate, String startTime, String endDate, String endTime, String priority, int status){
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(DEL_TASK_TITLENAME,title);
        values.put(DEL_TASK_DESCRIPTION,desc);
        values.put(DEL_TASK_START_OF_DATE,startDate);
        values.put(DEL_TASK_START_OF_TIME,startTime);
        values.put(DEL_TASK_END_OF_DATE,endDate);
        values.put(DEL_TASK_END_OF_TIME,endTime);
        values.put(DEL_TASK_PRIORITY,priority);
        values.put(DEL_TASK_STATUS,status);

        try{
            if(mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(DEL_TASK_LIST_TABLE,null,values);
        }
        catch(Exception e){
            Log.d("TaskListOpenHelper","Insert to del: " + e.getMessage());
        }
        finally {
            return newId;
        }
    }

    public int delete_forever(int id){
        int deleted = 0;

        try{
            if(mWritableDB == null){
                mWritableDB = getWritableDatabase();
            }

            deleted = mWritableDB.delete(DEL_TASK_LIST_TABLE,DEL_TASK_ID + "=?", new String[] {String.valueOf(id)});
        }
        catch(Exception e){
            Log.d("TaskListOpenHelper", "Delete: " + e.getMessage());
        }

        return deleted;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mReadableDB.execSQL("drop table if EXISTS " + TASK_LIST_TABLE);
        onCreate(db);
    }
}