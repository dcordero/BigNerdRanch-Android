package com.bignerdranch.android.criminalintent;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View datePickerView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        return new AlertDialog.Builder(getActivity())
                .setView(datePickerView)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
