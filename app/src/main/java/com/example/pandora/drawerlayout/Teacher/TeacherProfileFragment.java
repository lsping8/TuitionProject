package com.example.pandora.drawerlayout.Teacher;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.DataForServer.TeacherProfileData;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.TuitionCentre.CreateReport;

/**
 * Created by Pandora on 9/7/2016.
 */
public class TeacherProfileFragment extends Fragment {

    View view;
    float width, height;
    TextView race, contactNumber, experience,addCert;
    ImageView teacherImage;
    BitmapConfig bitmapConfig;
    EditText editContact, editRace, editExp, teacherName;
    LinearLayout certificate, profileLayout, bottomBtn, teachingIn, subjectTeach;
    ScrollView scrollView;
    int lineHeight, padding;
    TeacherProfileData teacherProfileData;
    InputMethodManager imm;

    public TeacherProfileFragment(){
        teacherProfileData = new TeacherProfileData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        float scaleValue = getActivity().getResources().getDisplayMetrics().density;
        lineHeight = (int) (1 * 0.5f + scaleValue);
        padding = (int) (5 * 0.5f + scaleValue);

        contactNumber = (TextView) view.findViewById(R.id.contactNumber);
        race = (TextView) view.findViewById(R.id.race);
        experience = (TextView) view.findViewById(R.id.teachingExp);
        addCert = (TextView) view.findViewById(R.id.addCert);
        teacherName = (EditText) view.findViewById(R.id.teacherName);
        teacherImage = (ImageView) view.findViewById(R.id.teacherImage);
        certificate = (LinearLayout) view.findViewById(R.id.certificate);
        profileLayout = (LinearLayout) view.findViewById(R.id.profileLayout);
        bottomBtn = (LinearLayout) view.findViewById(R.id.bottomBtn);
        teachingIn = (LinearLayout) view.findViewById(R.id.teachingIn);
        subjectTeach = (LinearLayout) view.findViewById(R.id.subjectTeaching);

        editContact = (EditText) view.findViewById(R.id.editContact);
        editRace = (EditText) view.findViewById(R.id.editRace);
        editExp = (EditText) view.findViewById(R.id.editExp);

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        ViewGroup.LayoutParams lp = scrollView.getLayoutParams();
        lp.height = (int) (height - height / 2.7429);

        setTeacherBio();

        setTeachingCentre();
        setSubjectTeaching();
        setBottomBtm();

        addCert.setTag("addCert");
        addCert.setOnClickListener(onClickListener);

        bottomBtn.setTag("updateBio");
        bottomBtn.setOnClickListener(onClickListener);

        return view;
    }

    private void setTeacherBio() {
        teacherImage.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.rsz_sohai, (int) width / 6, (int) width / 6), 0));
        teacherName.setText(teacherProfileData.getTeacherName());
        editContact.setInputType(InputType.TYPE_CLASS_NUMBER);
        editContact.setText(teacherProfileData.getTeacherContact());
        editRace.setText(teacherProfileData.getRace());
        editExp.setText(teacherProfileData.getTeachingExp());
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getTag().toString()){

                case "addCert":
                    buildCert();
                    break;

                case "updateBio":
                    teacherProfileData.updateData();
                    getActivity().finish();
                    break;
            }
        }
    };

    private void buildCert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setTitle("Input Certificate.");

        final EditText input = new EditText(getActivity());
        input.setGravity(Gravity.CENTER);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(3);
        input.setFilters(filters);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView line = new TextView(getActivity());
                TextView cert = new TextView(getActivity());

                line.setHeight(lineHeight);
                line.setWidth((int) width);
                line.setBackgroundColor(Color.parseColor("#000000"));

                cert.setWidth((int) width);
                cert.setGravity(Gravity.CENTER);
                cert.setTextColor(Color.parseColor("#000000"));
                cert.setText(input.getText().toString());
                teacherProfileData.getCert().add(input.getText().toString());
                cert.setPadding(padding, padding, padding, padding);

                certificate.addView(line);
                certificate.addView(cert);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.cancel();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setTeachingCentre() {

        for (int i = 0; i < 3; i++) {
            TextView line = new TextView(getActivity());
            TextView cert = new TextView(getActivity());

            line.setHeight(lineHeight);
            line.setWidth((int) width);
            line.setBackgroundColor(Color.parseColor("#000000"));

            cert.setWidth((int) width);
            cert.setGravity(Gravity.CENTER);
            cert.setTextColor(Color.parseColor("#000000"));
            cert.setText(teacherProfileData.getTeachingIn().get(i));
            cert.setPadding(padding, padding, padding, padding);

            teachingIn.addView(line);
            teachingIn.addView(cert);
        }
    }

    private void setSubjectTeaching() {
        for (int i = 0; i < 3; i++) {
            TextView line = new TextView(getActivity());
            TextView cert = new TextView(getActivity());

            line.setHeight(lineHeight);
            line.setWidth((int) width);
            line.setBackgroundColor(Color.parseColor("#000000"));

            cert.setWidth((int) width);
            cert.setGravity(Gravity.CENTER);
            cert.setTextColor(Color.parseColor("#000000"));
            cert.setText(teacherProfileData.getSubjectTeaching().get(i));
            cert.setPadding(padding, padding, padding, padding);

            subjectTeach.addView(line);
            subjectTeach.addView(cert);
        }
    }

    private void setBottomBtm() {
        bottomBtn.setY(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (imm != null)
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
