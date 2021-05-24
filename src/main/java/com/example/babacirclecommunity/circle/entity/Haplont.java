package com.example.babacirclecommunity.circle.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/4 9:52
 * 单元体实体类
 */
@Data
public class Haplont {

    private int id;

    /**
     * 名称
     */
    private String hName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是有有效（1有效 0无效 ）默认1
     */
    private int isDelete;
}
