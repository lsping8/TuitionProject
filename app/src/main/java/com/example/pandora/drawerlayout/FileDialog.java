package com.example.pandora.drawerlayout;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.DataForServer.MaterialData;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Pandora on 9/28/2016.
 */

public class FileDialog extends DialogFragment {

    View view;
    float width,height;
    BitmapConfig bitmapConfig;
    ImageView fileBtn;
    MaterialData materialData;
    RecyclerView recyclerView;
    FileRecycleView adapter;

    public FileDialog(MaterialData materialData){
        this.materialData = materialData;
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
        view = inflater.inflate(R.layout.dialog_file, container, false);

        bitmapConfig = new BitmapConfig();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        adapter = new FileRecycleView();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fileBtn = (ImageView)view.findViewById(R.id.fileBtn);
        fileBtn.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(),R.drawable.post_image,(int)(width/6),(int)(width/6)),0));
        fileBtn.setY(height - 650);
        fileBtn.setX(width - width/6 - width/21.6f);
        fileBtn.setOnClickListener(onClickListener);

        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showFileChooser();
        }
    };

    private void showFileChooser() {
        // This always works
        Intent i = new Intent(getActivity(), FilePickerActivity.class);
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
        /*i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);*/

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("requestCode",requestCode + "");
        Log.d("resultCode",resultCode + "");
        Log.d("Activity.RESULT_OK",Activity.RESULT_OK + "");

        if (resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            Log.d("Uri",uri+"");
                            File file = new File(uri.getPath());
                            adapter.insert(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getPath()), 600, 600),file.getName());
                            // Do something with the URI
                        }
                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);
                    if (paths != null) {
                        for (String path: paths) {
                            Uri uri = Uri.parse(path);
                            Log.d("Uri",uri+"");
                            // Do something with the URI
                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                File file = new File(uri.getPath());
                adapter.insert(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getPath()),600, 600),file.getName());
                Log.d("ElseUri",uri+"");
                // Do something with the URI
            }
        }
    }

    public String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public class FileRecycleView extends RecyclerView.Adapter<FileViewHolder>{

        @Override
        public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_file_material, parent, false);
            return new FileViewHolder(v);
        }

        @Override
        public void onBindViewHolder(FileViewHolder holder, int position) {
            holder.thumbNail.setImageBitmap(materialData.getFile(position).getThumbnail());
            holder.fileName.setText(materialData.getFile(position).getFileName());
        }

        @Override
        public int getItemCount() {
            return materialData.getFileNames().size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public void insert(Bitmap thumbnail,String fileName) {
            materialData.setNewFile(fileName,thumbnail);
            notifyItemInserted(getItemCount());
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(int position) {
            /*materialData.remove(position);
            notifyItemRemoved(position);*/
        }

    }

    private class FileViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbNail;
        TextView fileName;

        private FileViewHolder(View itemView) {
            super(itemView);
            thumbNail = (ImageView)itemView.findViewById(R.id.thumbnail);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
        }
    }
}
