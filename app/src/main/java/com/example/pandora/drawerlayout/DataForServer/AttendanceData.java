package com.example.pandora.drawerlayout.DataForServer;

/**
 * Created by Pandora on 10/2/2016.
 */

public class AttendanceData {

    private String className,classTime,classStudent,counter;

    public AttendanceData(String className,String classTime,String classStudent,String counter){
        this.className = className;
        this.classTime = classTime;
        this.classStudent = classStudent;
        this.counter = counter;
    }

    public String getClassName(){
        return className;
    }

    public String getClassTime(){
        return classTime;
    }

    public String getClassStudent(){
        return classStudent;
    }

    public String getCounter(){
        return counter;
    }

    public void setCounter(String counter){
        this.counter = counter;
    }
}
