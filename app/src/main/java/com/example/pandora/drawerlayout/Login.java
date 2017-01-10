package com.example.pandora.drawerlayout;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by Pandora on 8/30/2016.
 */
public class Login extends AppCompatActivity {

    RelativeLayout loginRelative,imageRelative;
    ViewPager viewPager;
    TabLayout tabLayout;
    float height,width;
    SignIn signIn;
    Register register;
    boolean startKeyboard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible,int heightDiff)
            {
                if (isVisible){
                    Log.d("onFocus","up");
                    moveLayout(isVisible);
                }else{
                    Log.d("onFocus","down");
                    if (!startKeyboard){
                        startKeyboard = true;
                    }else {
                        moveLayout(isVisible);
                    }
                }
            }
        });

        loginRelative = (RelativeLayout)findViewById(R.id.login_relative);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        imageRelative = (RelativeLayout)findViewById(R.id.login_image_relative);

        signIn = new SignIn();
        register = new Register();

        ViewGroup.LayoutParams lp = imageRelative.getLayoutParams();
        lp.height = (int)(height/2.9538);

        imageRelative.setBackgroundColor(Color.WHITE);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        RelativeLayout relativeLayout = (RelativeLayout)signIn.getActivity().findViewById(R.id.signInRelative);
                        relativeLayout.hasFocus();
                        break;
                    case 1:
                        relativeLayout = (RelativeLayout)register.getActivity().findViewById(R.id.registerRelative);
                        relativeLayout.hasFocus();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    private void setupViewPager() {
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), this.getApplicationContext());
        adapter.addFrag(signIn, "sign_in");
        adapter.addFrag(register, "register");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void moveLayout(boolean isVisible){
        if (isVisible){
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;
                }
            });
            loginRelative.setY(-height/2.9538f - tabLayout.getHeight());
        }else{
            viewPager.setOnTouchListener(null);
            loginRelative.setY(0);
        }
    }
}
