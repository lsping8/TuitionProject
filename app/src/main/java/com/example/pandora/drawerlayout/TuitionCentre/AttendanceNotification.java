package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.DataForServer.StudentInClassData;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.RecyclerTouchListener;

import java.util.ArrayList;

/**
 * Created by Pandora on 9/5/2016.
 */
public class AttendanceNotification extends AppCompatActivity {

    float height, width;
    String className, whichMode, blueCounter;
    TextView title;
    EditText searchBar;
    ArrayList<String> absenceStudent;
    ImageView notification_btn;
    BitmapConfig bitmapConfig;
    RecyclerView recyclerView;
    RecycleViewStudent adapter;
    StudentInClassData studentInClassData;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_notification);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Intent intent = getIntent();
        String[] intentString = intent.getStringExtra("className").split(",");
        className = intentString[0] + "," + intentString[1];
        whichMode = intentString[2];
        blueCounter = intentString[3];

        bitmapConfig = new BitmapConfig();

        if (whichMode.equals("red")) {
            studentInClassData = new StudentInClassData(width, height, bitmapConfig, this);
        } else {
            studentInClassData = new StudentInClassData(width, height, bitmapConfig, this, blueCounter);
            counter = Integer.parseInt(blueCounter);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        searchBar = (EditText) findViewById(R.id.searchBar);
        notification_btn = (ImageView) findViewById(R.id.notification_btn);
        title = (TextView) findViewById(R.id.className);
        title.setText(className);

        int padding = (int) (width / 108);
        ViewGroup.LayoutParams lp = searchBar.getLayoutParams();
        lp.height = (int) (height / 19.2);
        lp.width = (int) (width - width / 54);

        searchBar.setPadding(padding, padding, padding, padding);
        setNotification_btn();
        setTopLayout();

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        adapter = new RecycleViewStudent();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setChangeDuration(100);
        recyclerView.setItemAnimator(itemAnimator);
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

    private void setNotification_btn() {
        notification_btn.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.notification_btn, (int) width, (int) (height / 9.6)), (int) width, (int) (height / 9.6), true));
        notification_btn.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (whichMode.equals("red")) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("counterResult", counter + "");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("counterResult", counter + "");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private class RecycleViewStudent extends RecyclerView.Adapter<StudentViewHolder> {

        @Override
        public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_student, parent, false);
            return new StudentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(StudentViewHolder holder, int position) {
            holder.setTag(position);
            holder.studentImage.setImageBitmap(studentInClassData.getStudentInfo().get(position).getStudentImage());
            holder.studentName.setText(studentInClassData.getStudentInfo().get(position).getStudentName());
            holder.checkBox.setChecked(studentInClassData.getStudentInfo().get(position).getCheck());
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return studentInClassData.getStudentInfo().size();
        }

        public void dataChange(int position) {
            notifyItemChanged(position);
        }
    }

    private class StudentViewHolder extends RecyclerView.ViewHolder {

        ImageView studentImage;
        TextView studentName;
        CheckBox checkBox;
        LinearLayout linearLayout;

        private StudentViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear);
            linearLayout.setOnClickListener(onClickListener);
            studentImage = (ImageView) itemView.findViewById(R.id.studentImage);
            studentName = (TextView) itemView.findViewById(R.id.studentName);
            studentName.setWidth((int) (width / 1.5428));
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = v.getId();

                if (whichMode.equals("red")) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        studentInClassData.getStudentInfo().get(position).setCheck(false);
                        counter--;
                    } else {
                        checkBox.setChecked(true);
                        studentInClassData.getStudentInfo().get(position).setCheck(true);
                        counter++;
                    }
                } else {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        studentInClassData.getStudentInfo().get(position).setCheck(false);
                        counter++;
                    } else {
                        checkBox.setChecked(true);
                        studentInClassData.getStudentInfo().get(position).setCheck(true);
                        counter--;
                    }
                }
            }
        };

        public void setTag(int position) {
            linearLayout.setId(position);
        }
    }
}
