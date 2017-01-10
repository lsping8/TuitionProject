package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.DataForServer.ForumData;
import com.example.pandora.drawerlayout.MaterialSharingFragment;
import com.example.pandora.drawerlayout.NoticeFragment;
import com.example.pandora.drawerlayout.PostFragment;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.SampleFragmentPagerAdapter;

/**
 * Created by Pandora on 9/25/2016.
 */

public class CentreForum extends AppCompatActivity {

    float width, height;
    ViewPager viewpager;
    TabLayout tabLayout;
    BitmapConfig bitmapConfig;
    String level, subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Intent intent = getIntent();
        String[] string = intent.getExtras().getString("detail").split(" ");
        level = string[0] + " " + string[1];
        Log.d("level", level);
        for (int i = 2; i < string.length; i++) {
            if (i == 2) {
                subjectName = string[i];
            } else {
                subjectName = subjectName + " " + string[i];
            }
        }
        Log.d("subjectName", subjectName);

        bitmapConfig = new BitmapConfig();

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager();
        tabLayout.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        setTopLayout();
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

    private void setupViewPager() {
        ForumData forumData = new ForumData();
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        PostFragment fragment1 = new PostFragment(forumData);
        NoticeFragment fragment2 = new NoticeFragment(forumData);
        MaterialSharingFragment fragment3 = new MaterialSharingFragment(forumData);
        adapter.addFrag(fragment1, "POSTS");
        adapter.addFrag(fragment2, "NOTICES");
        adapter.addFrag(fragment3, "Material");
        viewpager.setAdapter(adapter);
    }
}
