package com.example.babacirclecommunity.common.utils;

import lombok.Data;

import java.util.List;

/**
 * @author MQ
 * @date 2021/6/4 17:25
 */
public class PosterInfoUtil {

    private String qrCodeUrl  = "";
    private String backgroundUrl   = "";
    private String avastarUrl  = "";

    private Integer qrCodeSize  = 0;
    private Integer avastarSize  = 0;

    private Integer qrCodeLocationX  = 0;
    private Integer qrCodeLocationY  = 0;

    private Integer avastarLocationX  = 0;
    private Integer avastarLocationY  = 0;


    private String nickName  = "";
    private String otherText  = "";


    private Integer nickNameLocationX  = 0;
    private Integer nickNameLocationY  = 0;

    private Integer otherTextLocationX  = 0;
    private Integer otherTextLocationY  = 0;

    private Integer nickNameFontSize = 0;
    private Integer otherTextFontSize = 0;

    private String savePath  = "";


    public List<Integer> getFontColors() {
        return fontColors;
    }

    public void setFontColors(List<Integer> fontColors) {
        this.fontColors = fontColors;
    }

    private List<Integer> fontColors;
    public Integer getNickNameFontSize() {
        return nickNameFontSize;
    }

    public void setNickNameFontSize(Integer nickNameFontSize) {
        this.nickNameFontSize = nickNameFontSize;
    }

    public Integer getOtherTextFontSize() {
        return otherTextFontSize;
    }

    public void setOtherTextFontSize(Integer otherTextFontSize) {
        this.otherTextFontSize = otherTextFontSize;
    }




    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOtherText() {
        return otherText;
    }

    public void setOtherText(String otherText) {
        this.otherText = otherText;
    }

    public Integer getNickNameLocationX() {
        return nickNameLocationX;
    }

    public void setNickNameLocationX(Integer nickNameLocationX) {
        this.nickNameLocationX = nickNameLocationX;
    }

    public Integer getNickNameLocationY() {
        return nickNameLocationY;
    }

    public void setNickNameLocationY(Integer nickNameLocationY) {
        this.nickNameLocationY = nickNameLocationY;
    }

    public Integer getOtherTextLocationX() {
        return otherTextLocationX;
    }

    public void setOtherTextLocationX(Integer otherTextLocationX) {
        this.otherTextLocationX = otherTextLocationX;
    }

    public Integer getOtherTextLocationY() {
        return otherTextLocationY;
    }

    public void setOtherTextLocationY(Integer otherTextLocationY) {
        this.otherTextLocationY = otherTextLocationY;
    }




    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getAvastarUrl() {
        return avastarUrl;
    }

    public void setAvastarUrl(String avastarUrl) {
        this.avastarUrl = avastarUrl;
    }

    public Integer getQrCodeSize() {
        return qrCodeSize;
    }

    public void setQrCodeSize(Integer qrCodeSize) {
        this.qrCodeSize = qrCodeSize;
    }

    public Integer getAvastarSize() {
        return avastarSize;
    }

    public void setAvastarSize(Integer avastarSize) {
        this.avastarSize = avastarSize;
    }

    public Integer getQrCodeLocationX() {
        return qrCodeLocationX;
    }

    public void setQrCodeLocationX(Integer qrCodeLocationX) {
        this.qrCodeLocationX = qrCodeLocationX;
    }

    public Integer getQrCodeLocationY() {
        return qrCodeLocationY;
    }

    public void setQrCodeLocationY(Integer qrCodeLocationY) {
        this.qrCodeLocationY = qrCodeLocationY;
    }

    public Integer getAvastarLocationX() {
        return avastarLocationX;
    }

    public void setAvastarLocationX(Integer avastarLocationX) {
        this.avastarLocationX = avastarLocationX;
    }

    public Integer getAvastarLocationY() {
        return avastarLocationY;
    }

    public void setAvastarLocationY(Integer avastarLocationY) {
        this.avastarLocationY = avastarLocationY;
    }
}
