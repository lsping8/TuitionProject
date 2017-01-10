package com.example.pandora.drawerlayout.DataForServer;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pandora on 9/14/2016.
 */
public class SaveFile {

    private HashMap<String, SaveClassDay> centreTimeTable;
    private CentreSubject centreSubject;

    public SaveFile() {
        centreTimeTable = new HashMap<>();

        for (int i = 0; i < 6; i++) {
            String level = "STANDARD " + (i + 1);
            centreTimeTable.put(level, new SaveClassDay());
        }

        for (int i = 0; i < 5; i++) {
            String level = "FORM " + (i + 1);
            centreTimeTable.put(level, new SaveClassDay());
        }

        centreSubject = new CentreSubject();
    }

    public ArrayList<SaveClassDay.SaveTimeTable> getPerDayClass(String level, String day) {
        return centreTimeTable.get(level).getPerDayClass(day);
    }

    public void setClassDetail(String startTime, String endTime, String subjectName, String day, String level,int position) {
        centreTimeTable.get(level).setClassDetail(startTime, endTime, subjectName, day,position);
        centreSubject.setSubject(level, subjectName);
    }

    public Integer getClassDetailSize(String day, String level) {
        return centreTimeTable.get(level).getClassDetailSize(day);
    }


    public ArrayList<String> getSubjectClass(String level) {
        return centreSubject.subjectByLevel.get(level);
    }

    //------------------------------------------ SaveClassDay --------------------------------------------------//

    public class SaveClassDay {

        HashMap<String, ArrayList<SaveTimeTable>> day;
        ArrayList<SaveTimeTable> saveTimeTables;

        private SaveClassDay() {
            day = new HashMap<>();
            setDay();
        }

        private void setDay() {
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

            for (String day1 : days) {
                saveTimeTables = new ArrayList<>();
                day.put(day1, saveTimeTables);
            }
        }

        private ArrayList<SaveTimeTable> getPerDayClass(String day) {
            return this.day.get(day);
        }

        private void setClassDetail(String startTime, String endTime, String subjectName, String day,int position) {
            if (position < 0){
                this.day.get(day).add(new SaveTimeTable(startTime, endTime, subjectName));
            }else {
                this.day.get(day).add(position, new SaveTimeTable(startTime, endTime, subjectName));
            }
        }

        private Integer getClassDetailSize(String day) {
            return this.day.get(day).size();
        }

        //------------------------------------------ SaveTimeTable --------------------------------------------------//

        public class SaveTimeTable {

            String startTime, endTime, subjectName;

            private SaveTimeTable(String startTime, String endTime, String subjectName) {
                this.startTime = startTime;
                this.endTime = endTime;
                this.subjectName = subjectName;
            }

            public String getStartTime(){
                return startTime;
            }

            public String getEndTime(){
                return endTime;
            }

            public String getSubjectName(){
                return subjectName;
            }
        }
    }

    //------------------------------------------ CentreSubject --------------------------------------------------//

    private class CentreSubject {

        HashMap<String, ArrayList<String>> subjectByLevel;
        ArrayList<String> subjectName;

        private CentreSubject() {
            subjectByLevel = new HashMap<>();
            for (int i = 0; i < 6; i++) {
                String level = "STANDARD " + (i + 1);
                subjectByLevel.put(level, null);
            }

            for (int i = 0; i < 5; i++) {
                String level = "FORM " + (i + 1);
                subjectByLevel.put(level, null);
            }
        }

        private void setSubject(String level, String subjectName) {

            if (subjectByLevel.get(level) == null) {
                this.subjectName = new ArrayList<>();
                this.subjectName.add(subjectName);
                subjectByLevel.put(level, this.subjectName);
            } else {
                this.subjectName = subjectByLevel.get(level);
                for (int i = 0; i < this.subjectName.size(); i++) {
                    if (this.subjectName.get(i).equals(subjectName))
                        return;
                }
                this.subjectName.add(subjectName);
                subjectByLevel.put(level, this.subjectName);
            }
        }
    }
}
