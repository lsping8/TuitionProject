package com.example.pandora.drawerlayout.DataForServer;

import android.graphics.Bitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pandora on 9/28/2016.
 */

public class MaterialData {

    private String folderName;
    private ArrayList<FileName> fileNames;

    public MaterialData(String folderName){
        this.folderName = folderName;
        fileNames = new ArrayList<>();
    }

    public void setNewFile(String fileName, Bitmap bitmap){
        fileNames.add(new FileName(fileName,bitmap));
    }

    public String getFolderName(){
        return folderName;
    }

    public ArrayList<FileName> getFileNames(){
        return fileNames;
    }

    public FileName getFile(int position){
        return fileNames.get(position);
    }

    public class FileName{

        String fileName;
        Bitmap thumbnail;

        public FileName(String fileName,Bitmap bitmap){
            this.fileName = fileName;
            thumbnail = bitmap;
        }

        public String getFileName(){
            return fileName;
        }

        public Bitmap getThumbnail(){
            return thumbnail;
        }
    }
}
