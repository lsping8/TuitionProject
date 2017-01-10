package com.example.pandora.drawerlayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.Parent.ParentMain;
import com.example.pandora.drawerlayout.Student.StudentMain;
import com.example.pandora.drawerlayout.Teacher.TeacherMain;
import com.example.pandora.drawerlayout.TuitionCentre.MainActivity;

/**
 * Created by Pandora on 8/30/2016.
 */
public class SignIn extends Fragment {

    View view;
    RelativeLayout signInRelative;
    EditText signInID,signInPass;
    LinearLayout faceBookLayout;
    float height,width;
    Login login;
    private CharSequence mSource;
    BitmapConfig bitmapConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_sign_in, container, false);

        //ss

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        login = (Login)getActivity();
        bitmapConfig = new BitmapConfig();

        signInRelative = (RelativeLayout)view.findViewById(R.id.signInRelative);
        signInID = (EditText)view.findViewById(R.id.signInID);
        signInPass = (EditText)view.findViewById(R.id.signInPass);
        faceBookLayout = (LinearLayout)view.findViewById(R.id.faceBookBtn);
        ImageView orImage = (ImageView)view.findViewById(R.id.orPicture);
        orImage.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(),R.drawable.or,(int) (width - width/10), (int) (height/38.4)), (int) (width - width/10), (int) (height/38.4), true));
        ImageView loginBtn = new ImageView(getActivity());
        loginBtn.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(),R.drawable.button,(int) (width/5.4), (int) (height/6.4)), (int) (width/5.4), (int) (height/6.4), true));
        signInRelative.addView(loginBtn);
        loginBtn.setOnClickListener(onClickListener);
        LinearLayout loginBox = (LinearLayout)view.findViewById(R.id.loginBox);
        ViewGroup.LayoutParams lp = loginBox.getLayoutParams();
        lp.width = (int)(width/1.5429);
        lp.height = (int)(height/6.4);

        lp = faceBookLayout.getLayoutParams();
        lp.height = (int)(height/3.84);

        loginBox.setBackgroundResource(R.drawable.login_box);
        loginBox.setX(width/12);
        loginBox.setY(height/38.4f);
        loginBtn.setX(width/12 + width/1.5429f);
        loginBtn.setY(height/38.4f);
        setLoginRow();
        setUpFaceBookLayout();
        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            CharSequence ID = signInID.getText();
            CharSequence passWord = mSource;
            Log.d("signin",ID+"");
            Log.d("signin",passWord+"");

            switch (signInID.getText().toString()){

                case "1":
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;

                case "2":
                    intent = new Intent(getActivity(),TeacherMain.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;

                case "3":
                    intent = new Intent(getActivity(),StudentMain.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;

                case "4":
                    intent = new Intent(getActivity(),ParentMain.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;

                case "":
                    intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
            }
        }
    };

    private void setLoginRow(){

        int padding = (int)(width/21.6);

        signInID.setMaxWidth((int)(width - width/10));
        signInID.setMinimumWidth((int)(width - width/10));
        signInID.setMinimumHeight((int)(height/12.8));
        signInID.setSingleLine(true);
        signInID.setPadding(padding,0,0,0);
        signInID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Log.d("onFocus","onFocus");
                    login.moveLayout(true);
                    signInID.setOnFocusChangeListener(null);
                }
            }
        });

        signInPass.setMaxWidth((int)(width - width/10));
        signInPass.setMinimumWidth((int)(width - width/10));
        signInPass.setMinimumHeight((int)(height/12.8));
        signInPass.setSingleLine(true);
        signInPass.setPadding(padding,0,0,0);
        signInPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Log.d("onFocus","onFocus");
                    login.moveLayout(true);
                    signInPass.setOnFocusChangeListener(null);
                }
            }
        });
        signInPass.setTransformationMethod(new MyPasswordTransformationMethod());
    }

    private void setUpFaceBookLayout(){
        ImageView faceBookButton = new ImageView(getActivity());
        TextView policy = new TextView(getActivity());
        faceBookLayout.addView(faceBookButton);
        int padding = (int)(width/13.5f);
        faceBookButton.setPadding(0,padding,0,padding);
        faceBookButton.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(),R.drawable.login_fb,(int) (width/1.2), (int) (height/9.6)), (int) (width/1.2), (int) (height/9.6), true));
        faceBookLayout.addView(policy);

        policy.setMinimumWidth((int)width);
        policy.setTextColor(Color.BLACK);
        policy.setGravity(Gravity.CENTER);
        policy.setTextSize(15);
        policy.setText("By signing in,You agree to our\n" + "Term of use and Privacy Policy" );

        faceBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class MyPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            public PasswordCharSequence(CharSequence source) {

                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
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
