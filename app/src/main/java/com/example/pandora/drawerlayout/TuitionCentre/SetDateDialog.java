package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/5/2016.
 */
public class SetDateDialog extends DialogFragment {

    View view;
    float height,width;
    RelativeLayout relativeLayout;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_set_date, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        relativeLayout = (RelativeLayout)view.findViewById(R.id.relative);
        ViewGroup.LayoutParams lp = relativeLayout.getLayoutParams();
        lp.width = (int)(width/1.08);
        lp.height = (int)(height/1.3714);

        setCancelable(true);
        setDialog();
        setMonthBtn();

        return view;
    }

    private void setDialog(){
        TextView set_date = (TextView)view.findViewById(R.id.setDate);
        TextView date = (TextView)view.findViewById(R.id.Date);
        TextView month = (TextView)view.findViewById(R.id.Month);
        TextView set = (TextView)view.findViewById(R.id.set);
        TextView line1 = new TextView(getActivity());
        TextView line2 = new TextView(getActivity());

        relativeLayout.addView(line1);
        relativeLayout.addView(line2);

        View line = view.findViewById(R.id.line);

        line1.setHeight((int)height/384);
        line1.setWidth((int)(width/1.3012));
        line1.setBackgroundColor(Color.DKGRAY);
        line1.setY(height/12.2414f);
        line1.setX(width/7.2f);

        line2.setHeight((int)height/384);
        line2.setWidth((int)(width/1.4026));
        line2.setBackgroundColor(Color.DKGRAY);
        line2.setY(height/3.5888f);
        line2.setX(width/5.1429f);

        set_date.setY(height/192);
        set_date.setX(width/54);

        line.setY(height/192);

        date.setY(height/17.4545f);
        date.setX(width/108);

        month.setY(height/3.84f);
        month.setX(width/108);

        set.setWidth((int)(width/1.2));
        set.setHeight((int)(height/19.2));
        set.setY(height/1.536f);
        set.setX(width/21.6f);
        set.setTag("set");
        set.setOnClickListener(onClickListener);
    }

    private void setMonthBtn(){
        String[] Month = {"JAN","FEB","MARH","APL","MAY","JUNE","JULY","AUG","SEPT","OCT","NOV","DEC"};
        GridLayout gridLayout = new GridLayout(getActivity());
        relativeLayout.addView(gridLayout);
        gridLayout.setColumnCount(3);
        gridLayout.setY(height/3.4286f);

        int padding = (int)width/54;

        for (int i=0;i<Month.length;i++){
            LinearLayout linearLayout = new LinearLayout(getActivity());
            TextView textView = new TextView(getActivity());
            linearLayout.setPadding(padding,padding,padding,padding);
            linearLayout.addView(textView);
            textView.setTextColor(Color.BLACK);
            textView.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.rectangle));
            textView.setGravity(Gravity.CENTER);
            textView.setText(Month[i]);
            textView.setWidth((int)(width/3.6610));
            textView.setHeight((int)(height/16));
            textView.setTag(Month[i]);
            gridLayout.addView(linearLayout);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getTag().toString().equals("set")){
                getDialog().cancel();
                getDialog().dismiss();
            }else{

            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
