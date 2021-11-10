package com.example.babacirclecommunity.sameCity.vo;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author JC
 * @date 2021/10/23 14:03
 */
@Data
public class SameCityUser {

    /**
     * 商家
     * {"is_shops":0,"platform":"京东","stor":"sfsgsg","job":null,"status":null,"twoSelect":"招人才","thSelect":"快"}
     *
     * 非商家 职业
     * {"is_shops":1,"is_status":0,"platform":"京东","stor":null,"job":"仓库管理","status":null,"twoSelect":"供货","thSelect":"快"}
     *
     * 非商家 状态
     * {"is_shops":1,"is_status":1,"platform":"京东","stor":null,"job":null,"status":"电商爱好者（学习电商中）","twoSelect":"找货源","thSelect":"中"}
     */

    /**
     * parameter表id
     */
    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户性别
     */
    private int userSex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户年龄
     */
    private int userAge;

    /**
     * 用户人气值(粉丝数量*10)
     */
    private int popularityValue;

    /**
     * 是否是商家
     */
    private int isShops;

    /**
     * 商家平台
     */
    @ApiModelProperty("JSON拓展信息返回")
    @JsonRawValue
    private String platform;

    /**
     * 商家平台
     */
    @ApiModelProperty("JSON拓展信息返回")
    @JsonRawValue
    private String platformLogo;

    /**
     * 商家店铺名
     */
    @ApiModelProperty("JSON拓展信息返回")
    @JsonRawValue
    private String stor;

    /**
     * 非商家职业状态
     */
    private int isStatus;

    /**
     * 非商家职业
     */
    @ApiModelProperty("JSON拓展信息返回")
    @JsonRawValue
    private String job;

    /**
     * 非商家状态
     */
    @ApiModelProperty("JSON拓展信息返回")
    @JsonRawValue
    private String status;

}
