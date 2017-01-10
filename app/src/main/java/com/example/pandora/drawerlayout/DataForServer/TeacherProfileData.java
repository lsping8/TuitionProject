package com.example.pandora.drawerlayout.DataForServer;

import java.util.ArrayList;

/**
 * Created by Pandora on 10/4/2016.
 */

public class TeacherProfileData {

    private String teacherName,teacherContact,race,teachingExp;
    private ArrayList<String> cert,teachingIn,subjectTeaching;

    public TeacherProfileData(){

        cert = new ArrayList<>();
        teachingIn = new ArrayList<>();
        subjectTeaching = new ArrayList<>();

        teacherName = "Haji Mohammad Najib bin Tun Haji Abdul Razak";
        teacherContact = "123-4567890";
        race = "Unknown";
        teachingExp = "100";

        for (int i=0;i<3;i++){
            teachingIn.add("Somewhere");
        }



        for (int i=0;i<3;i++){
            subjectTeaching.add("Something");
        }
    }

    public String getTeacherName(){
        return teacherName;
    }

    public String getTeacherContact(){
        return teacherContact;
    }

    public String getRace(){
        return race;
    }

    public String getTeachingExp(){
        return teachingExp;
    }

    public ArrayList<String> getCert(){
        return cert;
    }

    public ArrayList<String> getTeachingIn(){
        return teachingIn;
    }

    public ArrayList<String> getSubjectTeaching(){
        return subjectTeaching;
    }

    public void updateData(){

    }
}
