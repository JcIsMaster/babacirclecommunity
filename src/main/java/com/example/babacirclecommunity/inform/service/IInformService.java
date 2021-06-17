package com.example.babacirclecommunity.inform.service;



import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.inform.vo.InformUserVo;

import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/3/22 10:36
 */
public interface IInformService {

    /**
     * 查询评论通知
     * @param userId 当前用户id
     * @param type 0评论，1获赞
     * @param paging 分页
     * @return
     */
    List<InformUserVo> queryCommentsNotice(int userId, int type, Paging paging);
}
