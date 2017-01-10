package com.example.pandora.drawerlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pandora.drawerlayout.DataForServer.SaveFile;
import com.example.pandora.drawerlayout.TuitionCentre.CentreTimeTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

/**
 * Created by Pandora on 9/12/2016.
 */

public class TimeTable extends Fragment {

    View view;
    float height, width;
    BitmapConfig bitmapConfig;
    LinearLayout topLayout;
    GridLayout time;
    RelativeLayout subject;
    CentreTimeTable centreTimeTable;
    String whoAccess;
    SharedPreferences mPrefs;
    int currentWidth = 0;
    SaveFile saveFile;
    ArrayList<Integer> classOfHour = new ArrayList<>();
    TreeSet<Integer> storeYPosition = new TreeSet<>();
    Animator mCurrentAnimator;
    int mShortAnimationDuration = 250;
    SaveFile.SaveClassDay.SaveTimeTable saveTimeTable;


    public TimeTable() {

    }

    public TimeTable(CentreTimeTable centreTimeTable, SaveFile saveFile) {
        this.centreTimeTable = centreTimeTable;
        this.saveFile = saveFile;
        whoAccess = "centre";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time_table, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        bitmapConfig = new BitmapConfig();
        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        time = (GridLayout) view.findViewById(R.id.timeLayout);
        topLayout = (LinearLayout) view.findViewById(R.id.topBtn);
        topLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rectangle));
        subject = (RelativeLayout) view.findViewById(R.id.subject);

        setTime();
        setTopLayout();
        setSubject();

        return view;
    }

    private void setTime() {

        int timeNum = 10;
        String string = "AM";

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 2; j++) {

                if (timeNum == 12) {
                    string = "PM";
                }

                if (timeNum == 13) {
                    timeNum = 1;
                    string = "PM";
                }

                TextView textView = new TextView(getActivity());
                if (j == 0) {
                    textView.setText(timeNum + " : " + "00 " + string);
                    textView.setTag(timeNum + " : " + "00 " + string);
                } else {
                    textView.setText(timeNum + " : " + "30 " + string);
                    textView.setTag(timeNum + " : " + "30 " + string);
                }

                textView.setTextColor(Color.BLACK);
                textView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rectangle));
                textView.setHeight((int) (height / 12.8));
                textView.setWidth((int) (width / 4.32));
                textView.setGravity(Gravity.CENTER);
                time.addView(textView);
            }
            timeNum++;
        }
    }

    private void setTopLayout() {

        String[] tags = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        for (int i = 0; i < days.length; i++) {
            TextView day = new TextView(getActivity());
            day.setText(days[i]);
            day.setTag(tags[i]);
            day.setTextColor(Color.WHITE);
            day.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red));
            day.setWidth((int) (width / 7));
            day.setHeight((int) (width / 7));
            day.setGravity(Gravity.CENTER);
            topLayout.addView(day);
            day.setOnClickListener(onClickListener);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ViewGroup.LayoutParams lp = subject.getLayoutParams();
            lp.width = (int) (width / 4.32);
            currentWidth = (int) (width / 4.32);
            storeYPosition.clear();
            classOfHour.clear();

            switch (v.getTag().toString()) {

                case "class":
                    zoomImageFromThumb(((TextView) v), ((TextView) v).getText().toString());
                    break;

                case "Monday":
                    subject.removeAllViews();
                    loadData(v.getTag().toString());
                    break;

                case "Tuesday":
                    subject.removeAllViews();
                    loadData(v.getTag().toString());
                    break;

                case "Wednesday":
                    subject.removeAllViews();
                    loadData(v.getTag().toString());
                    break;

                case "Thursday":
                    subject.removeAllViews();
                    loadData(v.getTag().toString());
                    break;

                case "Friday":
                    subject.removeAllViews();
                    loadData(v.getTag().toString());
                    break;

                case "Saturday":
                    subject.removeAllViews();
                    loadData(v.getTag().toString());
                    break;

                case "Sunday":
                    subject.removeAllViews();
                    loadData(v.getTag().toString());
                    break;
            }
        }
    };

    private void setSubject() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        final String dayOfTheWeek = sdf.format(d);

        switch (whoAccess) {

            case "centre":
                loadData(dayOfTheWeek);
                break;

            case "teacher":

                break;
        }
    }

    private void loadData(String day) {
        String level;
        for (int i = 0; i < 6; i++) {
            level = "STANDARD " + (i + 1);
            setCentreTimeTable(day, level);
        }

        for (int i = 0; i < 5; i++) {
            level = "FORM " + (i + 1);
            setCentreTimeTable(day, level);
        }
    }

    private void teacherLoadDate(String day){



    }

    private void setCentreTimeTable(String day, String level) {

        for (int i = 0; i < saveFile.getClassDetailSize(day, level); i++) {
            saveTimeTable = saveFile.getPerDayClass(level, day).get(i);
            int currentYPosition = 0;
            TextView Class = new TextView(getActivity());
            Class.setTag("class");
            Class.setOnClickListener(onClickListener);
            subject.addView(Class);

            String[] startTime = saveTimeTable.getStartTime().split(" ");
            String[] endTime = saveTimeTable.getEndTime().split(" ");
            String subjectName = saveTimeTable.getSubjectName();
            String[] levelName = level.split(" ");
            String currStartHour = startTime[0];
            String currEndHour = endTime[0];
            int startMinute = Integer.parseInt(startTime[2]);
            int endMinute = Integer.parseInt(endTime[2]);
            boolean startCounting = false;

            if (!startTime[0].equals("12") && startTime[3].equals("PM")) {
                currStartHour = Integer.parseInt(startTime[0]) + 12 + "";
            }

            if (!endTime[0].equals("12") && endTime[3].equals("PM")) {
                currEndHour = Integer.parseInt(endTime[0]) + 12 + "";
            }

            int hourClass;
            int minuteHeight = 0;

            if (startMinute != endMinute) {
                if ((endMinute - startMinute) < 0) {
                    hourClass = Integer.parseInt(currEndHour) - Integer.parseInt(currStartHour) - 1;
                } else {
                    hourClass = Integer.parseInt(currEndHour) - Integer.parseInt(currStartHour);
                }
            } else {
                hourClass = Integer.parseInt(currEndHour) - Integer.parseInt(currStartHour);
            }

            for (int j = 0; j < time.getChildCount(); j++) {
                String[] tempTime = time.getChildAt(j).getTag().toString().split(" ");

                if (startTime[0].equals(tempTime[0]) && startTime[3].equals(tempTime[3])) {
                    if ((Integer.parseInt(tempTime[2]) + 30) > Integer.parseInt(startTime[2])) {
                        if (currentYPosition == 0) {
                            if (hourClass < 0)
                                hourClass = 0;
                            Class.setText(subjectName + "\n" + "class" + "\n" + levelName[0].substring(0, 4) + " " + levelName[1] + "\n" + saveTimeTable.getStartTime() + "\n - \n" + saveTimeTable.getEndTime());
                            Class.setWidth((int) (width / 4.32));
                            Class.setTextColor(Color.BLACK);
                            Class.setGravity(Gravity.CENTER);
                            Class.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_rectangle));

                            if ((Integer.parseInt(tempTime[2])) == Integer.parseInt(startTime[2])) {
                                currentYPosition = (int) (height / 12.8) * j;
                                Class.setY(currentYPosition);
                            } else {
                                currentYPosition = (int) (height / 12.8) * j + (int) (height / 25.6);
                                Class.setY(currentYPosition);
                            }
                            storeYPosition.add(currentYPosition);

                            if (startMinute == endMinute) {
                                Class.setHeight((int) (height / 12.8) * (hourClass * 2) + minuteHeight);
                                classOfHour.add((int) (height / 12.8) * (hourClass * 2) + minuteHeight);
                                break;
                            }
                        }
                    }
                }

                if (endTime[0].equals(tempTime[0]) && endTime[3].equals(tempTime[3])) {
                    Log.d("test", tempTime[0] + tempTime[2] + tempTime[3] + "");
                    Log.d("test", saveTimeTable.getEndTime() + "");

                    if ((Integer.parseInt(tempTime[2]) + 30) >= Integer.parseInt(endTime[2])) {
                        minuteHeight += (int) (height / 25.6);
                        Log.d("end2", tempTime[0] + tempTime[2] + tempTime[3] + "");
                        Log.d("end2", saveTimeTable.getEndTime() + "");
                    } else {
                        minuteHeight += (int) (height / 12.8);
                        Log.d("end2", tempTime[0] + tempTime[2] + tempTime[3] + "");
                        Log.d("end2", saveTimeTable.getEndTime() + "");
                    }

                    if ((Integer.parseInt(tempTime[2]) + 30) > Integer.parseInt(endTime[2])) {
                        Class.setHeight((int) (height / 12.8) * (hourClass * 2) + minuteHeight);
                        classOfHour.add((int) (height / 12.8) * (hourClass * 2) + minuteHeight);
                        break;
                    }
                }
            }
        }

        boolean overlap = false;
        ArrayList<Integer> yPosition = new ArrayList<>();
        yPosition.addAll(storeYPosition);
        int counter = 0;
        do {
            float currentXPosition = 0;
            for (int j = 0; j < subject.getChildCount(); j++) {
                View v1 = subject.getChildAt(j);
                if (v1.getY() == yPosition.get(counter)) {
                    for (int k = 0; k < subject.getChildCount(); k++) {
                        View v2 = subject.getChildAt(k);
                        if (v2 != v1) {
                            Rect firstView = getBounds(v1, classOfHour.get(j));
                            Rect secondView = getBounds(v2, classOfHour.get(k));
                            if (v2.getY() > v1.getY()) {
                                if (firstView.intersect(secondView)) {
                                    currentXPosition = v2.getX() + (int) (width / 4.32);
                                    v2.setX(currentXPosition);
                                    expandLayout((int) currentXPosition);
                                }
                            } else {
                                if (secondView.intersect(firstView)) {
                                    currentXPosition = v1.getX() + (int) (width / 4.32);
                                    v1.setX(currentXPosition);
                                    expandLayout((int) currentXPosition);
                                }
                            }
                        }
                    }
                }
            }

            if (counter < yPosition.size() - 1) {
                counter++;
            } else {
                if (currentXPosition == 0) {
                    overlap = true;
                } else {
                    counter = 0;
                }
            }

        } while (!overlap);

    }

    public Rect getBounds(View v, float hourPerClass) {
        return new Rect((int) (v.getX()), (int) (v.getY()), (int) (v.getX() + (int) (width / 4.32)), (int) (v.getY() + hourPerClass));
    }

    private void expandLayout(int currX) {
        if (currentWidth <= currX) {
            ViewGroup.LayoutParams lp = subject.getLayoutParams();
            lp.width = currX + (int) (width / 4.32);
            currentWidth = currX + (int) (width / 4.32);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void zoomImageFromThumb(final TextView Class, String content) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        TextView expandedView = (TextView) view.findViewById(R.id.expandedText);
        expandedView.setWidth((int) (width / 4.32));
        expandedView.setHeight((int) (height / 3.84));

        final RelativeLayout expandedLayout = (RelativeLayout) view.findViewById(R.id.expandedLayout);
        expandedView.setText(content);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        Class.getGlobalVisibleRect(startBounds);
        view.findViewById(R.id.timeRelative).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        Class.setAlpha(0f);
        expandedLayout.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedLayout.setPivotX(0f);
        expandedLayout.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedLayout, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedLayout, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedLayout, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedLayout,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedLayout, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedLayout,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedLayout,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedLayout,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Class.setAlpha(1f);
                        expandedLayout.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Class.setAlpha(1f);
                        expandedLayout.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

}
