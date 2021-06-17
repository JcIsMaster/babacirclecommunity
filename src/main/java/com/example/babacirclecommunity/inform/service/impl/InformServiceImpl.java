package com.example.babacirclecommunity.inform.service.impl;


import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.inform.dao.InformMapper;
import com.example.babacirclecommunity.inform.service.IInformService;
import com.example.babacirclecommunity.inform.vo.InformUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author MQ
 * @date 2021/3/22 10:46
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class InformServiceImpl implements IInformService {

    @Autowired
    private InformMapper informMapper;

    public String getPaging(Paging paging) {
        int page = (paging.getPage() - 1) * paging.getLimit();
        return "limit " + page + "," + paging.getLimit() + "";
    }

    @Override
    public List<InformUserVo> queryCommentsNotice(int userId, int type, Paging paging) {
        //查询评论通知
        if (type == 0) {
            //圈子评论
            List<InformUserVo> informUserVos = informMapper.queryCommentsNoticeCircle(userId, type, getPaging(paging));

            //提问评论
            List<InformUserVo> informUserVos1 = informMapper.queryCommentsNoticeQuestion(userId, type, getPaging(paging));

            //干货评论
            List<InformUserVo> informUserVos2 = informMapper.queryCommentsNoticeDryGoods(userId, type, getPaging(paging));

            return Stream.of(informUserVos, informUserVos1, informUserVos2).flatMap(Collection::stream).collect(Collectors.toList());
        }

        //查询点赞通知
        if (type == 1) {
            //圈子评论
            List<InformUserVo> informUserVos = informMapper.queryCommentsNoticeCircleGive(userId, type, getPaging(paging));

            //提问评论
            List<InformUserVo> informUserVos1 = informMapper.queryCommentsNoticeQuestionGive(userId, type, getPaging(paging));

            //干货评论
            List<InformUserVo> informUserVos2 = informMapper.queryCommentsNoticeDryGoodsGive(userId, type, getPaging(paging));

            return Stream.of(informUserVos, informUserVos1, informUserVos2).flatMap(Collection::stream).collect(Collectors.toList());
        }

        throw new ApplicationException(CodeType.SERVICE_ERROR);
    }
}
