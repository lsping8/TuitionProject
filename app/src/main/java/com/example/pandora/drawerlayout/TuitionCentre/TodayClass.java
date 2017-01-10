package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.DataForServer.AttendanceData;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.DataForServer.SaveFile;
import com.example.pandora.drawerlayout.RecyclerTouchListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Pandora on 9/4/2016.
 */

public class TodayClass extends Fragment {

    RecyclerView comingClass, history, currentClass;
    SwipeRefreshLayout swipeRefreshLayout;
    float height, width;
    View view;
    SharedPreferences mPrefs;
    SaveFile saveFile;
    int totalClass;
    Handler handler;
    int outdatedMinuteData = -1;
    ArrayList<AttendanceData> currentClasses, comingClasses, historyClasses;
    CurrentRecycleView currentAdapter;
    ComingRecycleView comingAdapter;
    HistoryRecycleView historyAdapter;
    String whichClass;
    int resultPosition;
    SaveFile.SaveClassDay.SaveTimeTable saveTimeTable;

    public TodayClass() {
        currentClasses = new ArrayList<>();
        comingClasses = new ArrayList<>();
        historyClasses = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_today_class, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        handler = new Handler();
        loadSaveFile();

        comingClass = (RecyclerView) view.findViewById(R.id.comingRecycleView);
        history = (RecyclerView) view.findViewById(R.id.historyRecycleView);
        currentClass = (RecyclerView) view.findViewById(R.id.currentRecycleView);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Attendance attendance = (Attendance) getActivity();
                attendance.refreshFragment();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        setClass();

        currentClass.setNestedScrollingEnabled(false);
        currentAdapter = new CurrentRecycleView();
        currentClass.setAdapter(currentAdapter);
        currentClass.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator1 = new DefaultItemAnimator();
        itemAnimator1.setAddDuration(1000);
        currentClass.setItemAnimator(itemAnimator1);

        int height = calculateHeight(comingClasses.size());
        ViewGroup.LayoutParams params = comingClass.getLayoutParams();
        params.height = height;
        comingClass.setNestedScrollingEnabled(false);
        comingAdapter = new ComingRecycleView();
        comingClass.setAdapter(comingAdapter);
        comingClass.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator2 = new DefaultItemAnimator();
        itemAnimator2.setAddDuration(1000);
        comingClass.setItemAnimator(itemAnimator2);

        height = calculateHeight(historyClasses.size());
        params = history.getLayoutParams();
        params.height = height;
        history.setNestedScrollingEnabled(false);
        historyAdapter = new HistoryRecycleView();
        history.setAdapter(historyAdapter);
        history.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        history.setItemAnimator(itemAnimator);

        return view;
    }

    private int calculateHeight(int size) {
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        int singleViewHeight = (int) (130 * scale + 0.5f);
        return singleViewHeight * size;
    }

    private void setClass() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        final String dayOfTheWeek = sdf.format(d);

        for (int i = 0; i < 6; i++) {
            String level = "STANDARD " + (i + 1);
            int classSize = saveFile.getClassDetailSize(dayOfTheWeek, level);
            totalClass = totalClass + classSize;
        }

        for (int i = 0; i < 5; i++) {
            String level = "FORM " + (i + 1);
            int classSize = saveFile.getClassDetailSize(dayOfTheWeek, level);
            totalClass = totalClass + classSize;
        }

