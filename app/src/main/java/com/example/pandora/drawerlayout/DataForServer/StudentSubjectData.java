package com.example.pandora.drawerlayout.DataForServer;

import java.util.ArrayList;

/**
 * Created by Pandora on 10/3/2016.
 */

public class StudentSubjectData {

    private ArrayList<SubjectData> studentSubjectData;

    public StudentSubjectData() {

        studentSubjectData = new ArrayList<>();
        String[] subject = {"Bahasa Malaysia", "English", "Mathematics", "Science"};

        for (int i = 0; i < subject.length; i++) {
            studentSubjectData.add(new SubjectData((i + 1 + ""), subject[i], ""));
        }
    }

    public ArrayList<SubjectData> getStudentSubjectData() {
        return studentSubjectData;
    }

    public void addData(String subjectName) {
        studentSubjectData.add(new SubjectData(studentSubjectData.size() + 1 + "", subjectName, ""));
    }

    public class SubjectData {

        String number, subjectName, mark;

        public SubjectData(String number, String subjectName, String mark) {
            this.number = number;
            this.subjectName = subjectName;
            this.mark = mark;
        }

        public String getNumber() {
            return number;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }
    }
}
