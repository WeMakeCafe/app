package com.example.wmc.HomeTag2ViewPager;

public class HomeTag2ViewPagerItem {

    String cafeName;
    String cafeAddress;
    String tag1;
    String tag2;
    String tag3;
    String review;
    String cafeImage;
    int rating;
    Double cafe_distance;

    public HomeTag2ViewPagerItem(String cafeName, String cafeAddress, String tag1, String tag2, String tag3, String review, String cafeImage, int rating, Double cafe_distance) {
        this.cafeName = cafeName;
        this.cafeAddress = cafeAddress;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.review = review;
        this.cafeImage = cafeImage;
        this.rating = rating;
        this.cafe_distance = cafe_distance;
    }

    public String getCafeName() {
        return cafeName;
    }

    public void setCafeName(String cafeName) {
        this.cafeName = cafeName;
    }

    public String getCafeAddress() {
        return cafeAddress;
    }

    public void setCafeAddress(String cafeAddress) {
        this.cafeAddress = cafeAddress;
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

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCafeImage() {
        return cafeImage;
    }

    public void setCafeImage(String cafeImage) {
        this.cafeImage = cafeImage;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Double getCafe_distance() {
        return cafe_distance;
    }

    public void setCafe_distance(Double cafe_distance) {
        this.cafe_distance = cafe_distance;
    }
}
