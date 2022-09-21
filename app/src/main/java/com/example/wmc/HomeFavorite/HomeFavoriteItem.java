package com.example.wmc.HomeFavorite;

public class HomeFavoriteItem {

    String cafeName;
    String tag1;
    String tag2;
    String cafeImage;

    public HomeFavoriteItem(String cafeName, String tag1, String tag2, String cafeImage) {
        this.cafeName = cafeName;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.cafeImage = cafeImage;
    }


    public String getCafeName() {
        return cafeName;
    }

    public void setCafeName(String cafeName) {
        this.cafeName = cafeName;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getCafeImage() {
        return cafeImage;
    }

    public void setCafeImage(String cafeImage) {
        this.cafeImage = cafeImage;
    }
}
