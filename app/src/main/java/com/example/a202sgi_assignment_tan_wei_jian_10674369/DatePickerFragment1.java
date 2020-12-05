package com.example.a202sgi_assignment_tan_wei_jian_10674369;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment1 extends DialogFragment implements
        DatePickerDialog.OnDateSetListener{

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getContext(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        com.example.a202sgi_assignment_tan_wei_jian_10674369.CreateTaskPage ct =
                (com.example.a202sgi_assignment_tan_wei_jian_10674369.CreateTaskPage)
                        getActivity();
        ct.setDate(year,month+1,dayOfMonth);
    }
}
