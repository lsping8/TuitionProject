package com.example.pandora.drawerlayout.TuitionCentre;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.system.ErrnoException;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/4/2016.
 */
public class FragmentHome2 extends Fragment {

    View view;
    GridLayout gridLayout;
    float height,width;
    BitmapConfig bitmapConfig;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_2,container,false);

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
        for (int i=0;i<5;i++){
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(),R.drawable.red_panel,(int)width/4,(int)width/4),(int)width/4,(int)width/4,true));
            imageView.setPadding(padding,paddingHeight,padding,paddingHeight);
            gridLayout.addView(imageView);
        }

        gridLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timeTable = new Intent(getActivity(),TimeTableCategorize.class);
                startActivity(timeTable);
                timeTable.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
        });

        gridLayout.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences mPrefs;
                    mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    mPrefs.edit().clear().apply();
                    Toast.makeText(getActivity(),"SharedPreferences Reset Success",Toast.LENGTH_SHORT).show();
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.createNewSaveFile();
                }catch (Exception e){
                    Toast.makeText(getActivity(),"SharedPreferences Reset Fail!!!!",Toast.LENGTH_SHORT).show();
                }
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
