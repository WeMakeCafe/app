package com.example.wmc.database;

public class Love {
    private Long loveNum;
    private Long reviewNum;
    private Long memNum;

    public Love(Long loveNum, Long reviewNum, Long memNum){
        this.loveNum = loveNum;
        this.reviewNum = reviewNum;
        this.memNum = memNum;
    }

    public Long getMemNum() {
        return memNum;
    }

    public Long getLoveNum() {
        return loveNum;
    }

    public Long getReviewNum() {
        return reviewNum;
    }
}

