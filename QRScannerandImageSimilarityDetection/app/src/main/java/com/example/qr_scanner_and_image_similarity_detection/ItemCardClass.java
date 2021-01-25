package com.example.qr_scanner_and_image_similarity_detection;

import android.graphics.Bitmap;

public class ItemCardClass {
    private String discreaption;
    private String cat_ID;
    private String imageSource;
    private int deleteItem;
    private boolean itemLostchecked;

    public ItemCardClass(){
    }

    public String getDiscreaption() {
        return discreaption;
    }

    public void setDiscreaption(String discreaption) {
        this.discreaption = discreaption;
    }

    public String getCat_ID() {
        return cat_ID;
    }

    public void setCat_ID(String cat_ID) {
        this.cat_ID = cat_ID;
    }

    public int getDeleteItem() {
        return deleteItem;
    }

    public void setDeleteItem(int deleteItem) {
        this.deleteItem = deleteItem;
    }

    public boolean isItemLostchecked() {
        return itemLostchecked;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public void setItemLostchecked(boolean itemLostchecked) {
        this.itemLostchecked = itemLostchecked;
    }
}
