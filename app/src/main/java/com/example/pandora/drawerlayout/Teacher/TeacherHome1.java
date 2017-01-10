package com.example.pandora.drawerlayout.Teacher;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.TuitionCentre.Attendance;
import com.example.pandora.drawerlayout.TuitionCentre.CreatePromotion;
import com.example.pandora.drawerlayout.TuitionCentre.Management;
import com.example.pandora.drawerlayout.TuitionCentre.Promotion;
import com.example.pandora.drawerlayout.TuitionCentre.SearchReportStudent;
import com.example.pandora.drawerlayout.TuitionCentre.TodayClass;
import com.example.pandora.drawerlayout.TuitionCentre.TuitionFee;

/**
 * Created by Pandora on 9/7/2016.
 */
public class TeacherHome1 extends Fragment {

    View view;
    GridLayout gridLayout;
    float height,width;
    BitmapConfig bitmapConfig;
    TeacherMain teacherMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_1,container,false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        teacherMain = (TeacherMain)getActivity();

        bitmapConfig = new BitmapConfig();
        gridLayout = (GridLayout)view.findViewById(R.id.gridLayout);
        setButton();
        return view;
    }

    private void setButton() {
        int padding = (int) width / 8;
        int paddingHeight = (int) (height / 19.2);
        for (int i = 0; i < 6; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.red_panel, (int) width / 4, (int) width / 4), (int) width / 4, (int) width / 4, true));
            imageView.setPadding(padding, paddingHeight, padding, paddingHeight);
            gridLayout.addView(imageView);
        }

        gridLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent promotion = new Intent(getActivity(), Promotion.class);
                promotion.putExtra("owner","teacherClass");
                startActivity(promotion);
                promotion.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });

        gridLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeTable = new Intent(getActivity(), TeacherActivity.class);
                timeTable.putExtra("fragmentToAttach","teacherTimeTable");
                startActivity(timeTable);
                timeTable.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });

        gridLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(getActivity(),TeacherActivity.class);
                profile.putExtra("fragmentToAttach","profileFragment");
                startActivity(profile);
                profile.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });

        gridLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teacherClass = new Intent(getActivity(), TeacherActivity.class);
                teacherClass.putExtra("fragmentToAttach","teacherClass");
                startActivity(teacherClass);
                teacherClass.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}