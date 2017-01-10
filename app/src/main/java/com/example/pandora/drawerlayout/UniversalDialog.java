package com.example.pandora.drawerlayout;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pandora.drawerlayout.DataForServer.MaterialData;
import com.example.pandora.drawerlayout.TuitionCentre.CentreTimeTable;

import java.util.ArrayList;

/**
 * Created by Pandora on 9/15/2016.
 */
public class UniversalDialog extends DialogFragment {

    CentreTimeTable centreTimeTable;
    View view;
    float width, height;
    RelativeLayout relativeLayout;
    TextView ok, cancel, description, info;
    String descriptionString;
    String key, day;
    LinearLayout linearLayout;
    String mode;
    MaterialSharingFragment.MaterialSharingRecycleView adapter;
    ArrayList<MaterialData> materialData;
    EditText folderName;

    public UniversalDialog(CentreTimeTable centreTimeTable, String descriptionString, String key, String day, LinearLayout linearLayout, String mode) {
        this.centreTimeTable = centreTimeTable;
        this.descriptionString = descriptionString;
        this.key = key;
        this.day = day;
        this.linearLayout = linearLayout;
        this.mode = mode;
    }

    public UniversalDialog(MaterialSharingFragment.MaterialSharingRecycleView adapter, ArrayList<MaterialData> materialData, String mode) {
        this.adapter = adapter;
        this.materialData = materialData;
        this.mode = mode;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setOwnerActivity(getActivity());
        dialog.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        showImmersive(getFragmentManager());
    }

    private void showImmersive(FragmentManager manager) {
        // It is necessary to call executePendingTransactions() on the FragmentManager
        // before hiding the navigation bar, because otherwise getWindow() would raise a
        // NullPointerException since the window was not yet created.
        manager.executePendingTransactions();

        // Copy flags from the activity, assuming it's fullscreen.
        // It is important to do this after show() was called. If we would do this in onCreateDialog(),
        // we would get a requestFeature() error.
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                getActivity().getWindow().getDecorView().getSystemUiVisibility()
        );

        // Make the dialogs window focusable again
        getDialog().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_universal, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative);
        ok = (TextView) view.findViewById(R.id.ok);
        ok.setTag("ok");
        ok.setOnClickListener(onClickListener);

        cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setTag("cancel");
        cancel.setOnClickListener(onClickListener);

        description = (TextView) view.findViewById(R.id.description);
        info = (TextView) view.findViewById(R.id.info);

        setLayout(mode);

        ok.setWidth((int) (width / 3.6));
        ok.setHeight((int) (height / 12.8));

        cancel.setWidth((int) (width / 3.6));
        cancel.setHeight((int) (height / 12.8));

        return view;
    }

    private void setLayout(String mode) {

        switch (mode) {

            case "timeTable":

                info.setText("Are you sure you want to delete the following class?");
                description.setText(descriptionString);

                ViewGroup.LayoutParams lp = relativeLayout.getLayoutParams();
                lp.width = (int) (width / 1.08);
                lp.height = (int) (height / 2.7428);

                break;

            case "materialSharing":

                info.setText("Name the folder.");

                lp = relativeLayout.getLayoutParams();
                lp.width = (int) (width / 1.08);
                lp.height = (int) (height / 3.2);

                folderName = new EditText(getActivity());
                relativeLayout.addView(folderName);
                folderName.setWidth((int) width);
                folderName.setY(height / 9.6f);

                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getTag().toString()) {

                case "ok":
                    mode();
                    getDialog().cancel();
                    getDialog().dismiss();
                    break;

                case "cancel":
                    getDialog().cancel();
                    getDialog().dismiss();
                    break;

            }
        }
    };

    private void mode() {
        switch (mode) {
            case "timeTable":
                centreTimeTable.deleteData(key, day, linearLayout);
                break;

            case "materialSharing":
                if (!folderName.getText().toString().equals("")) {
                    adapter.insert(folderName.getText().toString());
                }else{
                    Toast.makeText(getActivity(),"Please name the folder",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
