package com.example.wmc.database;

public class CafeImage {
    private Long cImageNum;
    private Long cafeNum;
    private String fileUrl;

    public CafeImage(Long cImageNum, Long cafeNum, String fileUrl){
        this.cImageNum = cImageNum;
        this.cafeNum = cafeNum;
        this.fileUrl = fileUrl;
    }

    public Long getcImageNum() {
        return cImageNum;
    }

    public Long getCafeNum() {
        return cafeNum;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
