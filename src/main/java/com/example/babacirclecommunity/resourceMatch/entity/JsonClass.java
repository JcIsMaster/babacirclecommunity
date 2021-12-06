package com.example.babacirclecommunity.resourceMatch.entity;

public class JsonClass {
    /**
     * 是否商家
     */
    private int is_shops;

    /**
     * 是职业还是状态
     */
    private Integer is_status;

    /**
     * 平台
     */
    private String platform;

    /**
     * 平台logo
     */
    private String platformLogo;

    /**
     * 店铺名
     */
    private String stor;

    /**
     * 职业
     */
    private String job;

    /**
     * 状态
     */
    private String status;

    /**
     * 需求
     */
    private String twoSelect;

    /**
     * 速度
     */
    private String thSelect;

    /**
     * 用户id
     */
    private int userId;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private int age;

    public Integer getIs_status() {
        return is_status;
    }

    public void setIs_status(Integer is_status) {
        this.is_status = is_status;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getStor() {
        return stor;
    }

    public void setStor(String stor) {
        this.stor = stor;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIs_shops() {
        return is_shops;
    }

    public void setIs_shops(int is_shops) {
        this.is_shops = is_shops;
    }

    public String getPlatformLogo() {
        return platformLogo;
    }

    public void setPlatformLogo(String platformLogo) {
        this.platformLogo = platformLogo;
    }

    @Override
    public String toString() {
        return "Json{" +
                "is_shops=" + is_shops +
                ", is_status=" + is_status +
                ", platform='" + platform + '\'' +
                ", stor='" + stor + '\'' +
                ", job='" + job + '\'' +
                ", status='" + status + '\'' +
                ", twoSelect='" + twoSelect + '\'' +
                ", thSelect='" + thSelect + '\'' +
                ", userId=" + userId +
                ", age=" + age +
                '}';
    }
}
