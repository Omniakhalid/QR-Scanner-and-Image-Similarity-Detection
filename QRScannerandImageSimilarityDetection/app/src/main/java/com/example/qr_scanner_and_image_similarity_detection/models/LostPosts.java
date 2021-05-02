package com.example.qr_scanner_and_image_similarity_detection.models;

public class LostPosts {
    private String User_ID;
    private String image_Lostim;
    private String discreaption_LostItm;
    private int ChatIcon;

    public LostPosts(){
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getImage_Lostim() {
        return image_Lostim;
    }

    public void setImage_Lostim(String image_Lostim) {
        this.image_Lostim = image_Lostim;
    }

    public String getDiscreaption_LostItm() {
        return discreaption_LostItm;
    }

    public void setDiscreaption_LostItm(String discreaption_LostItm) {
        this.discreaption_LostItm = discreaption_LostItm;
    }

    public int getChatIcon() {
        return ChatIcon;
    }

    public void setChatIcon(int chatIcon) {
        ChatIcon = chatIcon;
    }
}
