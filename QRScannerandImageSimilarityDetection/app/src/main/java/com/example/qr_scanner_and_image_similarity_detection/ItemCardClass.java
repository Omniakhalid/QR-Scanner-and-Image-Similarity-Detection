package com.example.qr_scanner_and_image_similarity_detection;

import android.graphics.Bitmap;

public class ItemCardClass {
    private String Discreaption;
    private String Cat_ID;
    private Bitmap ImageSource;
    private int DeleteItem;
    private boolean itemLostchecked;

    public ItemCardClass(){
    }

    public String getDiscreaption() {
        return Discreaption;
    }

    public String getmCategory() {
        return Cat_ID;
    }

    public Bitmap getImageSource() {
        return ImageSource;
    }

    public boolean isItemLostSwitch() {
        return itemLostchecked;
    }

    public int getDeleteItem() {
        return DeleteItem;
    }

    public void setDiscreaption(String discreaption) {
        Discreaption = discreaption;
    }

    public void setmCategory(String cat_ID) {
        this.Cat_ID = cat_ID;
    }

    public void setImageSource(Bitmap imageSource) {
        ImageSource = imageSource;
    }

    public void setItemLostSwitch(boolean itemLosted) {
        this.itemLostchecked = itemLosted;
    }

    public void setDeleteItem(int deleteItem) {
        DeleteItem = deleteItem;
    }

}
