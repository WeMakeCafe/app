package com.example.wmc.database;

public class ReviewImage {
    private Long rImageNum;
    private Long memNum;
    private Long reviewNum;
    private String fileUrl;

    public ReviewImage(Long rImageNum, Long memNum, Long reviewNum, String fileUrl){
        this.rImageNum = rImageNum;
        this.memNum = memNum;
        this.reviewNum = reviewNum;
        this.fileUrl = fileUrl;
    }

    public Long getrImageNum() {
        return rImageNum;
    }

    public Long getMemNum() {
        return memNum;
    }

    public Long getReviewNum() {
        return reviewNum;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
