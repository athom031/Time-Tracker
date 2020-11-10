package com.example.timetracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {
    int year;
    int month;
    int day;

    int minutes;

    String title;
    String message;

    private static final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /*  Dialog constructor
     *  @param int year, int month, int day, int minutes
     *  @return VOID
     *
     *  constructor that prepares dialog title and message
     *  minutes worked on key date already searched and passed
     *  grammar specifics for plural hours and days
     */
    public Dialog(int year, int month, int day, int minutes) {
        this.year = year;
        this.month = month;
        this.day = day;

        this.minutes = minutes;

        String y = "" + year;
        String m = months[month];
        String d = (day < 10) ? "0" + day : "" + day;

        title = String.format("Time tracked on %s %s %s", d, m, y);

        if(minutes == 0) {
            message = "You have not yet logged time for this day.";
        } else {
            int h = (int) Math.floor(minutes / 60);
            int min = minutes % 60;

            String hourType = (h == 1) ? " hour" : " hours";
            String minuteType = (min == 1) ? " minute" : " minutes";

            message = String.format("%d %s and %d %s", h, hourType, min, minuteType);
        }
    }

    /*  Dialog creator
     *  @param @Nullable Bundle savedInstanceState
     *  @return android.app.Dialog
     *
     *  uses AlertDialog class Builder with message and titles set up in class constructor
     *  no need to do anything when positive button is clicked
     *  returns created dialog from AlertDialog builder
     *  grammar specifics for plural hours and days
     */
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
        return builder.create();
    }

    /*  Set button color scheme
     *  @param VOID
     *  @return VOID
     *
     *  Changes color of background and text color for button
     *  These colors are hard coded
     */
    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFD82530"));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);
    }
}

