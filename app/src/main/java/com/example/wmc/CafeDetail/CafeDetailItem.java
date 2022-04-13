package com.example.wmc.CafeDetail;

public class CafeDetailItem {

    String reviewNickName;
    String level_and_location;
    String review_comment;
    int reviewProfile_image;
    int reviewImage;
    String good_count_textView;

    public CafeDetailItem(String reviewNickName, String level_and_location, String review_comment, int reviewProfile_image, int reviewImage, String good_count_textView) {
        this.reviewNickName = reviewNickName;
        this.level_and_location = level_and_location;
        this.review_comment = review_comment;
        this.reviewProfile_image = reviewProfile_image;
        this.reviewImage = reviewImage;
        this.good_count_textView = good_count_textView;
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
}
