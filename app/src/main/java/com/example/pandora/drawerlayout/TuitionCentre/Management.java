package com.example.pandora.drawerlayout.TuitionCentre;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
 * Created by Pandora on 9/7/2016.
 */
public class Management extends AppCompatActivity {

    float width,height;
    BitmapConfig bitmapConfig;
    LinearLayout addLayout,editLayout;
    TextView addText,editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();

        addLayout = (LinearLayout)findViewById(R.id.addLayout);
        editLayout = (LinearLayout)findViewById(R.id.editLayout);
        addText = (TextView)findViewById(R.id.addText);
        editText = (TextView)findViewById(R.id.editText);

        setAddLayout();
        setEditLayout();
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

    private void setAddLayout(){

        String[] string = {"STUDENT","TEACHER","SUBJECT"};

        int padding = (int)(width/21.6);

        for (int i=0;i<3;i++){
            TextView textView = new TextView(this);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setPadding(0,padding/2,0,padding/2);
            linearLayout.addView(textView);
            textView.setTextColor(Color.BLACK);
            textView.setText(string[i]);
            textView.setPadding(padding,0,0,0);
            textView.setBackground(ContextCompat.getDrawable(this,R.drawable.round_edge_rectangle));
            textView.setHeight((int)height/13);
            textView.setWidth((int)width);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            addLayout.addView(linearLayout);

            switch (i){
                case 0:
                    textView.setTag("STUDENT");
                    break;

                case 1:
                    textView.setTag("TEACHER");
                    break;

                case 2:
                    textView.setTag("SUBJECT");
                    break;
            }
            textView.setOnClickListener(onClickListener);
        }
        addText.bringToFront();
        addText.setWidth((int)(width/4.32));
        addText.setHeight((int)(height/19.2));
        addLayout.setY(-(int)(height/48));
    }

    private void setEditLayout(){

        String[] string = {"STUDENT","TEACHER","SUBJECT"};

        int padding = (int)(width/21.6);

        for (int i=0;i<3;i++){
            TextView textView = new TextView(this);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setPadding(0,padding/2,0,padding/2);
            linearLayout.addView(textView);
            textView.setTextColor(Color.BLACK);
            textView.setText(string[i]);
            textView.setPadding(padding,0,0,0);
            textView.setBackground(ContextCompat.getDrawable(this,R.drawable.round_edge_rectangle));
            textView.setHeight((int)(height/13));
            textView.setWidth((int)width);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            editLayout.addView(linearLayout);
        }
        editText.bringToFront();
        editText.setWidth((int)(width/4.32));
        editText.setHeight((int)(height/19.2));
        editLayout.setY(-(int)(height/48));
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getTag().toString()){

                case "STUDENT":
                    break;

                case "TEACHER":
                    break;

                case "SUBJECT":
                    break;
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
}
