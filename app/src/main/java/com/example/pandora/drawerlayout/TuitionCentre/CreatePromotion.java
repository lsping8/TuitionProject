package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Pandora on 9/6/2016.
 */
public class CreatePromotion extends Fragment {

    BitmapConfig bitmapConfig;
    float width, height;
    TextView tuitionCentre, submit, cancel;
    ImageView uploadPicture;
    RelativeLayout date;
    TextView setStart, setEnd;
    View view;
    Promotion promotion;
    DatePickerDialog startDatePickerDialog;
    DatePickerDialog endDatePickerDialog;
    SimpleDateFormat dateFormatter;
    TextView typeHere,title;
    Bitmap uploadedImage = null;

    public CreatePromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_promotion, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        tuitionCentre = (TextView) view.findViewById(R.id.tuitionCentre);
        submit = (TextView) view.findViewById(R.id.submit);
        cancel = (TextView) view.findViewById(R.id.cancel);
        uploadPicture = (ImageView) view.findViewById(R.id.uploadImage);
        date = (RelativeLayout) view.findViewById(R.id.date);
        setStart = (TextView) view.findViewById(R.id.setStartDate);
        setEnd = (TextView) view.findViewById(R.id.setEndDate);
        title = (TextView) view.findViewById(R.id.title);
        typeHere = (TextView) view.findViewById(R.id.typeHere);

        setUploadImage();
        setDescription();
        setDate();
        setNotify();
        setBtn();
        setDateTimeField();
        return view;
    }

    private void setUploadImage(){
        uploadPicture.setImageBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(),R.drawable.upload_picture,(int)(width),(int)(height/4.8)));
        uploadPicture.setTag("uploadImage");
        uploadPicture.setOnClickListener(onClickListener);
    }

    private void setDescription() {
        LinearLayout description = (LinearLayout) view.findViewById(R.id.description);
        ViewGroup.LayoutParams lp = description.getLayoutParams();
        lp.height = (int) (height / 5.4857);
    }

    private void setDate() {
        ViewGroup.LayoutParams lp = date.getLayoutParams();
        lp.height = (int) (height / 9.6);

        setStart.setWidth((int) (width / 1.5429));
        setStart.setY(-(height / 384));
        setStart.setTag("setStart");
        setStart.setOnClickListener(onClickListener);

        setEnd.setWidth((int) (width / 1.5429));
        setEnd.setY(height / 384);
        setEnd.setTag("setEnd");
        setEnd.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getTag().toString()) {

                case "setStart":
                    startDatePickerDialog.show();
                    break;

                case "setEnd":
                    endDatePickerDialog.show();
                    break;

                case "submit":
                    promotion.addData(setStart.getText().toString(),setEnd.getText().toString(),typeHere.getText().toString(),uploadedImage);
                    promotion.detachFragment();
                    break;

                case "cancel":
                    promotion.detachFragment();
                    break;

                case "setTitle":
                    builder(title,"Title");
                    break;

                case "setDescription":
                    builder(typeHere,"Description");
                    break;

                case "uploadImage":
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);
                    break;
            }
        }
    };

    private void builder(final TextView textView,String title){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();

        int theme;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            theme = android.R.style.Theme_DeviceDefault_Light_Dialog;
        } else {
            theme = AlertDialog.THEME_HOLO_LIGHT;
        }

        startDatePickerDialog = new DatePickerDialog(getActivity(), theme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                setStart.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(getActivity(), theme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                setEnd.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setNotify() {
        LinearLayout notifySelect = (LinearLayout) view.findViewById(R.id.notifyLayout);
        ViewGroup.LayoutParams lp = notifySelect.getLayoutParams();
        lp.height = (int) (height / 8.3478);
    }

    private void setBtn() {

        submit.setHeight((int) (height / 19.2));
        submit.setWidth((int) (width / 2.16));
        submit.setTag("submit");
        submit.setOnClickListener(onClickListener);

        cancel.setHeight((int) (height / 19.2));
        cancel.setWidth((int) (width / 2.16));
        cancel.setX(width / 1.9636f);
        cancel.setTag("cancel");
        cancel.setOnClickListener(onClickListener);

        title.setTag("setTitle");
        title.setOnClickListener(onClickListener);

        typeHere.setTag("setDescription");
        typeHere.setOnClickListener(onClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            uploadedImage = bitmapConfig.decodeSampledBitmapFromUriWithOutScale(getActivity(),targetUri,(int)(width/5.4),(int)(height/4.8));
            uploadPicture.setImageBitmap(uploadedImage);
        }
    }
}
