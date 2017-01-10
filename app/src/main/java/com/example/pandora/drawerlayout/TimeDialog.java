package com.example.pandora.drawerlayout;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pandora.drawerlayout.TuitionCentre.AddSubjectDialog;
import com.example.pandora.drawerlayout.TuitionCentre.CentreTimeTable;

/**
 * Created by Pandora on 9/13/2016.
 */
public class TimeDialog extends DialogFragment {

    View view;
    float height, width;
    TimePicker timePicker;
    Button button;
    View fragmentView;
    TextView setTime;
    int start_time;

    public TimeDialog(View view,TextView textView){
        fragmentView = view;
        setTime = textView;
        start_time = 12;
    }

    public TimeDialog(View view,TextView startTime,TextView textView){
        fragmentView = view;
        setTime = textView;
        if (startTime.getText().toString().equals("Start Time")){
            start_time = 12;
        }else{
            String[] time = startTime.getText().toString().split(" ");
            Log.d("time",time[3]);
            if (time[3].equals("AM")){
                start_time = Integer.parseInt(time[0]) + 2;
            }else{
                start_time = Integer.parseInt(time[0]) + 12;
                for (int i=0;i<2;i++){
                    start_time += 1;
                    if (start_time == 24){
                        start_time = 0;
                    }
                }
            }
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setOwnerActivity(getActivity());
        dialog.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        showImmersive(getFragmentManager());
    }

    private void showImmersive(FragmentManager manager) {
        // It is necessary to call executePendingTransactions() on the FragmentManager
        // before hiding the navigation bar, because otherwise getWindow() would raise a
        // NullPointerException since the window was not yet created.
        manager.executePendingTransactions();

        // Copy flags from the activity, assuming it's fullscreen.
        // It is important to do this after show() was called. If we would do this in onCreateDialog(),
        // we would get a requestFeature() error.
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                getActivity().getWindow().getDecorView().getSystemUiVisibility()
        );

        // Make the dialogs window focusable again
        getDialog().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_set_time, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        getDialog().setCancelable(true);
        timePicker = new TimePicker(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.test);
        linearLayout.addView(timePicker);
        timePicker.setIs24HourView(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(start_time);
            timePicker.setMinute(0);
        } else {
            timePicker.setCurrentHour(start_time);
            timePicker.setCurrentMinute(0);
        }

        button = (Button) view.findViewById(R.id.setTime);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour;
                String minute;
                boolean isAm;

                timePicker.clearFocus();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute()+"";
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute()+"";
                }

                if (hour < 12){
                    isAm = true;
                }else{
                    isAm = false;
                    if (hour >= 13){
                        hour = hour - 12;
                    }
                }

                if (hour == 0){
                    hour = 12;
                }

                String string;
                if (isAm){
                    string = " AM";
                }else{
                    string = " PM";
                }

                if (minute.length() == 1){
                    minute = "0" + minute;
                }

                setTime.setText(hour + " : " + minute + string);

                getDialog().cancel();
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
