package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.DataForServer.PromotionData;
import com.example.pandora.drawerlayout.PostFragment;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.RecyclerTouchListener;

import java.util.ArrayList;

/**
 * Created by Pandora on 9/6/2016.
 */
public class Promotion extends AppCompatActivity {

    BitmapConfig bitmapConfig;
    float width, height;
    RecyclerView recyclerView;
    ImageView promotionBtn;
    boolean isAttach = false;
    CreatePromotion createPromotion;
    RecyclePromotion adapter;
    PromotionData promotionData;
    String whichMode;

    public Promotion(){
        promotionData = new PromotionData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Intent intent = getIntent();
        whichMode = intent.getStringExtra("whichMode");
        if(whichMode == null)
            whichMode = "";

        bitmapConfig = new BitmapConfig();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        adapter = new RecyclePromotion();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        setTopLayout();
    }

    private void setTopLayout() {
        LinearLayout topLayout = (LinearLayout) findViewById(R.id.topLayout);
        ImageView userImage = (ImageView) findViewById(R.id.topPicture);
        TextView userTitle = (TextView) findViewById(R.id.topTitle);
        View padding = findViewById(R.id.padding);

        topLayout.setBackgroundColor(Color.parseColor("#00FFFF"));
        userTitle.setText("Centre");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int statusBarHeight = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            View view = new View(this);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.getLayoutParams().height = statusBarHeight;
            ViewGroup.LayoutParams lp = padding.getLayoutParams();
            lp.height = statusBarHeight;
            ((ViewGroup) w.getDecorView()).addView(view);
            view.setBackgroundColor(Color.parseColor("#008b8b"));
        }

        if (whichMode.equals("centre")){
            promotionBtn = (ImageView) findViewById(R.id.promotionBtn);
            promotionBtn.setImageBitmap(bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.post_image, (int) (width / 6), (int) (width / 6)), 0));
            promotionBtn.setY(height - height / 2.9538f);
            promotionBtn.setX(width - width / 6 - width / 21.6f);
            promotionBtn.setOnClickListener(onClickListener);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            createPromotion = new CreatePromotion(Promotion.this);
            fragmentTransaction.add(R.id.fragment, createPromotion, "createPromotion");
            fragmentTransaction.commit();
            isAttach = true;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onBackPressed() {
        if (isAttach) {
            detachFragment();
        } else {
            super.onBackPressed();
        }
    }

    public void detachFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(createPromotion);
        fragmentTransaction.commit();
        isAttach = false;
    }

    public void addData(String startDate,String endDate,String promotionText,Bitmap promotionImage){
        promotionData.addNewDate(startDate,endDate,promotionText,promotionImage);
        adapter.insert();
    }

    public class RecyclePromotion extends RecyclerView.Adapter<PromotionViewHolder>{

        @Override
        public PromotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_promotion, parent, false);
            return new PromotionViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PromotionViewHolder holder, int position) {
            holder.startDate.setText(promotionData.getStorePromotionArrayList().get(position).getStartDate());
            holder.endDate.setText(promotionData.getStorePromotionArrayList().get(position).getEndDate());
            holder.promotionText.setText(promotionData.getStorePromotionArrayList().get(position).getPromotionText());
            holder.promotionImage.setImageBitmap(promotionData.getStorePromotionArrayList().get(position).getPromotionImage());
        }

        @Override
        public int getItemCount() {
            return promotionData.getStorePromotionArrayList().size();
        }

        private void insert(){
            recyclerView.getLayoutManager().scrollToPosition(0);
            notifyItemInserted(0);
        }
    }

    private class PromotionViewHolder extends RecyclerView.ViewHolder{

        ImageView promotionImage;
        TextView promotionText,startDate,endDate;

        private PromotionViewHolder(View itemView) {
            super(itemView);
            promotionImage = (ImageView) itemView.findViewById(R.id.promotionImage);
            promotionText = (TextView) itemView.findViewById(R.id.promotionText);
            startDate = (TextView) itemView.findViewById(R.id.startDate);
            endDate = (TextView) itemView.findViewById(R.id.endDate);
        }
    }
}
