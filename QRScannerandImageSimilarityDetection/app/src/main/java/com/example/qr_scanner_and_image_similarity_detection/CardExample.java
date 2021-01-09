package com.example.qr_scanner_and_image_similarity_detection;

import android.graphics.Bitmap;

public class CardExample {
    private String myText1;
    private String myText2;
    private Bitmap mImageResource;
    public CardExample(String text1, String text2,Bitmap imageResource){
        myText1=text1;
        myText2=text2;
        mImageResource=imageResource;
    }

    public String getMyText1() {
        return myText1;
    }

    public String getMyText2() {
        return myText2;
    }

    public Bitmap getmImageResource() {
        return mImageResource;
    }
}
