package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/24/2016.
 */

public class ForumActivity extends AppCompatActivity{

    float width,height;
    SearchClass searchClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        searchClass = new SearchClass();
        searchClass.setActivity("forum");

        fragmentTransaction.add(R.id.fragment, searchClass, "searchClass");
        fragmentTransaction.commit();
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

    public void setLevelClass(String level){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CentreTotalClass centreTotalClass = new CentreTotalClass(level);
        fragmentTransaction.replace(R.id.fragment, centreTotalClass, "centreTotalClass");
        fragmentTransaction.addToBackStack("searchClass");
        fragmentTransaction.commit();
    }

    public void setForum(String level,String subjectName){
        Intent forum = new Intent(this,CentreForum.class);
        forum.putExtra("detail",level + " " + subjectName);
        startActivity(forum);
    }
}
