package com.example.pandora.drawerlayout.Parent;


import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/11/2016.
 */
public class ParentProfile extends Fragment {

    View view;
    float width,height;
    EditText editContact, editSecondInfo, editThirdInfo,parentName;
    ImageView parentImage;
    TextView contactNumber,secondInfo,thirdInfo;
    BitmapConfig bitmapConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parent_profile, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();

        secondInfo = (TextView) view.findViewById(R.id.secondInfo);
        contactNumber = (TextView) view.findViewById(R.id.contactNumber);
        thirdInfo = (TextView) view.findViewById(R.id.thirdInfo);

        editContact = (EditText) view.findViewById(R.id.editContact);
        editSecondInfo = (EditText) view.findViewById(R.id.editSecondInfo);
        editThirdInfo = (EditText) view.findViewById(R.id.editThirdInfo);
        parentName = (EditText) view.findViewById(R.id.parentName);

        parentImage = (ImageView)view.findViewById(R.id.parentImage);

        setParentBio();
        setContact();
        setSecondInfo();
        setThirdInfo();

        return view;
    }

    private void setParentBio(){
        parentImage.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.rsz_sohai, (int) width / 6, (int) width / 6), 0));
        parentName.setText("Haji Mohammad Najib bin Tun Haji Abdul Razak");
        parentName.setHeight((int) width / 6);
    }

    private void setContact(){
        contactNumber.setWidth((int)(width/4.1538));
        contactNumber.setHeight((int)(height/17.4545));

        editContact.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void setSecondInfo(){
        secondInfo.setWidth((int)(width/4.1538));
        secondInfo.setHeight((int)(height/17.4545));
    }

    private void setThirdInfo(){
        thirdInfo.setWidth((int)(width/4.1538));
        thirdInfo.setHeight((int)(height/17.4545));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
