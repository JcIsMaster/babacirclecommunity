package com.example.babacirclecommunity.resourceMatch.entity;

public class MyInformation {
    private int userId;
    private String userName;
    private String avatar;
    private int userSex;
    private String twoSelect;
    private int age;
    private int stauts;
    private String thSelect;

    public int getStauts() {
        return stauts;
    }

    public void setStauts(int stauts) {
        this.stauts = stauts;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    private String textValue;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserSex(int userSex) {
        return this.userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public String getTwoSelect() {
        return twoSelect;
    }

    public void setTwoSelect(String twoSelect) {
        this.twoSelect = twoSelect;
    }

    public String getThSelect() {
        return thSelect;
    }

    public void setThSelect(String thSelect) {
        this.thSelect = thSelect;
    }

    public int getUserSex() {
        return userSex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "MyInformation{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userSex=" + userSex +
                ", twoSelect='" + twoSelect + '\'' +
                ", age=" + age +
                '}';
    }
}
