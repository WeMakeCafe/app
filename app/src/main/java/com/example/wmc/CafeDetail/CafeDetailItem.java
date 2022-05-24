package com.example.wmc.CafeDetail;

public class CafeDetailItem {

    String reviewNickName;
    String level_and_location;
    String review_comment;
    String review_writeTime;
    int reviewProfile_image;
    int reviewImage;
    String good_count_textView;
    boolean check_user_flag;
    Long mem_num;
    Long get_cafe_num;

    public CafeDetailItem(String reviewNickName, String level_and_location, String review_comment, String review_writeTime, int reviewProfile_image, int reviewImage, String good_count_textView, boolean check_user_flag, Long mem_num, Long get_cafe_num) {
        this.reviewNickName = reviewNickName;
        this.level_and_location = level_and_location;
        this.review_comment = review_comment;
        this.review_writeTime = review_writeTime;
        this.reviewProfile_image = reviewProfile_image;
        this.reviewImage = reviewImage;
        this.good_count_textView = good_count_textView;
        this.check_user_flag = check_user_flag;
        this.mem_num = mem_num;
        this.get_cafe_num = get_cafe_num;
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

    public int getReviewProfile_image() {
        return reviewProfile_image;
    }

    public void setReviewProfile_image(int reviewProfile_image) {
        this.reviewProfile_image = reviewProfile_image;
    }

    public int getReviewImage() {
        return reviewImage;
    }

    public void setReviewImage(int reviewImage) {
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

    public Long getMem_num() {return mem_num;}

    public Long getGet_cafe_num() {return get_cafe_num;}
}
