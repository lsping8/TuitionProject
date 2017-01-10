package com.example.pandora.drawerlayout.DataForServer;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.TextView;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;

import java.util.ArrayList;

/**
 * Created by Pandora on 10/1/2016.
 */

public class StudentInClassData {

    private ArrayList<StudentInfo> studentInfo;

    public StudentInClassData(float width, float height, BitmapConfig bitmapConfig, Context context){
        studentInfo = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Bitmap bitmap = bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(context.getResources(), R.drawable.rsz_sohai, (int) width / 6, (int) width / 6), 0);
            String name = "Haji Mohammad Najib bin Tun Haji Abdul Razak";
            addData(bitmap, name, false);
        }
    }

    public StudentInClassData(float width, float height, BitmapConfig bitmapConfig, Context context,String blueCounter){
        studentInfo = new ArrayList<>();
        int blueInt = Integer.parseInt(blueCounter);

        for (int i = 0; i < blueInt; i++) {
            Bitmap bitmap = bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(context.getResources(), R.drawable.rsz_sohai, (int) width / 6, (int) width / 6), 0);
            String name = "Haji Mohammad Najib bin Tun Haji Abdul Razak";
            addData(bitmap, name, false);
        }
    }

    public ArrayList<StudentInfo> getStudentInfo(){
        return studentInfo;
    }

    public void addData(Bitmap studentImage,String studentName,boolean check){
        studentInfo.add(new StudentInfo(studentImage,studentName,check));
    }



    public class StudentInfo{

        Bitmap studentImage;
        String studentName;
        boolean check;

        private StudentInfo(Bitmap studentImage,String studentName,boolean check){

            this.studentImage = studentImage;
            this.studentName = studentName;
            this.check = check;
        }

        public Bitmap getStudentImage(){
            return studentImage;
        }

        public String getStudentName(){
            return studentName;
        }

        public boolean getCheck(){
            return check;
        }

        public void setCheck(boolean check){
            this.check = check;
        }
    }
}
