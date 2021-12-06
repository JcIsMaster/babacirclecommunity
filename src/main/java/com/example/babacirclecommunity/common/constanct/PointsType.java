package com.example.babacirclecommunity.common.constanct;

/**
 * @author Jc
 * @date 2021/11/10
 * @version 1.0
 */
public enum PointsType {

    /**
     * 匹配
     */
    HONORED_POINTS_MATCH(0,50),

    /**
     * 发帖
     */
    HONORED_POINTS_POST(1,20),

    /**
     * 人才
     */
    HONORED_POINTS_Talents(2,200),

    /**
     * 货源
     */
    HONORED_POINTS_RESOURCE(3,20),

    /**
     * 合作
     */
    HONORED_POINTS_COLLABORATE(4,50);

    /**
     * 金币充值
     * HONORED_POINTS_RECHARGE(5,充值金额 * 10);
     */

    private int type;
    private int points;

    PointsType(int type, int points) {
        this.type = type;
        this.points = points;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
