package com.example.wmc.database;

public class ReviewImage {
    private Long rimageNum;
    private Long memNum;
    private Long reviewNum;
    private String fileUrl;

    public ReviewImage(Long rmageNum, Long memNum, Long reviewNum, String fileUrl){
        this.rimageNum = rimageNum;
        this.memNum = memNum;
        this.reviewNum = reviewNum;
        this.fileUrl = fileUrl;
    }

    public Long getrimageNum() {
        return rimageNum;
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
