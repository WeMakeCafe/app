package com.example.wmc.database;

public class Personal {
    private Long memNum;
    private String id;
    private String pwd;
    private String nickName;
    private Integer grade;
    private String address;
    private Integer phoneNum;
    private Byte[] profileImage;
    private String favorite1;
    private String favorite2;

    public Personal(Long memNum, String id, String pwd, String nickName, Integer grade, String address, Integer phoneNum,
                    Byte[] profileImage, String favorite1, String favorite2) {

        this.memNum = memNum;
        this.id = id;
        this.pwd = pwd;
        this.nickName = nickName;
        this.grade = grade;
        this.address = address;
        this.phoneNum = phoneNum;
        this.profileImage = profileImage;
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

    public String getAddress() {
        return address;
    }

    public Integer getPhoneNum() {
        return phoneNum;
    }

    public Byte[] getProfileImage() {
        return profileImage;
    }

    public String getFavorite1() {
        return favorite1;
    }

    public String getFavorite2() {
        return favorite2;
    }


}
