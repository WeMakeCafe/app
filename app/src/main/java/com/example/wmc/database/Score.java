package com.example.wmc.database;

public class Score {
    private Long scoreNum;
    private Long cafeNum;
    private Long reviewNum;
    private Long categoryNum;

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

    private Integer tastePoint1_AVG;
    private Integer tastePoint2_AVG;
    private Integer tastePoint3_AVG;
    private Integer tastePoint4_AVG;
    private Integer seatPoint1_AVG;
    private Integer seatPoint2_AVG;
    private Integer seatPoint3_AVG;
    private Integer seatPoint4_AVG;
    private Integer studyPoint1_AVG;
    private Integer studyPoint2_AVG;
    private Integer studyPoint3_AVG;
    private Integer studyPoint4_AVG;

    public Score(Long scoreNum, Long cafeNum, Long reviewNum, Long categoryNum, Integer tastePoint1, Integer tastePoint2, Integer tastePoint3, Integer tastePoint4,
                 Integer seatPoint1, Integer seatPoint2, Integer seatPoint3, Integer seatPoint4, Integer studyPoint1, Integer studyPoint2, Integer studyPoint3, Integer studyPoint4,
                 Integer tastePoint1_AVG, Integer tastePoint2_AVG, Integer tastePoint3_AVG, Integer tastePoint4_AVG, Integer seatPoint1_AVG, Integer seatPoint2_AVG, Integer seatPoint3_AVG, Integer seatPoint4_AVG,
                 Integer studyPoint1_AVG, Integer studyPoint2_AVG, Integer studyPoint3_AVG, Integer studyPoint4_AVG){

        this.scoreNum = scoreNum;
        this.cafeNum = cafeNum;
        this.reviewNum = reviewNum;
        this.categoryNum = categoryNum;

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

        this.tastePoint1_AVG = tastePoint1_AVG;
        this.tastePoint2_AVG = tastePoint2_AVG;
        this.tastePoint3_AVG = tastePoint3_AVG;
        this.tastePoint4_AVG = tastePoint4_AVG;

        this.seatPoint1_AVG = seatPoint1_AVG;
        this.seatPoint2_AVG = seatPoint2_AVG;
        this.seatPoint3_AVG = seatPoint3_AVG;
        this.seatPoint4_AVG = seatPoint4_AVG;

        this.studyPoint1_AVG = studyPoint1_AVG;
        this.studyPoint2_AVG = studyPoint2_AVG;
        this.studyPoint3_AVG = studyPoint3_AVG;
        this.studyPoint4_AVG = studyPoint4_AVG;
    }

    public Long getScoreNum() {
        return scoreNum;
    }

    public Long getCafeNum() {
        return cafeNum;
    }

    public Long getReviewNum() {
        return reviewNum;
    }

    public Long getCategoryNum() {
        return categoryNum;
    }

    public Integer getTastePoint1() {
        return tastePoint1;
    }

    public Integer getTastePoint2() {
        return tastePoint2;
    }

    public Integer getTastePoint3() {
        return tastePoint3;
    }

    public Integer getTastePoint4() {
        return tastePoint4;
    }

    public Integer getSeatPoint1() {
        return seatPoint1;
    }

    public Integer getSeatPoint2() {
        return seatPoint2;
    }

    public Integer getSeatPoint3() {
        return seatPoint3;
    }

    public Integer getSeatPoint4() {
        return seatPoint4;
    }

    public Integer getStudyPoint1() {
        return studyPoint1;
    }

    public Integer getStudyPoint2() {
        return studyPoint2;
    }

    public Integer getStudyPoint3() {
        return studyPoint3;
    }

    public Integer getStudyPoint4() {
        return studyPoint4;
    }

    public Integer getTastePoint1_AVG() {
        return tastePoint1_AVG;
    }

    public Integer getTastePoint2_AVG() {
        return tastePoint2_AVG;
    }

    public Integer getTastePoint3_AVG() {
        return tastePoint3_AVG;
    }

    public Integer getTastePoint4_AVG() {
        return tastePoint4_AVG;
    }

    public Integer getSeatPoint1_AVG() {
        return seatPoint1_AVG;
    }

    public Integer getSeatPoint2_AVG() {
        return seatPoint2_AVG;
    }

    public Integer getSeatPoint3_AVG() {
        return seatPoint3_AVG;
    }

    public Integer getSeatPoint4_AVG() {
        return seatPoint4_AVG;
    }

    public Integer getStudyPoint1_AVG() {
        return studyPoint1_AVG;
    }

    public Integer getStudyPoint2_AVG() {
        return studyPoint2_AVG;
    }

    public Integer getStudyPoint3_AVG() {
        return studyPoint3_AVG;
    }

    public Integer getStudyPoint4_AVG() {
        return studyPoint4_AVG;
    }

}
