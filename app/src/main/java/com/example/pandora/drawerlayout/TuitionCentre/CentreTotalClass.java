package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.DataForServer.SaveFile;
import com.example.pandora.drawerlayout.RecyclerTouchListener;
import com.google.gson.Gson;

/**
 * Created by Pandora on 9/24/2016.
 */

public class CentreTotalClass extends Fragment {

    float width, height;
    BitmapConfig bitmapConfig;
    View view;
    String level;
    SaveFile saveFile;
    GridLayout gridLayout;
    Context context;
    RecyclerView recyclerView;
    ClassRecycleView adapter;

    public CentreTotalClass(String level) {
        this.level = level;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class, container, false);
        context = container.getContext();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();
        loadSaveFile();

        gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ((ForumActivity) getActivity()).setForum(level, saveFile.getSubjectClass(level).get(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        adapter = new ClassRecycleView();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        return view;
    }

    private void loadSaveFile() {
        Gson gson = new Gson();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = mPrefs.getString("MyObject", "");
        saveFile = gson.fromJson(json, SaveFile.class);
    }

    public class ClassRecycleView extends RecyclerView.Adapter<ClassViewHolder> {

        @Override
        public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_class, parent, false);
            return new ClassViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ClassViewHolder holder, int position) {
            holder.classImage.setImageBitmap(bitmapConfig.decodeSampledBitmapFromResource(context.getResources(), R.drawable.rsz_sohai, (int) (width / 2), (int) (width / 2)));
            holder.className.setText(saveFile.getSubjectClass(level).get(position));
            holder.classLevel.setText(level);
            holder.classMember.setText("10 members");
        }

        @Override
        public int getItemCount() {
            if (saveFile.getSubjectClass(level) == null)
                return 0;
            return saveFile.getSubjectClass(level).size();
        }
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {

        ImageView classImage;
        TextView className, classLevel, classMember;

        public ClassViewHolder(View itemView) {
            super(itemView);
            classImage = (ImageView) itemView.findViewById(R.id.classImage);
            className = (TextView) itemView.findViewById(R.id.className);
            classLevel = (TextView) itemView.findViewById(R.id.classLevel);
            classMember = (TextView) itemView.findViewById(R.id.classMember);
        }
    }
}
