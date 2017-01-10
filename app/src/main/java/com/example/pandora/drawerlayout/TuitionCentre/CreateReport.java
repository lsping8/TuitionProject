package com.example.pandora.drawerlayout.TuitionCentre;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.DataForServer.StudentSubjectData;
import com.example.pandora.drawerlayout.R;

/**
 * Created by Pandora on 9/7/2016.
 */
public class CreateReport extends AppCompatActivity {

    float height, width;
    ImageView studentImage;
    TextView studentName, submit, addSubject;
    LinearLayout label;
    BitmapConfig bitmapConfig;
    RecyclerView recyclerView;
    CreateReportRecycleView adapter;
    StudentSubjectData studentSubjectData;
    int counter = 0;
    InputMethodManager imm;

    public CreateReport() {
        studentSubjectData = new StudentSubjectData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        bitmapConfig = new BitmapConfig();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        studentImage = (ImageView) findViewById(R.id.studentImage);
        studentName = (TextView) findViewById(R.id.studentName);
        submit = (TextView) findViewById(R.id.submit);
        addSubject = (TextView) findViewById(R.id.addSubject);
        label = (LinearLayout) findViewById(R.id.label);

        setStudentImage();
        setStudentName();
        setAddSubject();
        setSubmit();
        setTopLayout();

        recyclerView.setNestedScrollingEnabled(false);
        adapter = new CreateReportRecycleView();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(500);
        recyclerView.setItemAnimator(itemAnimator);
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

    private void setStudentImage() {
        studentImage.setImageBitmap(bitmapConfig.getCircleBitmap(Bitmap.createScaledBitmap(bitmapConfig.decodeSampledBitmapFromResource(getResources(), R.drawable.rsz_sohai, (int) width / 5, (int) width / 5), (int) width / 5, (int) width / 5, true), 0));
    }

    private void setStudentName() {
        studentName.setHeight((int) width / 5);
        studentName.setText("Haji Mohammad Najib bin Tun Haji Abdul Razak");
        studentName.setPadding((int) (width / 108), 0, 0, 0);
        studentName.setBackground(ContextCompat.getDrawable(this, R.drawable.round_edge_rectangle));
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getTag().toString()) {

                case "addSubject":
                    builder();
                    break;

                case "submit":

                    for (int i = 0; i < studentSubjectData.getStudentSubjectData().size(); i++) {
                        if (!studentSubjectData.getStudentSubjectData().get(i).getMark().equals(""))
                            counter++;
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("counterResult", counter + "," + studentSubjectData.getStudentSubjectData().size());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                    break;
            }
        }
    };

    private void builder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setTitle("Input subject name.");

        final EditText input = new EditText(this);
        input.setGravity(Gravity.CENTER);
        input.setLines(1);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().equals("")) {
                    adapter.insert(input.getText().toString());
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                } else {
                    Toast.makeText(CreateReport.this, "Please input subject name.", Toast.LENGTH_SHORT).show();
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                dialog.cancel();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setAddSubject() {
        addSubject.setTag("addSubject");
        addSubject.setOnClickListener(onClickListener);
    }

    private void setSubmit() {
        submit.setTag("submit");
        submit.setOnClickListener(onClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (imm != null)
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public class CreateReportRecycleView extends RecyclerView.Adapter<CreateReportViewHolder> {

        @Override
        public CreateReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_subject_row, parent, false);
            return new CreateReportViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CreateReportViewHolder holder, int position) {
            holder.subjectNumber.setText(studentSubjectData.getStudentSubjectData().get(position).getNumber());
            holder.subjectName.setText(studentSubjectData.getStudentSubjectData().get(position).getSubjectName());
            holder.markSubject.setText(studentSubjectData.getStudentSubjectData().get(position).getMark());
            holder.setTag(position);
        }

        @Override
        public int getItemCount() {
            return studentSubjectData.getStudentSubjectData().size();
        }

        private void insert(String subjectName) {
            studentSubjectData.addData(subjectName);
            recyclerView.getLayoutManager().scrollToPosition(studentSubjectData.getStudentSubjectData().size());
            adapter.notifyItemInserted(studentSubjectData.getStudentSubjectData().size());
        }
    }

    public class CreateReportViewHolder extends RecyclerView.ViewHolder {

        TextView subjectNumber, subjectName, markSubject;

        private CreateReportViewHolder(View itemView) {
            super(itemView);
            subjectNumber = (TextView) itemView.findViewById(R.id.numberSubject);
            subjectName = (TextView) itemView.findViewById(R.id.subjectName);
            markSubject = (TextView) itemView.findViewById(R.id.subjectMark);
        }

        private void setTag(int position) {
            markSubject.setId(position);
            markSubject.setOnClickListener(onClickListener);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderMark(v.getId());
            }
        };

        private void builderMark(final int position) {

            AlertDialog.Builder builder = new AlertDialog.Builder(CreateReport.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            builder.setTitle("Input mark.");

            final EditText input = new EditText(CreateReport.this);
            input.setGravity(Gravity.CENTER);
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            input.setText(studentSubjectData.getStudentSubjectData().get(position).getMark());

            InputFilter[] filters = new InputFilter[1];
            filters[0] = new InputFilter.LengthFilter(3);
            input.setFilters(filters);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    studentSubjectData.getStudentSubjectData().get(position).setMark(input.getText().toString());
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    adapter.notifyItemChanged(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    dialog.cancel();
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}
