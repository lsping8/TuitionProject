package com.example.pandora.drawerlayout;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pandora.drawerlayout.DataForServer.Data;

import java.util.ArrayList;

/**
 * Created by Pandora on 9/26/2016.
 */

public class CommentDialog extends DialogFragment {

    View view;
    float width, height;
    String mode;
    RelativeLayout relativeLayout;
    ArrayList<Data.Comment> dataComment;
    Data data;
    TextView userName, postTime, description;
    ImageView imageView, send, uploadedImage;
    BitmapConfig bitmapConfig;
    RecyclerView recyclerView;
    CommentRecycleView adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText comment;

    public CommentDialog(String mode, Data data) {
        this.mode = mode;
        dataComment = data.getCommentsClass();
        this.data = data;
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
        view = inflater.inflate(R.layout.dialog_comment, container, false);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative);
        userName = (TextView) view.findViewById(R.id.userName);
        postTime = (TextView) view.findViewById(R.id.postTime);
        description = (TextView) view.findViewById(R.id.textDescription);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        uploadedImage = (ImageView) view.findViewById(R.id.uploadImage);
        send = (ImageView) view.findViewById(R.id.send);
        comment = (EditText) view.findViewById(R.id.addComment);

        bitmapConfig = new BitmapConfig();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        send.setImageBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(), R.drawable.button, (int) (width / 7), (int) (width / 7)));
        send.setTag("send");
        send.setOnClickListener(onClickListener);
        comment.setWidth((int) width - (int) (width / 7));

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, width / 2, height / 2);
        scaleAnimation.setDuration(400);
        relativeLayout.startAnimation(scaleAnimation);

        setCancelable(true);

        setUpTopComment();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new CommentRecycleView();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            adapter.refresh();
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getTag().toString()) {
                case "send":
                    if (!comment.getText().toString().equals("")) {
                        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                        itemAnimator.setAddDuration(500);
                        recyclerView.setItemAnimator(itemAnimator);
                        adapter.insert(R.drawable.rsz_sohai, "Something", "something", comment.getText().toString());

                        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(comment.getWindowToken(), 0);

                        comment.setText("");
                    } else {
                        Toast.makeText(getActivity(), "Comment Please", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void setUpTopComment() {
        imageView.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(), data.getImageId(), (int) (width / 7), (int) (width / 7)), 0));
        userName.setText(data.getUserName());
        postTime.setText(data.getPostTime());
        description.setText(data.getDescription());
        uploadedImage.setImageBitmap(data.getBitmap());
    }

    public class CommentRecycleView extends RecyclerView.Adapter<CommentViewHolder> {

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_comment, parent, false);
            return new CommentViewHolder(v, getActivity(), data);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            holder.userPic.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(), dataComment.get(position).getUserImage(), (int) (width / 9), (int) (width / 9)), 0));
            holder.userName.setText(dataComment.get(position).getUserName());
            holder.postTime.setText(dataComment.get(position).getPostTime());
            holder.description.setText(dataComment.get(position).getDescription());
        }

        @Override
        public int getItemCount() {
            return dataComment.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public void insert(int userImage, String UserName, String postTime, String description) {
            data.addComment(userImage, UserName, postTime, description);
            recyclerView.getLayoutManager().scrollToPosition(getItemCount());
            notifyItemInserted(getItemCount());
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(Data data) {
            int position = dataComment.indexOf(data);
            dataComment.remove(position);
            notifyItemRemoved(position);
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {

        Context context;
        Data data;
        ImageView userPic;
        TextView userName,description,postTime;

        private CommentViewHolder(View itemView, Context context,Data data) {
            super(itemView);
            this.context = context;
            this.data = data;
            userPic = (ImageView)itemView.findViewById(R.id.imageView);
            userName = (TextView)itemView.findViewById(R.id.userName);
            description = (TextView)itemView.findViewById(R.id.textDescription);
            postTime = (TextView)itemView.findViewById(R.id.postTime);
        }
    }

}
