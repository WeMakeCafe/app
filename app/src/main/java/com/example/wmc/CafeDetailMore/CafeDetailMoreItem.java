package com.example.wmc.CafeDetailMore;

public class CafeDetailMoreItem {

    String reviewNickName;
    String level_and_location;
    String review_comment;
    int reviewProfile_image;
    int reviewImage1;
    int reviewImage2;
    int reviewImage3;
    String good_count_textView;
    boolean check_user_flag;

    public CafeDetailMoreItem(String reviewNickName, String level_and_location, String review_comment, int reviewProfile_image, int reviewImage1, int reviewImage2, int reviewImage3, String good_count_textView, boolean check_user_flag) {
        this.reviewNickName = reviewNickName;
        this.level_and_location = level_and_location;
        this.review_comment = review_comment;
        this.reviewProfile_image = reviewProfile_image;
        this.reviewImage1 = reviewImage1;
        this.reviewImage2 = reviewImage2;
        this.reviewImage3 = reviewImage3;
        this.good_count_textView = good_count_textView;
        this.check_user_flag = check_user_flag;
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

    public int getReviewProfile_image() {
        return reviewProfile_image;
    }

    public void setReviewProfile_image(int reviewProfile_image) {
        this.reviewProfile_image = reviewProfile_image;
    }

    public int getReviewImage1() {
        return reviewImage1;
    }

    public void setReviewImage1(int reviewImage1) {
        this.reviewImage1 = reviewImage1;
    }

    public int getReviewImage2() {
        return reviewImage2;
    }

    public void setReviewImage2(int reviewImage2) {
        this.reviewImage2 = reviewImage2;
    }

    public int getReviewImage3() {
        return reviewImage3;
    }

    public void setReviewImage3(int reviewImage3) {
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
}
