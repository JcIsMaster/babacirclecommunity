package com.example.babacirclecommunity.common.constanct;

/**
 * @author JC
 * @date 2021/11/12 16:56
 */
public enum HonoredLevel {

    /**
     * 铜牌,创建圈子数3,月发布线上活动数5
     */
    HONORED_LEVEL_TONG(0,3,5),
    /**
     * 银牌,创建圈子数6,月发布线上活动数15
     */
    HONORED_LEVEL_YIN(1,6,15),
    /**
     * 金牌,创建圈子数20,月发布线上活动数50
     */
    HONORED_LEVEL_JIN(2,20,50);

    private int level;

    private int circleNum;

    private int activityOnlineNum;

    HonoredLevel(int level, int circleNum, int activityOnlineNum) {
        this.level = level;
        this.circleNum = circleNum;
        this.activityOnlineNum = activityOnlineNum;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCircleNum() {
        return circleNum;
    }

    public void setCircleNum(int circleNum) {
        this.circleNum = circleNum;
    }

    public int getActivityOnlineNum() {
        return activityOnlineNum;
    }

    public void setActivityOnlineNum(int activityOnlineNum) {
        this.activityOnlineNum = activityOnlineNum;
    }
}
