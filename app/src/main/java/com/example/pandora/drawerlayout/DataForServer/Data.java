package com.example.pandora.drawerlayout.DataForServer;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Pandora on 9/25/2016.
 */

public class Data {

    private String userName, description , like,comment;
    private String postTime,topic;
    private int imageId;
    private Bitmap bitmap = null;
    private ArrayList<Comment> commentsClass;
    private ArrayList<String> listTopic;

    public Data(String userName, String postTime, String description,String topic, int like, int comment, int imageId,Bitmap bitmap) {
        commentsClass = new ArrayList<>();
        listTopic = new ArrayList<>();
        this.userName = userName;
        this.postTime = postTime;
        this.imageId = imageId;
        this.description = description;
        this.topic = topic;

        if (bitmap != null)
            this.bitmap = bitmap;

        if (like != 0 || comment != 0){
            if (comment !=0){
                this.comment = comment + " comment";
            }

            if (like != 0){
                this.like = like + " like";
            }
        }else{
            this.like = "";
            this.comment = "";
        }
    }

    public ArrayList<Comment> getCommentsClass(){
        return commentsClass;
    }

    public ArrayList<String> getListTopic(){
        return listTopic;
    }

    public String getUserName(){
        return userName;
    }

    public String getDescription(){
        return description;
    }

    public String getLike(){
        return like;
    }

    public String getComment(){
        return comment;
    }

    public String getPostTime(){
        return postTime;
    }

    public String getTopic(){
        return topic;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getImageId(){
        return imageId;
    }

    public void addComment(int userImage,String UserName,String postTime,String description){
        commentsClass.add(new Comment(userImage,UserName,postTime,description));
    }

    public class Comment{

        private int userImage;
        private String userName,postTime,description;

        public Comment(int userImage,String UserName,String postTime,String description){
            this.userImage = userImage;
            this.userName = UserName;
            this.postTime = postTime;
            this.description = description;
        }

        public String getUserName(){
            return userName;
        }

        public String getPostTime(){
            return postTime;
        }

        public String getDescription(){
            return description;
        }

        public int getUserImage(){
            return userImage;
        }
    }
}
