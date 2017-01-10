package com.example.pandora.drawerlayout.DataForServer;

/**
 * Created by Pandora on 9/29/2016.
 */

public class NoticeData {

    private String announcement,postTime,userName;

    public NoticeData(String announcement,String postTime,String userName){
        this.announcement = announcement;
        this.postTime = postTime;
        this.userName = userName;
    }

    public String getAnnouncement(){
        return announcement;
    }

    public String getPostTime(){
        return postTime;
    }

    public String getUserName(){
        return userName;
    }

}
