package com.example.babacirclecommunity.learn.entity;

import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/4 17:12
 */
@Data
public class ClassList {

    /**
     * 课程id
     */
    private int id;
    /**
     * 课程标题
     */
    private String classTitle;
    /**
     * 列表打开状态
     */
    private boolean open;
    /**
     * 课程对象
     */
    private List<ClassOptions> classOptions;

}
