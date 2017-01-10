package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.DataForServer.SaveFile;
import com.example.pandora.drawerlayout.TimeTable;
import com.example.pandora.drawerlayout.UniversalDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Pandora on 9/12/2016.
 */
public class CentreTimeTable extends AppCompatActivity {

    float width, height;
    LinearLayout bottomLayout, timeTable;
    BitmapConfig bitmapConfig;
    LinearLayout.LayoutParams params;
    String level;
    TimeTable timeTableFragment;
    boolean isAttach = false;
    boolean loadingData = false;
    SaveFile saveFile;
    SharedPreferences mPrefs;
    FrameLayout frameLayout;
    Handler handler;
    Context context;
    SaveFile.SaveClassDay.SaveTimeTable saveTimeTable;
    int outdatedMinuteData = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_management);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Intent intent = getIntent();
        level = intent.getStringExtra("levelName");
        TextView levelName = (TextView) findViewById(R.id.level);
        levelName.setText(level);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        context = this;
        handler = new Handler();

        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        timeTable = (LinearLayout) findViewById(R.id.timeTable);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        ViewGroup.LayoutParams lp = frameLayout.getLayoutParams();
        lp.height = (int) (height - height / 3.2);

        int margin = (int) (height / 38.4);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, margin, 0, 0);

        setDays();
        setBottomLayout();
        retrieveSaveData();
        setTopLayout();
    }

    private void setTopLayout() {
        LinearLayout topLayout = (LinearLayout) findViewById(R.id.topLayout);
        ImageView userImage = (ImageView) findViewById(R.id.topPicture);
        TextView userTitle = (TextView) findViewById(R.id.topTitle);
        View padding = findViewById(R.id.padding);

        topLayout.setBackgroundColor(Color.parseColor("#00FFFF"));
        userTitle.setText("Centre");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int statusBarHeight = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            View view = new View(this);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.getLayoutParams().height = statusBarHeight;
            ViewGroup.LayoutParams lp = padding.getLayoutParams();
            lp.height = statusBarHeight;
            ((ViewGroup) w.getDecorView()).addView(view);
            view.setBackgroundColor(Color.parseColor("#008b8b"));
        }
    }

    private void setDays() {

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (int i = 0; i < 7; i++) {

            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout subjectLinear = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            subjectLinear.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.round_edge_rectangle));
            linearLayout.setLayoutParams(params);
            linearLayout.setTag(days[i]);

            TextView day = new TextView(this);
            TextView line = new TextView(this);

            day.setTextColor(Color.BLACK);
            day.setText(days[i]);
            day.setTypeface(Typeface.DEFAULT_BOLD);
            day.setPadding((int) width / 54, 0, 0, 0);
            linearLayout.addView(day);

            line.setBackgroundColor(Color.BLACK);
            line.setWidth((int) width);
            line.setHeight((int) (height / 384));
            linearLayout.addView(line);

            LinearLayout label = new LinearLayout(this);
            label.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.addView(label);
            setLabel(label);
            linearLayout.addView(subjectLinear);

            timeTable.addView(linearLayout);
            linearLayout.setClickable(true);
        }
    }

    private void setLabel(LinearLayout label) {
        TextView time = new TextView(this);
        TextView level = new TextView(this);
        TextView subject = new TextView(this);

        for (int i = 0; i < 3; i++) {
            TextView line = new TextView(this);
            line.setBackgroundColor(Color.BLACK);
            line.setWidth((int) width / 216);
            line.setHeight((int) height / 24);

            switch (i) {

                case 0:
                    time.setTextColor(Color.BLACK);
                    time.setText("Start Time");
                    time.setTypeface(Typeface.DEFAULT_BOLD);
                    time.setGravity(Gravity.CENTER);
                    time.setHeight((int) height / 24);
                    time.setWidth((int) (width / 4.32));
                    label.addView(time);
                    label.addView(line);
                    break;

                case 1:
                    level.setTextColor(Color.BLACK);
                    level.setText("End Time");
                    level.setTypeface(Typeface.DEFAULT_BOLD);
                    level.setGravity(Gravity.CENTER);
                    level.setHeight((int) height / 24);
                    level.setWidth((int) (width / 4.32));
                    label.addView(level);
                    label.addView(line);
                    break;

                case 2:
                    subject.setTextColor(Color.BLACK);
                    subject.setText("Subject");
                    subject.setTypeface(Typeface.DEFAULT_BOLD);
                    subject.setGravity(Gravity.CENTER);
                    subject.setHeight((int) height / 24);
                    subject.setWidth((int) (width / 1.8620));
                    label.addView(subject);
                    break;
            }
        }
    }

    private void setBottomLayout() {

        Button addSubject = new Button(this);
        Button viewTime = new Button(this);

        int margin = (int) (width / 10.8);
        LinearLayout.LayoutParams paramLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramLayout.setMargins(margin, 0, 0, 0);

        addSubject.setText("Add Subject");
        addSubject.setTextColor(Color.BLACK);
        addSubject.setWidth((int) width / 3);
        addSubject.setHeight((int) (height / 9.6));
        addSubject.setTag("Add Subject");
        addSubject.setBackground(ContextCompat.getDrawable(this, R.drawable.round_edge_rectangle));
        addSubject.setOnClickListener(onClickListener);

        viewTime.setText("View TimeTable");
        viewTime.setTextColor(Color.BLACK);
        viewTime.setWidth((int) width / 3);
        viewTime.setHeight((int) (height / 9.6));
        viewTime.setTag("View TimeTable");
        viewTime.setBackground(ContextCompat.getDrawable(this, R.drawable.round_edge_rectangle));
        viewTime.setLayoutParams(paramLayout);
        viewTime.setOnClickListener(onClickListener);

        bottomLayout.addView(addSubject);
        bottomLayout.addView(viewTime);
        bottomLayout.setY(height - height / 3.84f);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getTag().toString()) {

                case "Add Subject":
                    AddSubjectDialog addSubjectDialog = new AddSubjectDialog();
                    addSubjectDialog.show(getFragmentManager(), null);
                    break;

                case "View TimeTable":

                    RelativeLayout fragment = (RelativeLayout) findViewById(R.id.timeFragment);
                    fragment.bringToFront();

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    timeTableFragment = new TimeTable(CentreTimeTable.this, saveFile);

                    fragmentTransaction.add(R.id.timeFragment, timeTableFragment, "timeTable");
                    fragmentTransaction.commit();
                    isAttach = true;

                    break;
            }
        }
    };

    public void saveNewSubject(String startTime, String endTime, String day, String subjectName) {

        if (saveFile.getClassDetailSize(day, level) != 0) {

            String[] start = startTime.split(" ");
            String[] end = endTime.split(" ");
            int startNumber = Integer.parseInt(start[0] + start[2]);
            int endNumber = Integer.parseInt(end[0] + end[2]);
            int position = -1;
            boolean bestEndTime = false;

            if (!start[0].equals("12") && start[3].equals("PM")) {
                startNumber += 1200;
            }

            if (!end[0].equals("12") && end[3].equals("PM")) {
                endNumber += 1200;
            }

            int saveStartNum = 0, saveEndNum = 0;

            for (int i = 0; i < saveFile.getClassDetailSize(day, level); i++) {
                saveTimeTable = saveFile.getPerDayClass(level, day).get(i);
                String[] saveStart = saveTimeTable.getStartTime().split(" ");
                String[] saveEnd = saveTimeTable.getEndTime().split(" ");
                saveStartNum = Integer.parseInt(saveStart[0] + saveStart[2]);
                saveEndNum = Integer.parseInt(saveEnd[0] + saveEnd[2]);

                if (!saveStart[0].equals("12") && saveStart[3].equals("PM")) {
                    saveStartNum += 1200;
                }

                if (!saveEnd[0].equals("12") && saveEnd[3].equals("PM")) {
                    saveEndNum += 1200;
                }

                Log.d("startNumber",startNumber + "");
                Log.d("saveStartNum",saveStartNum + "");
                Log.d("endNumber",endNumber + "");
                Log.d("saveEndNum",saveEndNum + "");


                if (startNumber == saveStartNum) {
                    if (endNumber < saveEndNum && !bestEndTime) {
                        position = i;
                        bestEndTime = true;
                    }
                } else {
                    if (saveStartNum < startNumber && !bestEndTime) {
                        position = i+1;
                    }
                }

                Log.d("position",position + "");

            }

            saveFile.setClassDetail(startTime, endTime, subjectName, day, level, position);

            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(saveFile);
            prefsEditor.putString("MyObject", json);
            prefsEditor.apply();

            sortClass(day);

        } else {
            saveFile.setClassDetail(startTime, endTime, subjectName, day, level, 0);

            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(saveFile);
            prefsEditor.putString("MyObject", json);
            prefsEditor.apply();

            sortClass(day);
        }
    }

    public void addNewSubject(String day) {

        for (int i = 0; i < saveFile.getClassDetailSize(day, level); i++) {

            saveTimeTable = saveFile.getPerDayClass(level, day).get(i);

            LinearLayout dayLinear = (LinearLayout) timeTable.findViewWithTag(day);
            LinearLayout linearLayout = (LinearLayout) dayLinear.getChildAt(3);
            LinearLayout subjectLayout = new LinearLayout(context);
            subjectLayout.setGravity(Gravity.CENTER_VERTICAL);

            subjectLayout.setOnTouchListener(onTouchListener);
            subjectLayout.setOnLongClickListener(onLongClickListener);

            subjectLayout.setBackground(ContextCompat.getDrawable(CentreTimeTable.this, R.drawable.item_select));

            subjectLayout.setTag(i + "");

            TextView time = new TextView(context);
            TextView level = new TextView(context);
            TextView subject = new TextView(context);
            TextView border = new TextView(context);

            border.setBackgroundColor(Color.BLACK);
            border.setWidth((int) width);
            border.setHeight((int) (height / 384));
            linearLayout.addView(border);
            linearLayout.addView(subjectLayout);

            for (int j = 0; j < 3; j++) {
                TextView line = new TextView(context);
                line.setBackgroundColor(Color.BLACK);
                line.setWidth((int) (width / 216));
                line.setHeight((int) (height / 24));

                switch (j) {

                    case 0:
                        time.setTextColor(Color.BLACK);
                        time.setText(saveTimeTable.getStartTime());
                        time.setGravity(Gravity.CENTER);
                        time.setHeight((int) height / 24);
                        time.setWidth((int) (width / 4.32));
                        subjectLayout.addView(time);
                        subjectLayout.addView(line);
                        break;

                    case 1:
                        level.setTextColor(Color.BLACK);
                        level.setText(saveTimeTable.getEndTime());
                        level.setGravity(Gravity.CENTER);
                        level.setHeight((int) height / 24);
                        level.setWidth((int) (width / 4.32));
                        subjectLayout.addView(level);
                        subjectLayout.addView(line);
                        break;

                    case 2:
                        subject.setTextColor(Color.BLACK);
                        subject.setText(saveTimeTable.getSubjectName());
                        subject.setGravity(Gravity.CENTER);
                        subject.setHeight((int) height / 24);
                        subject.setWidth((int) (width / 1.8620));
                        subjectLayout.addView(subject);
                        break;
                }
            }
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    v.setPressed(true);
                    Log.d("down", "down");
                    break;

                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    Log.d("up", "up");
                    break;

            }

            return false;
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            LinearLayout linearLayout = (LinearLayout) v;
            LinearLayout parent = (LinearLayout) v.getParent();
            LinearLayout parentDay = (LinearLayout) parent.getParent();

            Log.d("KEY_LONG", v.getTag().toString());

            String day = ((TextView) parentDay.getChildAt(0)).getText().toString();
            String startTime = ((TextView) linearLayout.getChildAt(0)).getText().toString();
            String endTime = ((TextView) linearLayout.getChildAt(2)).getText().toString();
            String Subject = ((TextView) linearLayout.getChildAt(4)).getText().toString();
            String description = Subject + "\n" + startTime + " - " + endTime;

            Vibrator vib = (Vibrator) CentreTimeTable.this.getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(100);
            UniversalDialog universalDialog = new UniversalDialog(CentreTimeTable.this, description, v.getTag().toString(), day, linearLayout, "timeTable");
            universalDialog.show(getFragmentManager(), null);

            return false;
        }
    };

    public void deleteData(String key, String day, LinearLayout linearLayout) {

        LinearLayout linearLayout1 = (LinearLayout) timeTable.findViewWithTag(day);
        LinearLayout dayLayout = (LinearLayout) linearLayout1.getChildAt(3);
        for (int i = 0; i < dayLayout.getChildCount(); i++) {
            if (dayLayout.getChildAt(i) == linearLayout) {
                dayLayout.removeView(dayLayout.getChildAt(i - 1));
            }
        }
        dayLayout.removeView(linearLayout);

        saveFile.getPerDayClass(level, day).remove(Integer.parseInt(key));

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(saveFile);
        prefsEditor.putString("MyObject", json);
        prefsEditor.apply();
    }

    private void retrieveSaveData() {

        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        saveFile = gson.fromJson(json, SaveFile.class);
        loadSaveFile();
    }

    private void loadSaveFile() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        loadingData = true;
        for (final String day : days) {
            addNewSubject(day);
        }
        loadingData = false;
    }

    public void sortClass(final String day) {
        LinearLayout dayLinear = (LinearLayout) timeTable.findViewWithTag(day);
        LinearLayout linearLayout = (LinearLayout) dayLinear.getChildAt(3);
        linearLayout.removeAllViews();
        addNewSubject(day);
    }

    @Override
    public void onBackPressed() {
        if (isAttach) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(timeTableFragment);
            fragmentTransaction.commit();
            isAttach = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
