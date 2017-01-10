package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import com.example.pandora.drawerlayout.DataForServer.ReportStudentData;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.RecyclerTouchListener;

import java.util.ArrayList;

/**
 * Created by Pandora on 9/7/2016.
 */
public class ReportStudent extends AppCompatActivity {

    float height, width;
    String levelName;
    TextView title;
    EditText searchBar;
    View line;
    ArrayList<String> absenceStudent;
    ImageView notification_btn;
    BitmapConfig bitmapConfig;
    RecyclerView recyclerView;
    ReportStudentData reportStudentData;
    RecycleViewStudent adapter;
    int resultPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_notification);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Intent intent = getIntent();
        levelName = intent.getStringExtra("levelName");
        bitmapConfig = new BitmapConfig();

        reportStudentData = new ReportStudentData(width,height,bitmapConfig,this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        searchBar = (EditText) findViewById(R.id.searchBar);
        title = (TextView) findViewById(R.id.className);
        title.setText(levelName);

        int padding = (int) (width / 108);
        ViewGroup.LayoutParams lp = searchBar.getLayoutParams();
        lp.height = (int) (height / 19.2);
        lp.width = (int) (width - width / 54);

        searchBar.setPadding(padding, padding, padding, padding);
        setTopLayout();

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                resultPosition = position;
                Intent createReport = new Intent(ReportStudent.this,CreateReport.class);
                createReport.putExtra("studentName",reportStudentData.getStudentData().get(position).getStudentName());
                startActivityForResult(createReport,1);
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

    private void setTopLayout(){
        LinearLayout topLayout = (LinearLayout)findViewById(R.id.topLayout);
        ImageView userImage = (ImageView) findViewById(R.id.topPicture);
        TextView userTitle = (TextView) findViewById(R.id.topTitle);
        View padding = findViewById(R.id.padding);

        topLayout.setBackgroundColor(Color.parseColor("#00FFFF"));
        userTitle.setText("Centre");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
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

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private class RecycleViewStudent extends RecyclerView.Adapter<StudentViewHolder>{

        @Override
        public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_student_report, parent, false);
            return new StudentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(StudentViewHolder holder, int position) {
            holder.studentImage.setImageBitmap(reportStudentData.getStudentData().get(position).getStudentImage());
            holder.studentName.setText(reportStudentData.getStudentData().get(position).getStudentName());
            if (!reportStudentData.getStudentData().get(position).getTotalReport().equals("OK")) {
                holder.totalSubject.setText(reportStudentData.getStudentData().get(position).getTotalReport() + "/" + reportStudentData.getStudentData().get(position).getTotalSubject());
            }else{
                holder.totalSubject.setText(reportStudentData.getStudentData().get(position).getTotalReport());
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return reportStudentData.getStudentData().size();
        }
    }

    private class StudentViewHolder extends RecyclerView.ViewHolder{

        ImageView studentImage;
        TextView studentName,totalSubject;

        private StudentViewHolder(View itemView) {
            super(itemView);
            studentImage = (ImageView) itemView.findViewById(R.id.studentImage);
            studentName = (TextView) itemView.findViewById(R.id.studentName);
            studentName.setWidth((int)(width/1.8));
            totalSubject = (TextView) itemView.findViewById(R.id.totalSubject);
            totalSubject.setWidth((int)(width/10.8));
            totalSubject.setHeight((int)(height/19.2));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String[] result = data.getStringExtra("counterResult").split(",");
            reportStudentData.getStudentData().get(resultPosition).setTotalSubject(result[1]);
            reportStudentData.getStudentData().get(resultPosition).setTotalReport(result[0]);
            adapter.notifyItemChanged(resultPosition);
        }
    }
}
