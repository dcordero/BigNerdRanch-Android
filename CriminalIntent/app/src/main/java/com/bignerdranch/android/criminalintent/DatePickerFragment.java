package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

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

        DatePicker datePickerView = (DatePicker) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        datePickerView.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(datePickerView)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