        sortTitle(dayOfTheWeek);
    }

    private void sortTitle(String day) {

        int outDatedStartTime = 0;
        do {
            int latestStartTime = 0;
            boolean starting = true;
            for (int i = 0; i < 6; i++) {
                String level = "STANDARD " + (i + 1);
                for (int j = 0; j < saveFile.getClassDetailSize(day, level); j++) {
                    saveTimeTable = saveFile.getPerDayClass(level, day).get(j);
                    String[] temp1 = saveTimeTable.getStartTime().split(" ");
                    int currentStartTime = Integer.parseInt(temp1[0] + temp1[2]);

                    if (!temp1[0].equals("12") && temp1[3].equals("PM")) {
                        currentStartTime += 1200;
                    }

                    if (starting) {
                        if (currentStartTime > outDatedStartTime) {
                            latestStartTime = currentStartTime;
                            starting = false;
                        }
                    }

                    if (latestStartTime > currentStartTime && currentStartTime > outDatedStartTime) {
                        latestStartTime = currentStartTime;
                    }

                    Log.d("latestStartTime", latestStartTime + "");
                }
            }

            for (int i = 0; i < 5; i++) {
                String level = "FORM " + (i + 1);
                for (int j = 0; j < saveFile.getClassDetailSize(day, level); j++) {
                    saveTimeTable = saveFile.getPerDayClass(level, day).get(j);
                    String[] temp1 = saveTimeTable.getStartTime().split(" ");

                    int currentStartTime = Integer.parseInt(temp1[0] + temp1[2]);

                    if (!temp1[0].equals("12") && temp1[3].equals("PM")) {
                        currentStartTime += 1200;
                    }

                    if (latestStartTime > currentStartTime && currentStartTime > outDatedStartTime) {
                        latestStartTime = currentStartTime;
                    }

                    Log.d("latestStartTime", latestStartTime + "");
                }
            }

            setTitle(day, latestStartTime);
            outDatedStartTime = latestStartTime;

            Log.d("outDatedStartTime", outDatedStartTime + "");
        }
        while (currentClasses.size() + comingClasses.size() + historyClasses.size() != totalClass);

    }

    private void setTitle(String day, int latestStartTime) {

        String subjectName, startTime, endTime;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));

        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH : mm a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        String localTime = date.format(currentLocalTime);
        String[] splitLocalTime = localTime.split(" ");
        int localTM = Integer.parseInt(splitLocalTime[0] + splitLocalTime[2]);

        for (int i = 0; i < 6; i++) {
            String level = "STANDARD " + (i + 1);
            for (int j = 0; j < saveFile.getClassDetailSize(day, level); j++) {
                saveTimeTable = saveFile.getPerDayClass(level, day).get(j);
                String[] splitStartTimeString = saveTimeTable.getStartTime().split(" ");
                String[] splitEndTimeString = saveTimeTable.getEndTime().split(" ");
                int tempStartTime = Integer.parseInt(splitStartTimeString[0] + splitStartTimeString[2]);
                int tempEndTime = Integer.parseInt(splitEndTimeString[0] + splitEndTimeString[2]);

                if (!splitStartTimeString[0].equals("12") && splitStartTimeString[3].equals("PM")) {
                    tempStartTime += 1200;
                }

                if (!splitEndTimeString[0].equals("12") && splitEndTimeString[3].equals("PM")) {
                    tempEndTime += 1200;
                }

                if (latestStartTime == tempStartTime) {
                    startTime = saveTimeTable.getStartTime();
                    endTime = saveTimeTable.getEndTime();
                    subjectName = saveTimeTable.getSubjectName();

                    if (localTM > latestStartTime && localTM > tempEndTime) {
                        setGridLayout(startTime, endTime, level, subjectName, "History");
                    } else if (localTM > latestStartTime && localTM < tempEndTime) {
                        setGridLayout(startTime, endTime, level, subjectName, "Current Class");
                    } else if (localTM < latestStartTime) {
                        setGridLayout(startTime, endTime, level, subjectName, "Coming Class");
                    }
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            String level = "FORM " + (i + 1);
            for (int j = 0; j < saveFile.getClassDetailSize(day, level); j++) {
                saveTimeTable = saveFile.getPerDayClass(level, day).get(j);
                String[] splitStartTimeString = saveTimeTable.getStartTime().split(" ");
                String[] splitEndTimeString = saveTimeTable.getEndTime().split(" ");
                int tempStartTime = Integer.parseInt(splitStartTimeString[0] + splitStartTimeString[2]);
                int tempEndTime = Integer.parseInt(splitEndTimeString[0] + splitEndTimeString[2]);

                if (!splitStartTimeString[0].equals("12") && splitStartTimeString[3].equals("PM")) {
                    tempStartTime += 1200;
                }

                if (!splitEndTimeString[0].equals("12") && splitEndTimeString[3].equals("PM")) {
                    tempEndTime += 1200;
                }

                if (latestStartTime == tempStartTime) {
                    startTime = saveTimeTable.getStartTime();
                    endTime = saveTimeTable.getEndTime();
                    subjectName = saveTimeTable.getSubjectName();

                    if (localTM > latestStartTime && localTM > tempEndTime) {
                        setGridLayout(startTime, endTime, level, subjectName, "History");
                    } else if (localTM > latestStartTime && localTM < tempEndTime) {
                        setGridLayout(startTime, endTime, level, subjectName, "Current Class");
                    } else if (localTM < latestStartTime) {
                        setGridLayout(startTime, endTime, level, subjectName, "Coming Class");
                    }
                }
            }
        }
    }

    private void setGridLayout(final String startTime, final String endTime, final String level, final String subjectName, final String instruction) {

        switch (instruction) {

            case "Current Class":
                currentClasses.add(new AttendanceData(level + "," + subjectName, startTime + " - " + endTime, "105/105", "0"));
                break;

            case "Coming Class":
                comingClasses.add(new AttendanceData(level + "," + subjectName, startTime + " - " + endTime, "105/105", "0"));
                break;

            case "History":
                historyClasses.add(new AttendanceData(level + "," + subjectName, startTime + " - " + endTime, "105/105", "0"));
                break;
        }
    }

    private void loadSaveFile() {
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        saveFile = gson.fromJson(json, SaveFile.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public class CurrentRecycleView extends RecyclerView.Adapter<CurrentViewHolder> {

        @Override
        public CurrentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_current, parent, false);
            return new CurrentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CurrentViewHolder holder, int position) {
            holder.textName.setText(currentClasses.get(position).getClassName());
            holder.textTime.setText(currentClasses.get(position).getClassTime());
            holder.textTotalStudent.setText(currentClasses.get(position).getClassStudent());
            holder.textCounter.setText(currentClasses.get(position).getCounter());
            holder.setTag(position);
        }

        @Override
        public int getItemCount() {
            return currentClasses.size();
        }
    }

    public class CurrentViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textTime, textTotalStudent, redCircle, blueCircle, textCounter;
        RelativeLayout blueLayout;

        public CurrentViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.className);
            textTime = (TextView) itemView.findViewById(R.id.classTime);
            textTotalStudent = (TextView) itemView.findViewById(R.id.classStudent);
            redCircle = (TextView) itemView.findViewById(R.id.redCircle);
            blueCircle = (TextView) itemView.findViewById(R.id.blueCircle);
            textCounter = (TextView) itemView.findViewById(R.id.textCounter);
            blueLayout = (RelativeLayout) itemView.findViewById(R.id.blueLayout);
            blueLayout.setX(width / 24);
            textCounter.setY(height / 96);
        }

        public void setTag(int position) {
            blueCircle.setTag("blue_circle");
            blueCircle.setId(position);
            blueCircle.setOnClickListener(onClickListener);
            redCircle.setTag("red_circle");
            redCircle.setId(position);
            redCircle.setOnClickListener(onClickListener);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getTag().toString()) {

                    case "blue_circle":
                        String className = currentClasses.get(view.getId()).getClassName() + "," + "blue" + "," + currentClasses.get(view.getId()).getCounter();
                        resultPosition = view.getId();
                        whichClass = "currentClass";
                        Intent attendance_as_blue = new Intent(getActivity(), AttendanceNotification.class);
                        attendance_as_blue.putExtra("className", className);
                        startActivityForResult(attendance_as_blue, 1);
                        break;

                    case "red_circle":
                        className = currentClasses.get(view.getId()).getClassName() + "," + "red" + "," + "red";
                        resultPosition = view.getId();
                        whichClass = "currentClass";
                        Intent attendance_as_red = new Intent(getActivity(), AttendanceNotification.class);
                        attendance_as_red.putExtra("className", className);
                        startActivityForResult(attendance_as_red, 1);
                        break;
                }
            }
        };
    }

    public class ComingRecycleView extends RecyclerView.Adapter<ComingViewHolder> {

        @Override
        public ComingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_coming, parent, false);
            return new ComingViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ComingViewHolder holder, int position) {
            holder.textName.setText(comingClasses.get(position).getClassName());
            holder.textTime.setText(comingClasses.get(position).getClassTime());
            holder.textTotalStudent.setText(comingClasses.get(position).getClassStudent());
            holder.textCounter.setText(comingClasses.get(position).getCounter());
            holder.setTag(position);
        }

        @Override
        public int getItemCount() {
            return comingClasses.size();
        }
    }

    public class ComingViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textTime, textTotalStudent, redCircle, blueCircle, textCounter;
        RelativeLayout blueLayout;

        public ComingViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.className);
            textTime = (TextView) itemView.findViewById(R.id.classTime);
            textTotalStudent = (TextView) itemView.findViewById(R.id.classStudent);
            redCircle = (TextView) itemView.findViewById(R.id.redCircle);
            blueCircle = (TextView) itemView.findViewById(R.id.blueCircle);
            textCounter = (TextView) itemView.findViewById(R.id.textCounter);
            blueLayout = (RelativeLayout) itemView.findViewById(R.id.blueLayout);
            blueLayout.setX(width / 24);
            textCounter.setY(height / 96);
        }

        public void setTag(int position) {
            blueCircle.setTag("blue_circle");
            blueCircle.setId(position);
            blueCircle.setOnClickListener(onClickListener);
            redCircle.setTag("red_circle");
            redCircle.setId(position);
            redCircle.setOnClickListener(onClickListener);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getTag().toString()) {

                    case "blue_circle":
                        String className = comingClasses.get(view.getId()).getClassName() + "," + "blue" + "," + comingClasses.get(view.getId()).getCounter();
                        resultPosition = view.getId();
                        whichClass = "comingClass";
                        Intent attendance_as_blue = new Intent(getActivity(), AttendanceNotification.class);
                        attendance_as_blue.putExtra("className", className);
                        startActivityForResult(attendance_as_blue, 1);
                        break;

                    case "red_circle":
                        className = comingClasses.get(view.getId()).getClassName() + "," + "red" + "," + "red";
                        resultPosition = view.getId();
                        whichClass = "comingClass";
                        Intent attendance_as_red = new Intent(getActivity(), AttendanceNotification.class);
                        attendance_as_red.putExtra("className", className);
                        startActivityForResult(attendance_as_red, 1);
                        break;
                }
            }
        };
    }

    public class HistoryRecycleView extends RecyclerView.Adapter<HistoryViewHolder> {

        @Override
        public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_history, parent, false);
            return new HistoryViewHolder(v);
        }

        @Override
        public void onBindViewHolder(HistoryViewHolder holder, int position) {
            holder.textName.setText(historyClasses.get(position).getClassName());
            holder.textTime.setText(historyClasses.get(position).getClassTime());
            holder.textTotalStudent.setText(historyClasses.get(position).getClassStudent());
            holder.textCounter.setText(historyClasses.get(position).getCounter());
            holder.setTag(position);
        }

        @Override
        public int getItemCount() {
            return historyClasses.size();
        }
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textTime, textTotalStudent, redCircle, blueCircle, textCounter;
        RelativeLayout blueLayout;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.className);
            textTime = (TextView) itemView.findViewById(R.id.classTime);
            textTotalStudent = (TextView) itemView.findViewById(R.id.classStudent);
            redCircle = (TextView) itemView.findViewById(R.id.redCircle);
            blueCircle = (TextView) itemView.findViewById(R.id.blueCircle);
            textCounter = (TextView) itemView.findViewById(R.id.textCounter);
            blueLayout = (RelativeLayout) itemView.findViewById(R.id.blueLayout);
            blueLayout.setX(width / 24);
            textCounter.setY(height / 96);
        }

        public void setTag(int position) {
            blueCircle.setTag("blue_circle");
            blueCircle.setId(position);
            blueCircle.setOnClickListener(onClickListener);
            redCircle.setTag("red_circle");
            redCircle.setId(position);
            redCircle.setOnClickListener(onClickListener);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getTag().toString()) {

                    case "blue_circle":
                        String className = historyClasses.get(view.getId()).getClassName() + "," + "blue" + "," + historyClasses.get(view.getId()).getCounter();
                        ;
                        resultPosition = view.getId();
                        whichClass = "history";
                        Intent attendance_as_blue = new Intent(getActivity(), AttendanceNotification.class);
                        attendance_as_blue.putExtra("className", className);
                        startActivityForResult(attendance_as_blue, 1);
                        break;

                    case "red_circle":
                        className = historyClasses.get(view.getId()).getClassName() + "," + "red" + "," + "red";
                        resultPosition = view.getId();
                        whichClass = "history";
                        Intent attendance_as_red = new Intent(getActivity(), AttendanceNotification.class);
                        attendance_as_red.putExtra("className", className);
                        startActivityForResult(attendance_as_red, 1);
                        break;
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("requestCode", requestCode + "");
        Log.d("resultCode", whichClass + "");

        if (resultCode == Activity.RESULT_OK) {

            switch (whichClass) {

                case "currentClass":
                    String intentData = data.getStringExtra("counterResult");
                    currentClasses.get(resultPosition).setCounter(intentData);
                    currentAdapter.notifyItemChanged(resultPosition);
                    break;

                case "comingClass":
                    intentData = data.getStringExtra("counterResult");
                    Log.d("intentData", intentData + "");
                    Log.d("intentData", resultPosition + "");
                    comingClasses.get(resultPosition).setCounter(intentData);
                    comingAdapter.notifyItemChanged(resultPosition);
                    break;

                case "history":
                    intentData = data.getStringExtra("counterResult");
                    historyClasses.get(resultPosition).setCounter(intentData);
                    historyAdapter.notifyItemChanged(resultPosition);
                    break;
            }
        }
    }

}
