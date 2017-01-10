package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/5/2016.
 */
public class SearchClass extends Fragment {

    float height, width;
    View view;
    RelativeLayout relativeLayout;
    GridLayout gridLayout;
    String activityName = null;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_class, container, false);
        context = container.getContext();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        relativeLayout = (RelativeLayout) view.findViewById(R.id.search_relative);
        gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);

        setA();
        setB();

        return view;
    }

    private void setA() {

        int padding = (int) width / 24;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(padding, padding * 3, padding, padding);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle));
        relativeLayout.addView(linearLayout);
        gridLayout.addView(relativeLayout);
        ViewGroup.LayoutParams lp = relativeLayout.getLayoutParams();
        lp.height = (int) (height / 1.6);
        linearLayout.setY(height / 192);

        TextView groupName = new TextView(context);
        groupName.setText("Primary");
        groupName.setTextColor(Color.BLACK);
        groupName.setTextSize(20);
        groupName.setTypeface(Typeface.DEFAULT_BOLD);
        relativeLayout.addView(groupName);
        groupName.setY(height / 192);
        groupName.setX(height / 192);

        int margin = (int) height / 192;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, 0, 0, margin);

        TextView line = new TextView(context);
        line.setWidth((int) width);
        line.setHeight((int) (height / 384));
        line.setBackgroundColor(Color.GRAY);
        relativeLayout.addView(line);
        line.setY(height / 19.2f);

        for (int i = 0; i < 6; i++) {
            Button textView = new Button(context);
            textView.setWidth((int) (width - padding * 2));
            textView.setHeight((int) (height / 12.8));
            textView.setTextColor(Color.BLACK);
            textView.setPadding(padding, 0, 0, 0);
            textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.round_edge_rectangle));
            textView.setText("STANDARD " + (i + 1));
            textView.setLayoutParams(params);
            linearLayout.addView(textView);
            textView.setTag("STANDARD " + (i + 1));
            textView.setOnClickListener(onClickListener);
        }
    }

    private void setB() {

        int padding = (int) width / 24;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(padding, padding * 3, padding, padding);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle));
        relativeLayout.addView(linearLayout);
        gridLayout.addView(relativeLayout);
        ViewGroup.LayoutParams lp = relativeLayout.getLayoutParams();
        lp.height = (int) (height / 1.6);
        linearLayout.setY(height / 192);

        TextView groupName = new TextView(context);
        groupName.setText("Secondary");
        groupName.setTextColor(Color.BLACK);
        groupName.setTextSize(20);
        groupName.setTypeface(Typeface.DEFAULT_BOLD);
        relativeLayout.addView(groupName);
        groupName.setY(height / 192);
        groupName.setX(height / 192);

        int margin = (int) height / 192;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, 0, 0, margin);

        TextView line = new TextView(context);
        line.setWidth((int) width);
        line.setHeight((int) (height / 384));
        line.setBackgroundColor(Color.GRAY);
        relativeLayout.addView(line);
        line.setY(height / 19.2f);

        for (int i = 0; i < 5; i++) {
            Button textView = new Button(context);
            textView.setWidth((int) (width - padding * 2));
            textView.setHeight((int) (height / 12.8));
            textView.setTextColor(Color.BLACK);
            textView.setPadding(padding, 0, 0, 0);
            textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.round_edge_rectangle));
            textView.setText("FORM " + (i + 1));
            textView.setLayoutParams(params);
            linearLayout.addView(textView);
            textView.setTag("FORM " + (i + 1));
            textView.setOnClickListener(onClickListener);
        }
    }

    public void setActivity(String activityName) {
        this.activityName = activityName;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (activityName) {

                case "SearchReport":
                    Log.d("activityName", activityName);
                    Intent searchReport = new Intent(getActivity(), ReportStudent.class);
                    searchReport.putExtra("levelName", v.getTag().toString());
                    startActivity(searchReport);

                    break;

                case "Attendance":
                    Log.d("activityName", activityName);
                    break;

                case "timeTable":
                    Intent timeTable = new Intent(getActivity(), CentreTimeTable.class);
                    timeTable.putExtra("levelName", v.getTag().toString());
                    startActivity(timeTable);
                    break;

                case "forum":
                    ((ForumActivity)getActivity()).setLevelClass(v.getTag().toString());
                    break;
            }
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
