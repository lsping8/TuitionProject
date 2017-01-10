package com.example.pandora.drawerlayout;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pandora.drawerlayout.DataForServer.Data;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Pandora on 9/27/2016.
 */

public class PostDialog extends DialogFragment {

    View view;
    float width, height;
    BitmapConfig bitmapConfig;
    Button postPicture, post;
    EditText postDescription;
    ImageView picture;
    PostFragment postFragment;
    Uri targetUri = null;
    Spinner postTopic;
    List<String> topic;
    String selectedTopic,editPost;
    Data editData;
    int position;

    public PostDialog(PostFragment postFragment, List<String> topic) {
        this.postFragment = postFragment;
        this.topic = topic;
    }

    public PostDialog(PostFragment postFragment, List<String> topic,Data data,int position){
        this.postFragment = postFragment;
        this.topic = topic;
        editData = data;
        this.position = position;
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
        view = inflater.inflate(R.layout.dialog_post, container, false);

        bitmapConfig = new BitmapConfig();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        postTopic = (Spinner) view.findViewById(R.id.postFilter);
        postDescription = (EditText) view.findViewById(R.id.postDescription);
        picture = (ImageView) view.findViewById(R.id.uploadedPicture);
        postPicture = (Button) view.findViewById(R.id.uploadPic);
        post = (Button) view.findViewById(R.id.post);
        postPicture.setTag("postPicture");
        post.setTag("post");
        postPicture.setOnClickListener(onClickListener);
        post.setOnClickListener(onClickListener);

        setSpinner();

        if (editData != null)
            editData();

        return view;
    }

    private void setSpinner() {

        HintAdapter adapter = new HintAdapter(getActivity(), android.R.layout.simple_spinner_item, topic);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postTopic.setAdapter(adapter);
        // show hint
        if (editData == null) {
            postTopic.setSelection(adapter.getCount());
        }else{
            for (int i=0;i<topic.size();i++){
                if (topic.get(i).equals(editData.getTopic())){
                    postTopic.setSelection(i);
                    selectedTopic = topic.get(i);
                    break;
                }
            }
        }

        postTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTopic = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void editData(){

        postDescription.setText(editData.getDescription());
        picture.setImageBitmap(editData.getBitmap());

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getTag().toString()) {

                case "postPicture":
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);
                    break;

                case "post":
                    Bitmap bitmap = null;
                    if (targetUri != null)
                        bitmap = bitmapConfig.decodeSampledBitmapFromUri(getActivity(), targetUri, (int) (width / 2), (int) (width / 2));

                    if (editData == null) {
                        postFragment.addData("Something", "Something", postDescription.getText().toString(), 0, 0, R.drawable.rsz_sohai, bitmap, selectedTopic);
                    }else{
                        postFragment.editData("Something", "Something", postDescription.getText().toString(), 0 , 0, R.drawable.rsz_sohai, bitmap, selectedTopic,position);
                    }
                    getDialog().cancel();
                    getDialog().dismiss();
                    break;

            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            targetUri = data.getData();
            picture.setImageBitmap(bitmapConfig.decodeSampledBitmapFromUri(getActivity(), targetUri, (int) (width / 6), (int) (width / 6)));
        }
    }

    public class HintAdapter extends ArrayAdapter<String> {

        public HintAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            // don't display last item. It is used as hint.
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }
}

