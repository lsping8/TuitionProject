package com.example.pandora.drawerlayout.Teacher;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.DataForServer.ForumData;
import com.example.pandora.drawerlayout.MaterialSharingFragment;
import com.example.pandora.drawerlayout.NoticeFragment;
import com.example.pandora.drawerlayout.PostFragment;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.SampleFragmentPagerAdapter;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Pandora on 9/9/2016.
 */
public class TeacherForum extends AppCompatActivity {

    float width,height;
    ViewPager viewpager;
    BitmapConfig bitmapConfig;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);

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
    }

    private void setupViewPager() {
        ForumData forumData = new ForumData();
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), this.getApplicationContext());
        PostFragment fragment1 = new PostFragment(forumData);
        NoticeFragment fragment2 = new NoticeFragment(forumData);
        MaterialSharingFragment fragment3 = new MaterialSharingFragment(forumData);
        adapter.addFrag(fragment1, "POSTS");
        adapter.addFrag(fragment2,"NOTICES");
        adapter.addFrag(fragment3,"Material");
        viewpager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
