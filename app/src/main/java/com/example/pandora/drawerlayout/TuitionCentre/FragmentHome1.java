package com.example.pandora.drawerlayout.TuitionCentre;

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

/**
 * Created by Pandora on 9/4/2016.
 */
public class FragmentHome1 extends Fragment {

    View view;
    GridLayout gridLayout;
    float height,width;
    BitmapConfig bitmapConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_1,container,false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();
        gridLayout = (GridLayout)view.findViewById(R.id.gridLayout);
        setButton();
        return view;
    }

    private void setButton(){
        int padding = (int)width/8;
        int paddingHeight = (int)(height/19.2);
        for (int i=0;i<6;i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(),R.drawable.red_panel,(int)width/4,(int)width/4),(int)width/4,(int)width/4,true));
            imageView.setPadding(padding,paddingHeight,padding,paddingHeight);
            gridLayout.addView(imageView);
        }

        gridLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent attendance = new Intent(getActivity(),Attendance.class);
                startActivity(attendance);
                attendance.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });

        gridLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tuitionFee = new Intent(getActivity(),TuitionFee.class);
                startActivity(tuitionFee);
                tuitionFee.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });

        gridLayout.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent promotion = new Intent(getActivity(),Promotion.class);
                promotion.putExtra("whichMode","centre");
                startActivity(promotion);
                promotion.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });

        gridLayout.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchReportStudent = new Intent(getActivity(),SearchReportStudent.class);
                startActivity(searchReportStudent);
                searchReportStudent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });

        gridLayout.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent management = new Intent(getActivity(),Management.class);
                startActivity(management);
                management.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });

        gridLayout.getChildAt(5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).getAction();
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
