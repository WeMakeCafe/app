package com.example.wmc.database;

import java.time.LocalDateTime;

public class Review {
    private Long reviewNum;
    private Long cafeNum;
    private Long categoryNum;
    private byte[] reviewImage;
    private Integer likeCount;
    private String reviewText;
    private String createTime;
    private Long keyword1;
    private Long keyword2;
    private Long keyword3;
    private Long keyword4;
    private Long keyword5;
    private Long keyword6;
    private Long keyword7;
    private Long keyword8;
    private Long keyword9;
    private Long keyword10;
    private Long keyword11;
    private Long keyword12;
    private Long keyword13;
    private Long keyword14;
    private Long keyword15;
    private Long keyword16;
    private Long keyword17;
    private Long keyword18;
    private Long keyword19;
    private Long keyword20;
    private Long keyword21;
    private Long keyword22;
    private Long keyword23;
    private Long keyword24;
    private Long keyword25;
    private Long keyword26;
    private Long keyword27;
    private Long keyword28;
    private Long keyword29;
    private Long keyword30;
    private Long keyword31;
    private Long keyword32;
    private Long keyword33;
    private Long keyword34;
    private Long keyword35;
    private Long keyword36;
    private Long memNum;

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
    private Boolean locationcheck;

    public Review (Long reviewNum, Long cafeNum, byte[] reviewImage, Long categoryNum, Integer likeCount, String reviewText, String createTime,
                   Long keyword1, Long keyword2,Long keyword3,Long keyword4,Long keyword5,Long keyword6,Long keyword7,Long keyword8, Long keyword9,
                   Long keyword10,Long keyword11, Long keyword12,Long keyword13,Long keyword14,Long keyword15,Long keyword16, Long keyword17,
                   Long keyword18, Long keyword19,Long keyword20,Long keyword21, Long keyword22,Long keyword23,Long keyword24, Long keyword25,
                   Long keyword26,Long keyword27,Long keyword28, Long keyword29,Long keyword30,Long keyword31, Long keyword32, Long keyword33,
                   Long keyword34,Long keyword35,Long keyword36, Long memNum, Integer tastePoint1, Integer tastePoint2, Integer tastePoint3,
                   Integer tastePoint4, Integer seatPoint1, Integer seatPoint2, Integer seatPoint3, Integer seatPoint4, Integer studyPoint1,
                   Integer studyPoint2, Integer studyPoint3, Integer studyPoint4, Boolean locationcheck) {

        this.reviewNum = reviewNum;
        this.cafeNum = cafeNum;
        this.reviewImage = reviewImage;
        this.categoryNum = categoryNum;
        this.likeCount = likeCount;
        this.reviewText = reviewText;
        this.createTime = createTime;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.keyword4 = keyword4;
        this.keyword5 = keyword5;
        this.keyword6 = keyword6;
        this.keyword7 = keyword7;
        this.keyword8 = keyword8;
        this.keyword9 = keyword9;
        this.keyword10 = keyword10;
        this.keyword11 = keyword11;
        this.keyword12 = keyword12;
        this.keyword13 = keyword13;
        this.keyword14 = keyword14;
        this.keyword15 = keyword15;
        this.keyword16 = keyword16;
        this.keyword17 = keyword17;
        this.keyword18 = keyword18;
        this.keyword19 = keyword19;
        this.keyword20 = keyword20;
        this.keyword21 = keyword21;
        this.keyword22 = keyword22;
        this.keyword23 = keyword23;
        this.keyword24 = keyword24;
        this.keyword25 = keyword25;
        this.keyword26 = keyword26;
        this.keyword27 = keyword27;
        this.keyword28 = keyword28;
        this.keyword29 = keyword29;
        this.keyword30 = keyword30;
        this.keyword31 = keyword31;
        this.keyword32 = keyword32;
        this.keyword33 = keyword33;
        this.keyword34 = keyword34;
        this.keyword35 = keyword35;
        this.keyword36 = keyword36;
        this.memNum = memNum;

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
        this.locationcheck = locationcheck;
    }

    public Long getReviewNum() {return reviewNum;}
    public Long getCafeNum() {return cafeNum;}
    public byte[] getReviewImage() {return reviewImage;}
    public Long getCategoryNum() {return categoryNum;}
    public Integer getLikeCount() {return likeCount;}
    public String getReviewText() {return reviewText;}
    public String getCreateTime() { return createTime; }
    public Long getKeyword1() {return keyword1;}
    public Long getKeyword2() {return keyword2;}
    public Long getKeyword3() {return keyword3;}
    public Long getKeyword4() {return keyword4;}
    public Long getKeyword5() {return keyword5;}
    public Long getKeyword6() {return keyword6;}
    public Long getKeyword7() {return keyword7;}
    public Long getKeyword8() {return keyword8;}
    public Long getKeyword9() {return keyword9;}
    public Long getKeyword10() {return keyword10;}
    public Long getKeyword11() {return keyword11;}
    public Long getKeyword12() {return keyword12;}
    public Long getKeyword13() {return keyword13;}
    public Long getKeyword14() {return keyword14;}
    public Long getKeyword15() {return keyword15;}
    public Long getKeyword16() {return keyword16;}
    public Long getKeyword17() {return keyword17;}
    public Long getKeyword18() {return keyword18;}
    public Long getKeyword19() {return keyword19;}
    public Long getKeyword20() {return keyword20;}
    public Long getKeyword21() {return keyword21;}
    public Long getKeyword22() {return keyword22;}
    public Long getKeyword23() {return keyword23;}
    public Long getKeyword24() {return keyword24;}
    public Long getKeyword25() {return keyword25;}
    public Long getKeyword26() {return keyword26;}
    public Long getKeyword27() {return keyword27;}
    public Long getKeyword28() {return keyword28;}
    public Long getKeyword29() {return keyword29;}
    public Long getKeyword30() {return keyword30;}
    public Long getKeyword31() {return keyword31;}
    public Long getKeyword32() {return keyword32;}
    public Long getKeyword33() {return keyword33;}
    public Long getKeyword34() {return keyword34;}
    public Long getKeyword35() {return keyword35;}
    public Long getKeyword36() {return keyword36;}
    public Long getMemNum() {return memNum;}

    public Integer getTastePoint1() {return tastePoint1;}
    public Integer getTastePoint2() {return tastePoint2;}
    public Integer getTastePoint3() {return tastePoint3;}
    public Integer getTastePoint4() {return tastePoint4;}

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

    public Boolean getLocationcheck() {
        return locationcheck;
    }

    public void setLocationcheck(Boolean locationcheck) {
        this.locationcheck = locationcheck;
    }
}
