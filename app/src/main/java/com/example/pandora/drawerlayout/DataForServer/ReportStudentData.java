package com.example.pandora.drawerlayout.DataForServer;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.pandora.drawerlayout.BitmapConfig;
import com.example.pandora.drawerlayout.R;

import java.util.ArrayList;

/**
 * Created by Pandora on 10/1/2016.
 */

public class ReportStudentData {

    private ArrayList<StudentData> studentData;

    public ReportStudentData(float width, float height, BitmapConfig bitmapConfig, Context context){
        studentData = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Bitmap bitmap = bitmapConfig.getCircleBitmap(bitmapConfig.decodeSampledBitmapFromResource(context.getResources(), R.drawable.rsz_sohai, (int) width / 6, (int) width / 6), 0);
            String name = "Haji Mohammad Najib bin Tun Haji Abdul Razak";
            addData(bitmap,name,4+"",0+"");
        }

    }

    public ArrayList<StudentData> getStudentData(){
        return studentData;
    }

    public void addData(Bitmap studentImage,String studentName,String totalSubject,String totalReport){
        studentData.add(new StudentData(studentImage,studentName,totalSubject,totalReport));
    }

    public class StudentData{

        Bitmap studentImage;
        String studentName,totalSubject,totalReport;

        public StudentData(Bitmap studentImage,String studentName,String totalSubject,String totalReport){
            this.studentImage = studentImage;
            this.studentName = studentName;
            this.totalSubject = totalSubject;
            this.totalReport = totalReport;
        }

        public Bitmap getStudentImage(){
            return studentImage;
        }

        public String getStudentName(){
            return studentName;
        }

        public String getTotalSubject(){
            return totalSubject;
        }

        public String getTotalReport(){
            return totalReport;
        }

        public void setTotalReport(String report){
            if (report.equals(totalSubject)){
                totalReport = "OK";
            }else{
                totalReport = report;
            }
        }

        public void setTotalSubject(String totalReport){
            this.totalReport = totalReport;
        }
    }
}
