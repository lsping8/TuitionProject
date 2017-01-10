package com.example.pandora.drawerlayout.Teacher;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/9/2016.
 */
public class TeacherClass extends Fragment {

    View view;
    float width,height;
    BitmapConfig bitmapConfig;
    GridLayout gridLayout;
    LinearLayout.LayoutParams params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();

        gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);

        setClass();

        return view;
    }

    private void setClass(){

        for (int i=0;i<2;i++){
            LinearLayout linearLayout = new LinearLayout(getActivity());

            if (i!=0) {
                linearLayout.setPadding(0, 100, 0, 0);
            }else{
                linearLayout.setOnClickListener(onClickListener);
            }

            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.LEFT);
            gridLayout.addView(linearLayout);

            TextView level = new TextView(getActivity());
            level.setText("FORM 1");
            level.setTextColor(Color.BLACK);
            linearLayout.addView(level);

            LinearLayout classIcon = new LinearLayout(getActivity());
            classIcon.setOrientation(LinearLayout.VERTICAL);
            classIcon.setGravity(Gravity.LEFT);
            classIcon.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.rectangle));
            linearLayout.addView(classIcon);

            ImageView imageView = new ImageView(getActivity());
            imageView.setImageBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(),R.drawable.rsz_sohai,(int)(width/2),(int)(width/2)));
            classIcon.addView(imageView);

            TextView detail = new TextView(getActivity());
            detail.setText("BM class\nForm1\n10 members");
            detail.setTextColor(Color.BLACK);
            detail.setGravity(Gravity.LEFT);
            detail.setPadding(20,0,0,0);
            classIcon.addView(detail);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent forum = new Intent(getActivity(),TeacherForum.class);
            startActivity(forum);
            forum.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
