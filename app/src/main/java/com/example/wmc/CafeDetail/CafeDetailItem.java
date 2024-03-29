package com.example.wmc.CafeDetail;

public class CafeDetailItem {

    String reviewNickName;
    String level_and_location;
    String review_comment;
    String review_writeTime;
    String reviewProfile_image;
    String reviewImage;
    String good_count_textView;
    boolean check_user_flag;
    boolean check_love_flag; // love 테이블에 memNum 일치 여부
    Long mem_num;
    Long get_cafe_num;
    Long get_love_num;
    Long get_review_num;
    boolean locationCheck_flag;

    public CafeDetailItem(String reviewNickName, String level_and_location, String review_comment, String review_writeTime, String reviewProfile_image, String reviewImage, String good_count_textView, boolean check_user_flag, boolean check_love_flag, Long mem_num, Long get_cafe_num, Long get_love_num, Long get_review_num, Boolean locationCheck_flag) {
        this.reviewNickName = reviewNickName;
        this.level_and_location = level_and_location;
        this.review_comment = review_comment;
        this.review_writeTime = review_writeTime;
        this.reviewProfile_image = reviewProfile_image;
        this.reviewImage = reviewImage;
        this.good_count_textView = good_count_textView;
        this.check_user_flag = check_user_flag;
        this.check_love_flag = check_love_flag;
        this.mem_num = mem_num;
        this.get_cafe_num = get_cafe_num;
        this.get_love_num = get_love_num;
        this.get_review_num = get_review_num;
        this.locationCheck_flag = locationCheck_flag;
    }

    public String getReviewNickName() {
        return reviewNickName;
    }

    public void setReviewNickName(String reviewNickName) {
        reviewNickName = reviewNickName;
    }

    public String getLevel_and_location() {
        return level_and_location;
    }

    public void setLevel_and_location(String level_and_location) {
        this.level_and_location = level_and_location;
    }

    public String getReview_comment() {
        return review_comment;
    }

    public void setReview_comment(String review_comment) {
        this.review_comment = review_comment;
    }

    public String getReview_writeTime() {
        return review_writeTime;
    }

    public void setReview_writeTime(String review_writeTime) {
        this.review_writeTime = review_writeTime;
    }

    public String getReviewProfile_image() {
        return reviewProfile_image;
    }

    public void setReviewProfile_image(String reviewProfile_image) {
        this.reviewProfile_image = reviewProfile_image;
    }

    public String getReviewImage() {
        return reviewImage;
    }

    public void setReviewImage(String reviewImage) {
        this.reviewImage = reviewImage;
    }

    public String getGood_count_textView() {
        return good_count_textView;
    }

    public void setGood_count_textView(String good_count_textView) {
        this.good_count_textView = good_count_textView;
    }

    public boolean getCheck_user_flag(){
        return check_user_flag;
    }

    public boolean getCheck_love_flag(){ return check_love_flag; }

    public Long getMem_num() {return mem_num;}

    public Long getGet_cafe_num() {return get_cafe_num;}

    public Long getGet_love_num() { return get_love_num; }

    public Long getGet_review_num() { return get_review_num; }

    public boolean isLocationCheck_flag() {
        return locationCheck_flag;
    }

    public void setLocationCheck_flag(boolean locationCheck_flag) {
        this.locationCheck_flag = locationCheck_flag;
    }
}
