package com.yifeng.entity.bo;

import lombok.Data;

import java.util.Date;


@Data
public class HeadLine {
    private Long lineId;
    private String lineName;
    private String lineLink;
    private String lineImg;
    // 优先级
    private Integer priority;
    private Integer enableStatus;
    private Date createTime;
    private Date lastEditTime;

}
