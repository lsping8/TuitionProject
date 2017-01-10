package com.example.pandora.drawerlayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pandora.drawerlayout.DataForServer.ForumData;
import com.example.pandora.drawerlayout.DataForServer.NoticeData;

import java.util.ArrayList;

/**
 * Created by Pandora on 9/9/2016.
 */
public class NoticeFragment extends Fragment {

    View view;
    float height,width;
    BitmapConfig bitmapConfig;
    ImageView noticeBtn;
    ArrayList<NoticeData> noticeData;
    RecyclerView recyclerView;
    NoticeRecycleViewAdapter adapter;

    public NoticeFragment(ForumData forumData){
        noticeData = forumData.getNoticeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice,container,false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();

        noticeBtn = (ImageView)view.findViewById(R.id.postBtn);

        noticeBtn.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(),R.drawable.post_image,(int)(width/6),(int)(width/6)),0));
        noticeBtn.setY(height - height/2.9538f);
        noticeBtn.setX(width - width/6 - width/21.6f);
        noticeBtn.setOnClickListener(onClickListener);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        adapter = new NoticeRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NoticeDialog noticeDialog = new NoticeDialog(NoticeFragment.this);
            noticeDialog.show(getActivity().getFragmentManager(),null);
        }
    };

    public void addNotice(String announcement,String postTime,String userName ){
        adapter.insert(new NoticeData(announcement,postTime,userName));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public class NoticeRecycleViewAdapter extends RecyclerView.Adapter<NoticeViewHolder>{

        public NoticeRecycleViewAdapter(){

        }

        @Override
        public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_notice, parent, false);
            return new NoticeViewHolder(v);
        }

        @Override
        public void onBindViewHolder(NoticeViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return noticeData.size();
        }

        public void insert(NoticeData data){
            noticeData.add(0,data);
            recyclerView.getLayoutManager().scrollToPosition(0);
            notifyItemInserted(0);
        }

        public void remove(){

        }
    }

    private class NoticeViewHolder extends RecyclerView.ViewHolder {

        public NoticeViewHolder(View itemView) {
            super(itemView);
        }
    }
}
