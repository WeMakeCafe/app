package com.example.wmc.CafeDetailMore;

public class CafeDetailMoreItem {

    String reviewNickName;
    String level_and_location;
    String review_comment;
    String reviewMore_writeTime;
    String reviewProfile_image;
    String reviewImage1;
    String reviewImage2;
    String reviewImage3;
    String good_count_textView;
    Long mem_num;
    Long get_cafe_num;
    Long get_love_num;
    Long get_review_num;
    boolean check_user_flag;
    boolean check_love_flag;
    boolean location_flag;

    public CafeDetailMoreItem(String reviewNickName, String level_and_location, String review_comment, String reviewMore_writeTime, String reviewProfile_image, String reviewImage1, String reviewImage2, String reviewImage3, String good_count_textView, boolean check_user_flag, boolean check_love_flag, Long get_cafe_num, Long mem_num, Long get_love_num, Long get_review_num, Boolean location_flag) {
        this.reviewNickName = reviewNickName;
        this.level_and_location = level_and_location;
        this.review_comment = review_comment;
        this.reviewMore_writeTime = reviewMore_writeTime;
        this.reviewProfile_image = reviewProfile_image;
        this.reviewImage1 = reviewImage1;
        this.reviewImage2 = reviewImage2;
        this.reviewImage3 = reviewImage3;
        this.good_count_textView = good_count_textView;
        this.check_user_flag = check_user_flag;
        this.check_love_flag = check_love_flag;
        this.get_cafe_num = get_cafe_num;
        this.mem_num = mem_num;
        this.get_love_num = get_love_num;
        this.get_review_num = get_review_num;
        this.location_flag = location_flag;
    }

    public String getReviewNickName() {
        return reviewNickName;
    }

    public void setReviewNickName(String reviewNickName) {
        this.reviewNickName = reviewNickName;
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

    public String getReviewMore_writeTime() {
        return reviewMore_writeTime;
    }

    public void setReviewMore_writeTime(String reviewMore_writeTime) {
        this.reviewMore_writeTime = reviewMore_writeTime;
    }

    public String getReviewProfile_image() {
        return reviewProfile_image;
    }

    public void setReviewProfile_image(String reviewProfile_image) {
        this.reviewProfile_image = reviewProfile_image;
    }

    public String getReviewImage1() {
        return reviewImage1;
    }

    public void setReviewImage1(String reviewImage1) {
        this.reviewImage1 = reviewImage1;
    }

    public String getReviewImage2() {
        return reviewImage2;
    }

    public void setReviewImage2(String reviewImage2) {
        this.reviewImage2 = reviewImage2;
    }

    public String getReviewImage3() {
        return reviewImage3;
    }

    public void setReviewImage3(String reviewImage3) {
        this.reviewImage3 = reviewImage3;
    }

    public String getGood_count_textView() {
        return good_count_textView;
    }

    public void setGood_count_textView(String good_count_textView) {
        this.good_count_textView = good_count_textView;
    }

    public boolean getCheck_user_flag() {
        return check_user_flag;
    }

    public void setCheck_user_flag(boolean check_user_flag) {
        this.check_user_flag = check_user_flag;
    }

    public boolean getCheck_love_flag(){
        return check_love_flag;
    }

    public Long getGet_cafe_num() {return get_cafe_num;}

    public Long getMem_num() {return mem_num;}

    public Long getGet_review_num() { return get_review_num; }

    public Long getGet_love_num() { return get_love_num; }

    public boolean isLocation_flag() {
        return location_flag;
    }

    public void setLocation_flag(boolean location_flag) {
        this.location_flag = location_flag;
    }
}
