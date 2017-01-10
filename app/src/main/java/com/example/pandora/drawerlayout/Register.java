package com.example.pandora.drawerlayout;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Pandora on 8/30/2016.
 */
public class Register extends Fragment {

    View view;
    float height,width;
    RelativeLayout registerRelative;
    EditText email,password,confirm_pass;
    CharSequence passWord,confirm_Pass;
    Login login;
    BitmapConfig bitmapConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_register, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();
        login = (Login)getActivity();

        RelativeLayout registerBox = (RelativeLayout) view.findViewById(R.id.registerBox);
        registerRelative = (RelativeLayout)view.findViewById(R.id.registerRelative);
        email = (EditText)view.findViewById(R.id.ask_email);
        password = (EditText)view.findViewById(R.id.ask_pass);
        confirm_pass = (EditText)view.findViewById(R.id.confirmPass);
        ViewGroup.LayoutParams lp = registerBox.getLayoutParams();
        lp.width = (int)(width/1.2);
        lp.height = (int)(height/4.2667);
        registerBox.setBackgroundResource(R.drawable.register_box);
        registerBox.setX(width/12);
        registerBox.setY(height/38.4f);
        setUpNewAccountBtn();
        setUpRegister();
        return view;
    }

    private void setUpRegister(){
        int padding = (int)(width/21.6);

        email.setMaxWidth((int)(width - width/10));
        email.setMinimumWidth((int)(width - width/10));
        email.setMinimumHeight((int)(height/12.8));
        email.setSingleLine(true);
        email.setPadding(padding,0,0,0);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    login.moveLayout(true);
                    email.setOnFocusChangeListener(null);
                }
            }
        });

        password.setMaxWidth((int)(width - width/10));
        password.setMinimumWidth((int)(width - width/10));
        password.setMinimumHeight((int)(height/12.8));
        password.setSingleLine(true);
        password.setPadding(padding,0,0,0);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    login.moveLayout(true);
                    password.setOnFocusChangeListener(null);
                }
            }
        });
        password.setTag("password");
        password.setTransformationMethod(new MyPasswordTransformationMethod());

        confirm_pass.setMaxWidth((int)(width - width/10));
        confirm_pass.setMinimumWidth((int)(width - width/10));
        confirm_pass.setMinimumHeight((int)(height/12.8));
        confirm_pass.setSingleLine(true);
        confirm_pass.setPadding(padding,0,0,0);
        confirm_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    login.moveLayout(true);
                    confirm_pass.setOnFocusChangeListener(null);
                }
            }
        });
        confirm_pass.setTag("confirm_pass");
        confirm_pass.setTransformationMethod(new MyPasswordTransformationMethod());
    }

    private void setUpNewAccountBtn(){
        ImageView createAccount = new ImageView(getActivity());
        createAccount.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(),R.drawable.create_account,(int) (width/1.2), (int) (height/9.6)), (int) (width/1.2), (int) (height/9.6), true));
        registerRelative.addView(createAccount);
        createAccount.setY(height/38.4f + height/4.2667f + height/38.4f);
        createAccount.setX(width/12);
    }

    public class MyPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {

            if (view.getTag().equals("password")) {
                return new PasswordCharSequence(source);
            }
            if (view.getTag().equals("confirm_pass")){
                return new PasswordCharSequence(source,"");
            }
            return null;
        }

        private class PasswordCharSequence implements CharSequence {

            CharSequence mSource;

            public PasswordCharSequence(CharSequence source1) {
                passWord = source1; // Store char sequence
                mSource = source1;
            }

            public PasswordCharSequence(CharSequence source2,String test){
                confirm_Pass = source2;
                mSource = source2;
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
