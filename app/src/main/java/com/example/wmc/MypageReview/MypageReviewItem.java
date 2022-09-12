package com.example.wmc.MypageReview;

public class MypageReviewItem {

    String mypageReview_CafeName;
    String mypageReview_writeTime;
    String review_comment;
    String reviewImage1;
    String reviewImage2;
    String reviewImage3;
    String good_count_textView;
    boolean check_user_flag;
    Long mem_num;
    Long get_cafe_num;
    Long get_review_num;

    public MypageReviewItem(String mypageReview_CafeName, String mypageReview_writeTime, String review_comment, String reviewImage1, String reviewImage2, String reviewImage3, String good_count_textView, boolean check_user_flag, Long mem_num, Long get_cafe_num, Long get_review_num) {
        this.mypageReview_CafeName = mypageReview_CafeName;
        this.mypageReview_writeTime = mypageReview_writeTime;
        this.review_comment = review_comment;
        this.reviewImage1 = reviewImage1;
        this.reviewImage2 = reviewImage2;
        this.reviewImage3 = reviewImage3;
        this.good_count_textView = good_count_textView;
        this.check_user_flag = check_user_flag;
        this.mem_num = mem_num;
        this.get_cafe_num = get_cafe_num;
        this.get_review_num = get_review_num;
    }


    public String getMypageReview_CafeName() {
        return mypageReview_CafeName;
    }

    public void setMypageReview_CafeName(String mypageReview_CafeName) {
        this.mypageReview_CafeName = mypageReview_CafeName;
    }

    public String getMypageReview_writeTime() {
        return mypageReview_writeTime;
    }

    public void setMypageReview_writeTime(String mypageReview_writeTime) {
        this.mypageReview_writeTime = mypageReview_writeTime;
    }

    public String getReview_comment() {
        return review_comment;
    }

    public void setReview_comment(String review_comment) {
        this.review_comment = review_comment;
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

    public Long getMem_num() {
        return mem_num;
    }

    public void setMem_num(Long mem_num) {
        this.mem_num = mem_num;
    }

    public Long getGet_cafe_num() {
        return get_cafe_num;
    }

    public void setGet_cafe_num(Long get_cafe_num) {
        this.get_cafe_num = get_cafe_num;
    }

    public Long getGet_review_num() {
        return get_review_num;
    }

    public void setGet_review_num(Long get_review_num) {
        this.get_review_num = get_review_num;
    }
}
