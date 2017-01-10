package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.TimeDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Pandora on 9/12/2016.
 */
public class AddSubjectDialog extends DialogFragment {

    View view;
    float height, width;
    RelativeLayout relativeLayout;
    Spinner spinner;
    TextView startTime, endTime, set, cancel;
    CentreTimeTable centreTimeTable;
    String day;
    EditText subjectName;
    List<String> listDays = new ArrayList<>();


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_add_subject, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        centreTimeTable = (CentreTimeTable) getActivity();

        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative);
        startTime = (TextView) view.findViewById(R.id.startTime);
        endTime = (TextView) view.findViewById(R.id.endTime);
        set = (TextView) view.findViewById(R.id.Set);
        cancel = (TextView) view.findViewById(R.id.cancel);
        subjectName = (EditText) view.findViewById(R.id.subjectName);

        startTime.setTag("startTime");
        startTime.setWidth((int) (width / 2.7));
        startTime.setHeight((int) (height / 12.8));

        endTime.setTag("endTime");
        endTime.setWidth((int) (width / 2.7));
        endTime.setHeight((int) (height / 12.8));

        set.setWidth((int) (width / 3.6));
        set.setHeight((int) (height / 19.2));
        set.setTag("set");

        cancel.setWidth((int) (width / 3.6));
        cancel.setHeight((int) (height / 19.2));
        cancel.setTag("cancel");

        subjectName.setWidth((int) (width / 1.8));
        subjectName.setHeight((int) (height / 12.8));

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String test = "Choose a day";

        Collections.addAll(listDays, days);
        listDays.add(test);

        spinner = (Spinner) view.findViewById(R.id.day);
        HintAdapter adapter = new HintAdapter(getActivity(), android.R.layout.simple_spinner_item, listDays);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Log.d("adapter", adapter.getCount() + "");
        // show hint
        spinner.setSelection(adapter.getCount());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.d("day", day + "");

        ViewGroup.LayoutParams lp = relativeLayout.getLayoutParams();
        lp.width = (int) (width / 1.08);
        lp.height = (int) (height / 2.1333);

        getDialog().setCancelable(true);

        startTime.setOnClickListener(onClickListener);
        endTime.setOnClickListener(onClickListener);
        set.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);

        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getTag().toString()) {

                case "startTime":
                    TimeDialog timeDialog = new TimeDialog(view, startTime);
                    timeDialog.show(getActivity().getFragmentManager(), null);
                    break;

                case "endTime":
                    timeDialog = new TimeDialog(view, startTime, endTime);
                    timeDialog.show(getActivity().getFragmentManager(), null);
                    break;

                case "set":

                    try {
                        String[] string = subjectName.getText().toString().split(" ");
                        String subject = null;

                        for (int i = 0; i < string.length; i++) {
                            if (i == 0) {
                                subject = string[i];
                            } else {
                                subject = subject + " " + string[i];
                            }
                        }

                        centreTimeTable.saveNewSubject(startTime.getText().toString(), endTime.getText().toString(), day, subject);
                        getDialog().cancel();
                        getDialog().dismiss();
                    } catch (Exception e) {
                        Log.d("error",e + "");
                        Toast.makeText(getActivity(), "Input Is Not Complete", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case "cancel":
                    getDialog().cancel();
                    getDialog().dismiss();
                    break;

            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public class HintAdapter extends ArrayAdapter<String> {


        public HintAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            // don't display last item. It is used as hint.
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }
}
