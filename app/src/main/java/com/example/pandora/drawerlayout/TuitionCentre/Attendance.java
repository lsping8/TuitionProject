package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/5/2016.
 */
public class Attendance extends AppCompatActivity {

    float height, width;
    BitmapConfig bitmapConfig;
    LinearLayout linearLayout;
    TodayClass todayClass;
    SearchClass searchClass;
    String whichPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();

        linearLayout = (LinearLayout) findViewById(R.id.btnLayout);
        linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle));
        setBtnLayout();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        todayClass = new TodayClass();

        fragmentTransaction.add(R.id.fragment, todayClass, "todayClass");
        whichPage = "todayClass";
        fragmentTransaction.commit();
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

    private void setBtnLayout() {
        int padding = (int) width / 6;
        ImageView today = new ImageView(this);
        ImageView all = new ImageView(this);
        for (int i = 0; i < 2; i++) {
            LinearLayout linearLayout1 = new LinearLayout(this);
            TextView text = new TextView(this);
            linearLayout1.setGravity(Gravity.CENTER);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);
            if (i % 2 == 0) {
                today.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_communities, (int) width / 12, (int) width / 12), (int) width / 12, (int) width / 12, true));
                today.setPadding(0, padding / 8, 0, 0);
                text.setText("Today");
                today.setTag("Today");
                linearLayout1.addView(today);
                today.setOnClickListener(onClickListener);
            } else {
                all.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_communities, (int) width / 12, (int) width / 12), (int) width / 12, (int) width / 12, true));
                all.setPadding(0, padding / 8, 0, 0);
                text.setText("All");
                all.setTag("All");
                linearLayout1.addView(all);
                all.setOnClickListener(onClickListener);
            }
            text.setGravity(Gravity.CENTER);
            text.setTextColor(Color.BLACK);
            linearLayout1.addView(text);
            linearLayout1.setPadding(padding, 0, padding, 0);
            linearLayout.addView(linearLayout1);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getTag().toString()) {
                case "Today":
                    if (!whichPage.equals("todayClass")) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        todayClass = new TodayClass();
                        fragmentTransaction.remove(searchClass);
                        fragmentTransaction.add(R.id.fragment, todayClass, "todayClass");
                        whichPage = "todayClass";
                        fragmentTransaction.commit();
                    }
                    break;

                case "All":
                    if (!whichPage.equals("searchClass")) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        searchClass = new SearchClass();
                        searchClass.setActivity("Attendance");
                        fragmentTransaction.remove(todayClass);
                        fragmentTransaction.add(R.id.fragment, searchClass, "searchClass");
                        whichPage = "searchClass";
                        fragmentTransaction.commit();
                    }
                    break;
            }
        }
    };

    public void refreshFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(todayClass);
        todayClass = new TodayClass();
        fragmentTransaction.add(R.id.fragment, todayClass, "todayClass");
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
