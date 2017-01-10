package com.example.pandora.drawerlayout.Student;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/10/2016.
 */
public class StudentProfileFragment extends Fragment {

    View view;
    float width, height;
    TextView contactNumber, level, address,attendanceDay,attendancePercentage;
    BitmapConfig bitmapConfig;
    EditText editLevel, editContact, editAddress;
    LinearLayout attendance, profileLayout, bottomBtn;
    LinearLayout.LayoutParams params;
    ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_profile, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Log.d("height",height+"");
        Log.d("width",width+"");

        bitmapConfig = new BitmapConfig();

        level = (TextView) view.findViewById(R.id.level);
        contactNumber = (TextView) view.findViewById(R.id.contactNumber);
        address = (TextView) view.findViewById(R.id.address);
        attendancePercentage = (TextView)view.findViewById(R.id.attendancePercentage);
        attendanceDay = (TextView)view.findViewById(R.id.attendanceDay);

        attendance = (LinearLayout) view.findViewById(R.id.attendance);
        profileLayout = (LinearLayout) view.findViewById(R.id.profileLayout);
        bottomBtn = (LinearLayout) view.findViewById(R.id.bottomBtn);

        editLevel = (EditText) view.findViewById(R.id.editLevel);
        editContact = (EditText) view.findViewById(R.id.editContact);
        editAddress = (EditText) view.findViewById(R.id.editAddress);

        scrollView = (ScrollView)view.findViewById(R.id.scrollView);
        ViewGroup.LayoutParams lp = scrollView.getLayoutParams();
        lp.height = (int)(height - height/2.7429);

        int margin = (int) (height / 38.4);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, margin, 0, 0);

        setLevel();
        setContactNumber();
        setAddress();
        setAttendanceDay();
        setAttendancePercentage();
        setBottomBtn();

        return view;
    }

    private void setLevel() {
        level.setWidth((int)(width/4.1538));
        level.setHeight((int)(height/19.2));

    }

    private void setContactNumber() {
        contactNumber.setWidth((int)(width/4.1538));
        contactNumber.setHeight((int)(height/17.4545));

        editContact.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void setAddress() {
        address.setWidth((int)(width/4.1538));
        address.setHeight((int)(height/9.6));
    }

    private void setAttendanceDay(){
        attendanceDay.setWidth((int)width/2);
        attendanceDay.setHeight((int)(height/19.2));

        attendanceDay.setText("Day : 72/75");
    }

    private void setAttendancePercentage(){
        attendancePercentage.setWidth((int)width/2);
        attendancePercentage.setHeight((int)(height/19.2));

        attendancePercentage.setText("Percentage : 96%");
    }

    private void setBottomBtn(){
        TextView textView = new TextView(getActivity());
        bottomBtn.addView(textView);

        textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.round_edge_rectangle));
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(40);
        textView.setText("Update Bio");
        textView.setWidth((int)width);
        textView.setGravity(Gravity.CENTER);

        bottomBtn.setY(height - height/4.1739f);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
