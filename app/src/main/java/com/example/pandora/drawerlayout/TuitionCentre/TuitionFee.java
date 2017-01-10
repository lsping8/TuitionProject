package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/5/2016.
 */
public class TuitionFee extends AppCompatActivity {

    float height, width;
    RelativeLayout remainder, confirmation;
    Confirmation confirmationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuition_fee);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        remainder = (RelativeLayout) findViewById(R.id.remainder);
        confirmation = (RelativeLayout) findViewById(R.id.confirmation);

        setRemainder();
        setConfirmation();
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

    private void setRemainder() {
        ViewGroup.LayoutParams lp = remainder.getLayoutParams();
        lp.width = (int) (width - width / 12);
        lp.height = (int) (height / 6.4);
        remainder.setY(height / 21.3333f);
        remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDateDialog dialog = new SetDateDialog();
                dialog.show(getFragmentManager(), null);
            }
        });
    }

    private void setConfirmation() {
        ViewGroup.LayoutParams lp = confirmation.getLayoutParams();
        lp.width = (int) (width - width / 12);
        lp.height = (int) (height / 6.4);
        confirmation.setY(height / 10.6667f);
        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                confirmationFragment = new Confirmation();

                fragmentTransaction.add(R.id.fragment, confirmationFragment, "searchClass");
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onBackPressed() {
        if (confirmationFragment != null){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(confirmationFragment);
            fragmentTransaction.commit();
        }else {
            super.onBackPressed();
        }
    }
}
