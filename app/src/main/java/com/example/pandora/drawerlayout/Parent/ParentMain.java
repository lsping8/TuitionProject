package com.example.pandora.drawerlayout.Parent;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.SampleFragmentPagerAdapter;
import com.example.pandora.drawerlayout.Teacher.TeacherHome1;
import com.example.pandora.drawerlayout.Teacher.TeacherHome2;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Pandora on 9/10/2016.
 */
public class ParentMain extends AppCompatActivity {

    float width,height;
    RelativeLayout relativeLayout;
    ViewPager viewpager;
    LinearLayout linearLayout;
    BitmapConfig bitmapConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();
        relativeLayout = (RelativeLayout)findViewById(R.id.relative);
        linearLayout = (LinearLayout)findViewById(R.id.bottomLinear);
        linearLayout.setBackground(ContextCompat.getDrawable(this,R.drawable.rectangle));
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        setupViewPager();
        indicator.setViewPager(viewpager);
    }

    private void setupViewPager() {
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), this.getApplicationContext());
        ParentHome1 home1 = new ParentHome1();
        ParentHome2 home2 = new ParentHome2();
        adapter.addFrag(home1, "");
        adapter.addFrag(home2,"");
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
