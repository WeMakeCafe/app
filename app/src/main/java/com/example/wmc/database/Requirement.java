package com.example.wmc.database;

import java.time.LocalDateTime;

public class Requirement {
    private Long requireNum;
    private Long memNum;
    private Long cafeNum;
    private LocalDateTime requireTime;
    private String requireReason;

    public Requirement(Long requireNum, Long memNum, Long cafeNum, LocalDateTime requireTime, String requireReason){

        this.requireNum = requireNum;
        this.memNum = memNum;
        this.cafeNum = cafeNum;
        this.requireTime = requireTime;
        this.requireReason = requireReason;
    }

    public Long getRequireNum() {
        return requireNum;
    }

    public Long getMemNum() {
        return memNum;
    }

    public Long getCafeNum() {
        return cafeNum;
    }

    public LocalDateTime getRequireTime() {
        return requireTime;
    }

    public String getRequireReason() {
        return requireReason;
    }

}
