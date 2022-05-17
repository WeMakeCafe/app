package com.example.wmc.database;

public class Category {
    private Long categoryNum;
    private Long reviewNum;
    private Integer tastePoint1;
    private Integer tastePoint2;
    private Integer tastePoint3;
    private Integer tastePoint4;
    private Integer seatPoint1;
    private Integer seatPoint2;
    private Integer seatPoint3;
    private Integer seatPoint4;
    private Integer studyPoint1;
    private Integer studyPoint2;
    private Integer studyPoint3;
    private Integer studyPoint4;




    public Category(Long categoryNum, Long reviewNum, Integer tastePoint1, Integer tastePoint2, Integer tastePoint3, Integer tastePoint4,
                    Integer seatPoint1, Integer seatPoint2, Integer seatPoint3, Integer seatPoint4, Integer studyPoint1, Integer studyPoint2,
                    Integer studyPoint3, Integer studyPoint4) {

        this.categoryNum = categoryNum;
        this.reviewNum = reviewNum;
        this.tastePoint1 = tastePoint1;
        this.tastePoint2 = tastePoint2;
        this.tastePoint3 = tastePoint3;
        this.tastePoint4 = tastePoint4;
        this.seatPoint1 = seatPoint1;
        this.seatPoint2 = seatPoint2;
        this.seatPoint3 = seatPoint3;
        this.seatPoint4 = seatPoint4;
        this.studyPoint1 = studyPoint1;
        this.studyPoint2 = studyPoint2;
        this.studyPoint3 = studyPoint3;
        this.studyPoint4 = studyPoint4;
    }

    public Long getCategoryNum() {return categoryNum;}
    public Long getReviewNum() {return reviewNum;}
    public Integer getTastePoint1() {return tastePoint1;}
    public Integer getTastePoint2() {return tastePoint2;}
    public Integer getTastePoint3() {return tastePoint3;}
    public Integer getTastePoint4() {return tastePoint4;}
    public Integer getSeatPoint1() {return seatPoint1;}
    public Integer getSeatPoint2() {return seatPoint2;}
    public Integer getSeatPoint3() {return seatPoint3;}
    public Integer getSeatPoint4() {return seatPoint4;}
    public Integer getStudyPoint1() {return studyPoint1;}
    public Integer getStudyPoint2() {return studyPoint2;}
    public Integer getStudyPoint3() {return studyPoint3;}
    public Integer getStudyPoint4() {return studyPoint4;}
}
