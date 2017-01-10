package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;
import com.example.pandora.drawerlayout.SampleFragmentPagerAdapter;
import com.example.pandora.drawerlayout.DataForServer.SaveFile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Pandora on 8/28/2016.
 */
public class MainActivity extends AppCompatActivity {
    float width, height;
    RelativeLayout relativeLayout;
    ViewPager viewpager;
    LinearLayout linearLayout;
    BitmapConfig bitmapConfig;
    CircleIndicator indicator;
    String whichPage;
    SearchClass searchClass;
    CentreTotalClass centreTotalClass;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SaveFile saveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();
        fragmentManager = getFragmentManager();

        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        linearLayout = (LinearLayout) findViewById(R.id.bottomLinear);
        linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle));
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        setupViewPager();
        setBottomLinear();
        createNewSaveFile();
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
    }

    private void setupViewPager() {
        whichPage = "viewPager";
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), this.getApplicationContext());
        FragmentHome1 home1 = new FragmentHome1();
        FragmentHome2 home2 = new FragmentHome2();
        adapter.addFrag(home1, "");
        adapter.addFrag(home2, "");
        viewpager.setAdapter(adapter);
        indicator.setViewPager(viewpager);
    }

    public void setLevelClass(String level) {
        whichPage = "class";
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        centreTotalClass = new CentreTotalClass(level);
        fragmentTransaction.replace(R.id.fragment, centreTotalClass, "centreTotalClass");
        fragmentTransaction.addToBackStack("searchClass");
        fragmentTransaction.commit();
    }

    public void setForum(String level, String subjectName) {
        Intent forum = new Intent(this, CentreForum.class);
        forum.putExtra("detail", level + " " + subjectName);
        startActivity(forum);
    }

    private void setBottomLinear() {
        int padding = (int) width / 12;
        for (int i = 0; i < 3; i++) {
            LinearLayout linearLayout1 = new LinearLayout(this);
            ImageView homeImage = new ImageView(this);
            TextView home = new TextView(this);
            linearLayout1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);
            switch (i) {
                case 0:
                    homeImage.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_home, (int) width / 12, (int) width / 12), (int) width / 12, (int) width / 12, true));
                    home.setText("Home");
                    homeImage.setTag("Home");
                    homeImage.setOnClickListener(onClickListener);
                    break;
                case 1:
                    homeImage.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_home, (int) width / 12, (int) width / 12), (int) width / 12, (int) width / 12, true));
                    home.setText("Question");
                    homeImage.setTag("Question");
                    homeImage.setOnClickListener(onClickListener);
                    break;
                case 2:
                    homeImage.setImageBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_home, (int) width / 12, (int) width / 12), (int) width / 12, (int) width / 12, true));
                    home.setText("Me");
                    homeImage.setTag("Me");
                    homeImage.setOnClickListener(onClickListener);
                    break;
            }
            home.setGravity(Gravity.CENTER);
            home.setTextColor(Color.BLACK);
            linearLayout1.addView(homeImage);
            linearLayout1.addView(home);
            linearLayout1.setPadding(115, padding / 8, 115, 0);
            linearLayout.addView(linearLayout1);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getTag().toString()) {

                case "Home":

                    break;

                case "Question":

                    Intent forum = new Intent(MainActivity.this,ForumActivity.class);
                    startActivity(forum);

                    break;

                case "Me":

                    break;
            }
        }
    };

    public void createNewSaveFile() {

        Gson gson = new Gson();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = mPrefs.getString("MyObject", "");
        if (json.equals("")) {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            saveFile = new SaveFile();
            json = gson.toJson(saveFile);
            prefsEditor.putString("MyObject", json);
            prefsEditor.apply();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void getAction() {
        float scale = getResources().getDisplayMetrics().density;
        //Log.d("action",getSupportActionBar().getHeight() + " Scale: " + scale);
    }
}
