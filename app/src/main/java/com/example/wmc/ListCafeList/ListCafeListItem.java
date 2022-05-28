package com.example.wmc.ListCafeList;

public class ListCafeListItem {

    String cafeList_cafeName;
    String cafeList_cafeAddress;
    String openTime;
    String tag1;
    String tag2;
    int cafeList_cafeImage;
    boolean check_user_flag = false;

    Long get_cafe_num;
    Long get_bookmark_num;

    public ListCafeListItem(String cafeList_cafeName, String cafeList_cafeAddress, String openTime, String tag1, String tag2, int cafeList_cafeImage, boolean check_user_flag, Long get_cafe_num, Long get_bookmark_num) {
        this.cafeList_cafeName = cafeList_cafeName;
        this.cafeList_cafeAddress = cafeList_cafeAddress;
        this.openTime = openTime;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.cafeList_cafeImage = cafeList_cafeImage;
        this.check_user_flag = check_user_flag;
        this.get_cafe_num = get_cafe_num;
        this.get_bookmark_num = get_bookmark_num;
    }

    public String getCafeList_cafeName() {
        return cafeList_cafeName;
    }

    public void setCafeList_cafeName(String cafeList_cafeName) {
        this.cafeList_cafeName = cafeList_cafeName;
    }

    public String getCafeList_cafeAddress() {
        return cafeList_cafeAddress;
    }

    public void setCafeList_cafeAddress(String cafeList_cafeAddress) {
        this.cafeList_cafeAddress = cafeList_cafeAddress;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
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

    public int getCafeList_cafeImage() {
        return cafeList_cafeImage;
    }

    public void setCafeList_cafeImage(int cafeList_cafeImage) {
        this.cafeList_cafeImage = cafeList_cafeImage;
    }

    public boolean getCheck_user_flag(){return check_user_flag;}

    public Long getGet_cafe_num(){ return get_cafe_num; }

    public Long getGet_bookmark_num() { return get_bookmark_num; }
}
