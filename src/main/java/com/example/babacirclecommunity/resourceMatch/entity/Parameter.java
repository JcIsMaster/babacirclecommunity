package com.example.babacirclecommunity.resourceMatch.entity;

/**
 * @author Administrator
 */
public class Parameter {
    private int id;
    private int userId;
    private String jsonClass;
    private String createAt;
    private int age;
    private String userName;
    private String avatar;
    private int userSex;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getJsonClass() {
        return jsonClass;
    }

    public void setJsonClass(String jsonClass) {
        this.jsonClass = jsonClass;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    @Override
    public String toString() {
        return "Parameter{" +
                "id=" + id +
                ", userId=" + userId +
                ", jsonClass='" + jsonClass + '\'' +
                ", createAt='" + createAt + '\'' +
                ", age=" + age +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userSex=" + userSex +
                '}';
    }
}
