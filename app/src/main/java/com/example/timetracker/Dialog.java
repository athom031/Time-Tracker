package com.example.timetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;


public class Dialog extends AppCompatDialogFragment {
    String title;
    String message;
    //static final String[] months = ["Jan", "Feb", "Mar", "Aug", ]
    //use simple date format to parse the way you want to
    public Dialog(String m, String d, String y, String key, int minutes) {


        title = "Time tracked on " + key;
        if(minutes == -1) {
            message = "You have not yet logged time for this day.";
        } else {
            int h = (int) Math.floor(minutes / 60);
            int min = minutes % 60;

            String hourType = (h == 1) ? " hour" : " hours";
            String minuteType = (min == 1) ? " minute" : " minutes";

            message = h + hourType + " " + m + minuteType;
        }
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFD82530"));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);

    }
}

