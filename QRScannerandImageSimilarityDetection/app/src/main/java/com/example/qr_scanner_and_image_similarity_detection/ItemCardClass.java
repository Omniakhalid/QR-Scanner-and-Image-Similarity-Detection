package com.example.qr_scanner_and_image_similarity_detection;

import android.graphics.Bitmap;

public class ItemCardClass {
    private String Discreaption;
    private String mCategory;
    private Bitmap ImageSource;
    public ItemCardClass(){
    }

    public String getDiscreaption() {
        return Discreaption;
    }

    public String getmCategory() {
        return mCategory;
    }

    public Bitmap getImageSource() {
        return ImageSource;
    }

    public void setDiscreaption(String discreaption) {
        Discreaption = discreaption;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public void setImageSource(Bitmap imageSource) {
        ImageSource = imageSource;
    }
}
