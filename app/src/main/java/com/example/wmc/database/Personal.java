package com.example.wmc.database;

public class Personal {
    private Long memNum;
    private String id;
    private String pwd;
    private String nickName;
    private Integer grade;
    private Integer phoneNum;
    private String profileimageurl;
    private String favorite1;
    private String favorite2;

    public Personal(Long memNum, String id, String pwd, String nickName, Integer grade, Integer phoneNum,
                    String profileimageurl, String favorite1, String favorite2) {

        this.memNum = memNum;
        this.id = id;
        this.pwd = pwd;
        this.nickName = nickName;
        this.grade = grade;
        this.phoneNum = phoneNum;
        this.profileimageurl = profileimageurl;
        this.favorite1 = favorite1;
        this.favorite2 = favorite2;
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


    public Integer getPhoneNum() {
        return phoneNum;
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


}
