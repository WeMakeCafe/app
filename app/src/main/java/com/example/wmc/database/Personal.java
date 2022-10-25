package com.example.wmc.database;

public class Personal {
    private Long memNum;
    private String id;
    private String pwd;
    private String nickName;
    private Integer grade;
    private String profileimageurl;
    private String favorite1;
    private String favorite2;
    private String personalQuestion;
    private String personalAnswer;
    private String confirmstring;

    public Personal(Long memNum, String id, String pwd, String nickName, Integer grade,
                    String profileimageurl, String favorite1, String favorite2, String personalAnswer, String personalQuestion,
                    String confirmstring) {

        this.memNum = memNum;
        this.id = id;
        this.pwd = pwd;
        this.nickName = nickName;
        this.grade = grade;
        this.profileimageurl = profileimageurl;
        this.favorite1 = favorite1;
        this.favorite2 = favorite2;
        this.personalAnswer = personalAnswer;
        this.personalQuestion = personalQuestion;
        this.confirmstring = confirmstring;
    }

    public Long getMemNum() {
        return memNum;
    }

    public String getId() {
        return id;
    }

    public String getPwd() {
        return pwd;
    }

    public String getNickName() {
        return nickName;
    }

    public Integer getGrade() {
        return grade;
    }

    public String getProfileimageurl() {
        return profileimageurl;
    }

    public String getFavorite1() {
        return favorite1;
    }

    public String getFavorite2() {
        return favorite2;
    }

    public String getPersonalQuestion() { return personalQuestion; }

    public String getPersonalAnswer() {
        return personalAnswer;
    }

    public String getConfirmstring() { return confirmstring; }
}
