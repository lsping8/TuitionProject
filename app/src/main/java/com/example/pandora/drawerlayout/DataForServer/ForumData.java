package com.example.pandora.drawerlayout.DataForServer;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pandora on 9/29/2016.
 */

public class ForumData {

    private ArrayList<Data> dataArray;
    private  ArrayList<MaterialData> materialData;
    private  ArrayList<NoticeData> noticeData;
    private List<String> topic;

    public ForumData(){
        dataArray = new ArrayList<>();
        materialData = new ArrayList<>();
        noticeData = new ArrayList<>();
        topic = new ArrayList<>();
    }

    public ArrayList<Data> getDataArray(){
        return dataArray;
    }

    public ArrayList<MaterialData> getMaterialData(){
        return materialData;
    }

    public ArrayList<NoticeData> getNoticeData(){
        return noticeData;
    }

    public List<String> getTopic(){
        return topic;
    }

}
