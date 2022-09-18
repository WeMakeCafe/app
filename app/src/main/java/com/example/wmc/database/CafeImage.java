package com.example.wmc.database;

public class CafeImage {
    private Long cimageNum;
    private Long cafeNum;
    private String fileUrl;

    public CafeImage(Long cimageNum, Long cafeNum, String fileUrl){
        this.cimageNum = cimageNum;
        this.cafeNum = cafeNum;
        this.fileUrl = fileUrl;
    }

    public Long getcimageNum() {
        return cimageNum;
    }

    public Long getCafeNum() {
        return cafeNum;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
