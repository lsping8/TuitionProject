package com.example.pandora.drawerlayout.Parent;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/11/2016.
 */
public class ParentActivity extends AppCompatActivity {

    float width,height;
    String fragmentToAttach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Intent intent = getIntent();
        fragmentToAttach = intent.getExtras().getString("fragmentToAttach");

        switch (fragmentToAttach){

            case "profileFragment":
                profileFragment();
                break;
        }
    }

    private void profileFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ParentProfile parentProfile = new ParentProfile();

        fragmentTransaction.add(R.id.parentFragment, parentProfile, "parentProfile");
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
