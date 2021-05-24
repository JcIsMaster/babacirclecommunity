package com.example.babacirclecommunity.file.service;

/**
 * @author MQ
 * @date 2021/5/23 20:09
 */
public interface IFileService {

    /**
     * 删除服务器图片
     * @param type 0 代表是要删除图片  1删除视频
     * @param imgUrl 图片路劲
     */
    void deleteFile(int type,String imgUrl);
}
