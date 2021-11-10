package com.example.babacirclecommunity.resourceMatch.entity;

public class UserInformation {

    @Override
    public String toString() {
        return "UserInformation{" +
                "age=" + age +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userSex=" + userSex +
                ", twoSelect='" + twoSelect + '\'' +
                '}';
    }

    private int age;


    private int userId;
    private String userName;
    private String avatar;
    private int userSex;
    private String twoSelect;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


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

    public int getUserSex() {
        return userSex;
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
}
