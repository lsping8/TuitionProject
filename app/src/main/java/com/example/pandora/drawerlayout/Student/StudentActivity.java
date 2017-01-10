package com.example.pandora.drawerlayout.Student;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.TimeTable;
import com.example.pandora.drawerlayout.TuitionCentre.SearchClass;
import com.example.pandora.drawerlayout.TuitionCentre.TodayClass;

/**
 * Created by Pandora on 9/10/2016.
 */
public class StudentActivity extends AppCompatActivity {

    float width, height;
    String fragmentToAttach, whichPage;
    BitmapConfig bitmapConfig;
    LinearLayout bottomBtn,topLayout;
    StudentProfileFragment profileFragment;
    StudentPaymentHistory paymentHistory;
    StudentReport studentReport;
    ImageView studentImage;
    TextView studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Intent intent = getIntent();
        fragmentToAttach = intent.getExtras().getString("fragmentToAttach");

        topLayout = (LinearLayout)findViewById(R.id.topLayout);

        switch (fragmentToAttach) {

            case "profileFragment":
                bitmapConfig = new BitmapConfig();
                topLayout.setVisibility(View.VISIBLE);
                bottomBtn = (LinearLayout) findViewById(R.id.btnLayout);
                profileFragment();
                break;

            case "studentClass":
                studentClassFragment();
                break;

            case "timeTable":
                topLayout.setVisibility(View.INVISIBLE);
                setTimeTable();
                break;
        }
    }

    private void profileFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        profileFragment = new StudentProfileFragment();

        fragmentTransaction.add(R.id.student_fragment, profileFragment, "studentProfile");
        whichPage = "studentProfile";
        fragmentTransaction.commit();

        setStudentBio();
        setBottomBtn();
    }

    private void setStudentBio() {

        studentName = (EditText) findViewById(R.id.studentName);
        studentImage = (ImageView) findViewById(R.id.studentImage);

        studentImage.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.rsz_sohai, (int) width / 6, (int) width / 6), 0));
        studentName.setHint("NAME");
        studentName.setText("Haji Mohammad Najib bin Tun Haji Abdul Razak");
        studentName.setHeight((int) width / 6);

    }

    private void setBottomBtn() {

        int padding = (int) width / 6;
        ImageView me = new ImageView(this);
        ImageView payHistory = new ImageView(this);
        ImageView report = new ImageView(this);
        for (int i = 0; i < 3; i++) {
            LinearLayout linearLayout1 = new LinearLayout(this);
            TextView text = new TextView(this);
            linearLayout1.setGravity(Gravity.CENTER);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);

            switch (i) {

                case 0:
                    me.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_communities, (int) width / 12, (int) width / 12), (int) width / 12, (int) width / 12, true));
                    me.setPadding(0, padding / 8, 0, 0);
                    text.setText("Me");
                    me.setTag("me");
                    linearLayout1.addView(me);
                    me.setOnClickListener(onClickListener);
                    break;

                case 1:
                    payHistory.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_communities, (int) width / 12, (int) width / 12), (int) width / 12, (int) width / 12, true));
                    payHistory.setPadding(0, padding / 8, 0, 0);
                    text.setText("Payment History");
                    payHistory.setTag("payHistory");
                    linearLayout1.addView(payHistory);
                    payHistory.setOnClickListener(onClickListener);
                    break;

                case 2:
                    report.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_communities, (int) width / 12, (int) width / 12), (int) width / 12, (int) width / 12, true));
                    report.setPadding(0, padding / 8, 0, 0);
                    text.setText("Report");
                    report.setTag("report");
                    linearLayout1.addView(report);
                    report.setOnClickListener(onClickListener);
                    break;

            }

            text.setGravity(Gravity.CENTER);
            text.setTextColor(Color.BLACK);
            linearLayout1.addView(text);
            //inearLayout1.setPadding(padding, 0, padding, 0);
            bottomBtn.addView(linearLayout1);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getTag().toString()) {

                case "me":
                    if (whichPage.equals("studentProfile")) {
                        return;
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        detachFragment(fragmentTransaction);
                        fragmentTransaction.attach(profileFragment);
                        whichPage = "studentProfile";
                        fragmentTransaction.commit();
                    }
                    break;

                case "payHistory":
                    if (whichPage.equals("studentHistory")) {
                        return;
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (paymentHistory == null) {
                            paymentHistory = new StudentPaymentHistory();
                            detachFragment(fragmentTransaction);
                            fragmentTransaction.add(R.id.student_fragment, paymentHistory, "paymentHistory");
                        } else {
                            detachFragment(fragmentTransaction);
                            fragmentTransaction.attach(paymentHistory);
                        }
                        whichPage = "studentHistory";
                        fragmentTransaction.commit();
                    }
                    break;

                case "report":
                    if (whichPage.equals("studentReport")) {
                        return;
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (studentReport == null) {
                            studentReport = new StudentReport();
                            detachFragment(fragmentTransaction);
                            fragmentTransaction.add(R.id.student_fragment, studentReport, "studentReport");
                        } else {
                            detachFragment(fragmentTransaction);
                            fragmentTransaction.attach(studentReport);
                        }
                        whichPage = "studentReport";
                        fragmentTransaction.commit();
                    }
                    break;
            }

        }
    };

    private void detachFragment(FragmentTransaction fragmentTransaction) {
        switch (whichPage) {

            case "studentProfile":
                fragmentTransaction.detach(profileFragment);
                break;

            case "studentHistory":
                fragmentTransaction.detach(paymentHistory);
                break;

            case "studentReport":
                fragmentTransaction.detach(studentReport);
                break;
        }
    }

    private void studentClassFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        StudentClass studentClass = new StudentClass();

        fragmentTransaction.add(R.id.studentFragment, studentClass, "studentClass");
        fragmentTransaction.commit();
    }

    private void setTimeTable(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TimeTable timeTable = new TimeTable();

        fragmentTransaction.add(R.id.studentFragment, timeTable, "timeTable");
        fragmentTransaction.commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
