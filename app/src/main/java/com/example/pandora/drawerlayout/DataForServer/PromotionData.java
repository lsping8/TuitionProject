package com.example.pandora.drawerlayout.DataForServer;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Pandora on 9/30/2016.
 */

public class PromotionData {

    private ArrayList<StorePromotion> storePromotionArrayList;

    public PromotionData(){
        storePromotionArrayList = new ArrayList<>();
    }

    public ArrayList<StorePromotion> getStorePromotionArrayList(){
        return storePromotionArrayList;
    }

    public void addNewDate(String startDate,String endDate,String promotionText,Bitmap promotionImage){
        storePromotionArrayList.add(0,new StorePromotion(startDate,endDate,promotionText,promotionImage));
    }

    public class StorePromotion{

        String startDate,endDate,promotionText;
        Bitmap promotionImage;

        private StorePromotion(String startDate,String endDate,String promotionText,Bitmap promotionImage){
            this.startDate = startDate;
            this.endDate = endDate;
            this.promotionText = promotionText;
            this.promotionImage = promotionImage;
        }

        public String getStartDate(){
            return startDate;
        }

        public String getEndDate(){
            return endDate;
        }

        public String getPromotionText(){
            return promotionText;
        }

        public Bitmap getPromotionImage(){
            return promotionImage;
        }

    }
}
