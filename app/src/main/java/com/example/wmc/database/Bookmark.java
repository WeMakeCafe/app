package com.example.wmc.database;

public class Bookmark {
    private Long bookmarkNum;
    private Long cafeNum;
    private Long memNum;

    public Bookmark(Long bookmarkNum, Long cafeNum, Long memNum){

        this.bookmarkNum = bookmarkNum;
        this.cafeNum = cafeNum;
        this.memNum = memNum;
    }

    public Long getBookmarkNum() {
        return bookmarkNum;
    }

    public Long getCafeNum() {
        return cafeNum;
    }

    public Long getMemNum() {
        return memNum;
    }

}
