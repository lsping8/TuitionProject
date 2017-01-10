package com.example.pandora.drawerlayout;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pandora.drawerlayout.DataForServer.Data;
import com.example.pandora.drawerlayout.DataForServer.ForumData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pandora on 9/9/2016.
 */
public class PostFragment extends Fragment {

    View view;
    float height, width;
    BitmapConfig bitmapConfig;
    ImageView postBtn;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Handler handler = new Handler();
    ArrayList<Data> dataArray;
    ArrayList<Data> recycleData;
    List<String> topicArray;
    Spinner spinner;
    String selectedTopic;
    boolean isFilter = false;

    public PostFragment(ForumData forumData) {
        dataArray = forumData.getDataArray();
        topicArray = forumData.getTopic();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();

        spinner = (Spinner) view.findViewById(R.id.topicSpinner);
        postBtn = (ImageView) view.findViewById(R.id.postBtn);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        postBtn.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getActivity().getResources(), R.drawable.post_image, (int) (width / 6), (int) (width / 6)), 0));
        postBtn.setY(height - 650);
        postBtn.setX(width - width / 6 - width / 21.6f);

        postBtn.setOnClickListener(onClickListener);

        setData();
        setSpinner();

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
        float scale = getContext().getResources().getDisplayMetrics().density;
        adapter = new RecyclerViewAdapter(getActivity(), width, height, scale);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(500);
        itemAnimator.setChangeDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        return view;
    }

    private void setSpinner() {

        String[] topic = {"Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4", "Chapter 5", "Chapter 6", "Chapter 7"};
        String allTopic = "All topic";

        Collections.addAll(topicArray, topic);
        topicArray.add(allTopic);
        topicArray.add(allTopic);

        HintAdapter adapter = new HintAdapter(getActivity(), android.R.layout.simple_spinner_item, topicArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // show hint
        spinner.setSelection(adapter.getCount());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("select", "select");
                selectedTopic = parent.getItemAtPosition(position).toString();
                if (!selectedTopic.equals("All topic")) {
                    isFilter = true;
                } else {
                    isFilter = false;
                }
                filterTopic();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("non", "non");
                filterTopic();
            }
        });
    }

    private void filterTopic() {
        if (isFilter) {
            ArrayList<Data> filterData = new ArrayList<>();
            for (int i = 0; i < dataArray.size(); i++) {
                if (dataArray.get(i).getTopic().equals(selectedTopic)) {
                    filterData.add(dataArray.get(i));
                }
            }
            recycleData = filterData;
            adapter.refresh();
        } else {
            recycleData = dataArray;
            adapter.refresh();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PostDialog postDialog = new PostDialog(PostFragment.this,topicArray);
            postDialog.show(getActivity().getFragmentManager(), null);
        }
    };

    public void addData(String userName, String postTime, String description, int like, int comment, int imageId, Bitmap bitmap,String topic) {

        Data data = new Data(userName, getPostTime(new DateTime()), description, topic, like, comment, imageId, bitmap);
        adapter.insert(0, data, topic);
    }

    public void editData(String userName, String postTime, String description, int like, int comment, int imageId, Bitmap bitmap,String topic,int position){
        Data data = new Data(userName, getPostTime(new DateTime()), description, topic, like, comment, imageId, bitmap);
        adapter.change(position, data, topic);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.refresh();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        }
    };

    private void setData() {

        for (int i = 0; i < 10; i++) {
            dataArray.add(new Data("Something", getPostTime(new DateTime()), "something", "All topic", 0, 0, R.drawable.rsz_sohai, null));
        }
    }

    private String getPostTime(DateTime now) {

        String[] strings = {"second", "minute", "hour"};
        String word = null;
        DateTime myBirthDate = new DateTime(2016, 8, 26, 15, 45, 0, 0);
        Period period = new Period(myBirthDate, now);

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears().appendSuffix(" year,")
                .appendMonths().appendSuffix(" month,")
                .appendWeeks().appendSuffix(" week,")
                .appendDays().appendSuffix(" day,")
                .appendHours().appendSuffix(" hour,")
                .appendMinutes().appendSuffix(" minute,")
                .appendSeconds().appendSuffix(" second,")
                .toFormatter();

        String[] string = formatter.print(period).split(",");
        String[] string2 = string[0].split(" ");

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals(string2[1])) {
                word = string2[0] + " " + strings[i] + " ago";
            }
        }

        if (word == null) {
            word = "Few second ago";
        }

        return word;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<PostViewHolder> {

        private Context context;
        BitmapConfig bitmapConfig;
        float width, height;
        int padding;

        public RecyclerViewAdapter(Context context, float width, float height, float scale) {
            this.context = context;
            this.width = width;
            this.height = height;
            padding = (int) (10 * scale + 0.5f);
            bitmapConfig = new BitmapConfig();
            recycleData = dataArray;
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_post, parent, false);
            PostViewHolder holder = new PostViewHolder(v, context);
            return holder;
        }

        @Override
        public void onBindViewHolder(PostViewHolder holder, int position) {

            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            holder.userName.setText(recycleData.get(position).getUserName());
            holder.postTime.setText(recycleData.get(position).getPostTime());
            holder.description.setText(recycleData.get(position).getDescription());
            holder.setTag(position);

            holder.uploadImage.setImageBitmap(recycleData.get(position).getBitmap());

            if (!recycleData.get(position).getLike().equals("")) {
                holder.like.setText(recycleData.get(position).getLike());
                holder.like.setPadding(padding, padding, padding, padding);
            }
            if (!recycleData.get(position).getComment().equals("")) {
                holder.comment.setText(recycleData.get(position).getComment());
                holder.comment.setPadding(padding, padding, padding, padding);
            }
            holder.imageView.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(context.getResources(), recycleData.get(position).getImageId(), (int) (width / 7), (int) (width / 7)), 0));
        }

        @Override
        public int getItemCount() {
            //returns the number of elements the RecyclerView will display
            return recycleData.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        // Insert a new item to the RecyclerView on a predefined position
        public void insert(int position, Data data, String topic) {
            if (spinner.getSelectedItem().toString().equals(topic)) {
                recycleData.add(position, data);
                dataArray.add(position, data);
                recyclerView.getLayoutManager().scrollToPosition(0);
                notifyItemInserted(position);
            } else {
                dataArray.add(position, data);
                recyclerView.getLayoutManager().scrollToPosition(0);
                notifyItemInserted(position);
            }
        }

        public void change(int position, Data data, String topic){
            if (spinner.getSelectedItem().toString().equals(topic)) {
                recycleData.set(position, data);
                dataArray.set(position, data);
                notifyItemChanged(position);
            } else {
                dataArray.set(position, data);
                notifyItemChanged(position);
            }
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(int position) {
            if (!isFilter) {
                dataArray.remove(position);
                notifyItemRemoved(position);
            } else {
                Data temp = dataArray.get(position);
                int position2 = dataArray.indexOf(temp);
                recycleData.remove(position);
                dataArray.remove(position2);
                notifyItemRemoved(position);
            }
        }
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        TextView userName, postTime, description, comment, like, commentBtn;
        ImageView imageView, uploadImage, popUp;
        Context context;

        private PostViewHolder(View itemView, Context context) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            postTime = (TextView) itemView.findViewById(R.id.postTime);
            description = (TextView) itemView.findViewById(R.id.textDescription);
            comment = (TextView) itemView.findViewById(R.id.comment);
            like = (TextView) itemView.findViewById(R.id.like);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            uploadImage = (ImageView) itemView.findViewById(R.id.uploadImage);
            commentBtn = (TextView) itemView.findViewById(R.id.commentBtn);
            popUp = (ImageView) itemView.findViewById(R.id.popUp);
            popUp.setOnClickListener(onClickListener);
            commentBtn.setOnClickListener(onClickListener);
            this.context = context;
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                switch (v.getTag().toString()) {

                    case "CommentButton":
                        if (!isFilter) {
                            Data data = recycleData.get(v.getId());
                            CommentDialog forumDialog = new CommentDialog("post", data);
                            forumDialog.show(((Activity) context).getFragmentManager(), null);
                        } else {
                            Data data = dataArray.get(v.getId());
                            CommentDialog forumDialog = new CommentDialog("post", data);
                            forumDialog.show(((Activity) context).getFragmentManager(), null);
                        }
                        break;

                    case "popUp":
                        PopupMenu popup = new PopupMenu(getActivity(), popUp);
                        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getTitle().toString()){

                                    case "Edit Post":
                                        Data editData;
                                        if (!isFilter) {
                                            editData = dataArray.get(v.getId());
                                        } else {
                                            editData = recycleData.get(v.getId());
                                        }

                                        PostDialog postDialog = new PostDialog(PostFragment.this,topicArray,editData,v.getId());
                                        postDialog.show(getActivity().getFragmentManager(),null);
                                        break;

                                    case "Delete Post":
                                        adapter.remove(v.getId());
                                        break;

                                }
                                return true;
                            }
                        });
                        popup.show();
                        break;

                }


            }
        };

        public void setTag(int position) {
            commentBtn.setTag("CommentButton");
            popUp.setTag("popUp");
            if (!isFilter) {
                commentBtn.setId(position);
                popUp.setId(position);
            } else {
                commentBtn.setId(position);
                popUp.setId(position);
            }
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
