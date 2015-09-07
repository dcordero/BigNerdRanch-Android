package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TimePicker timePickerView = (TimePicker) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        return new AlertDialog.Builder(getActivity())
                .setView(timePickerView)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
}
}
