package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.beignerdranch.android.criminalIntent.Date";

    private static final String ARG_DATE = "Date";

    public static DatePickerFragment newIntance(Date date) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_DATE, date);

        DatePickerFragment instance = new DatePickerFragment();
        instance.setArguments(arguments);
        return instance;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePicker datePickerView = (DatePicker) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        datePickerView.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(datePickerView)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePickerView.getYear();
                        int month = datePickerView.getMonth();
                        int day = datePickerView.getDayOfMonth();

                        Date date = new GregorianCalendar(year, month, day).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
