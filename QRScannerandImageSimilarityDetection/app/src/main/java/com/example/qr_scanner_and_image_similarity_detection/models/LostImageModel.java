package com.example.qr_scanner_and_image_similarity_detection.models;

import android.graphics.Bitmap;

public class LostImageModel implements Comparable<LostImageModel>{
    private String UserId;
    private int similarity;
    private Bitmap img_bitmap;

    public LostImageModel() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }

    public Bitmap getImg_bitmap() {
        return img_bitmap;
    }

    public void setImg_bitmap(Bitmap img_bitmap) {
        this.img_bitmap = img_bitmap;
    }

    @Override
    public int compareTo(LostImageModel lostImageModel) {
        int compareSimilarity = lostImageModel.getSimilarity();

        //descending order
        return compareSimilarity - this.similarity;
    }
}
