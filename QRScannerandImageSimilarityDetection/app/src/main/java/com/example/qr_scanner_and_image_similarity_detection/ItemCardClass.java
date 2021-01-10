package com.example.qr_scanner_and_image_similarity_detection;

import android.graphics.Bitmap;

public class CardExample {
    private String Discreaption;
    private String mCategory;
    private Bitmap Save;
    public CardExample(String text1, String text2,Bitmap imageResource){
        Discreaption=text1;
        mCategory=text2;
        Save=imageResource;
    }

    public String getMyText1() {
        return Discreaption;
    }

    public String getMyText2() {
        return mCategory;
    }

    public Bitmap getmImageResource() {
        return Save;
    }
}
