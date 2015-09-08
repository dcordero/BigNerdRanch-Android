package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME = "com.bignerdranch.android.CriminalIntent.Time";

    private static final String ARG_DATE = "Date";

    private Date mDate;

    public static TimePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDate = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        final TimePicker timePickerView = (TimePicker) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        timePickerView.setIs24HourView(true);
        timePickerView.setCurrentHour(hour);
        timePickerView.setCurrentMinute(minutes);

        return new AlertDialog.Builder(getActivity())
                .setView(timePickerView)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(mDate);
                        calendar.set(Calendar.HOUR, timePickerView.getCurrentHour());
                        calendar.set(Calendar.MINUTE, timePickerView.getCurrentMinute());

                        mDate = calendar.getTime();
                        sendResult(Activity.RESULT_OK, mDate);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
