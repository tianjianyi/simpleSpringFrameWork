package com.yifeng.service.solo.impl;


import com.yifeng.entity.bo.HeadLine;
import com.yifeng.entity.dto.Result;
import com.yifeng.service.solo.IHeadLineService;
import org.yifeng.core.annotation.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements IHeadLineService {

    @Override
    public Result<Boolean> addHeadLine(HeadLine headLine) {
        System.out.println(headLine);
        System.out.println("wozaiyunxingya");
        Result<Boolean> result = new Result<Boolean>();
        result.setCode(200);
        result.setMsg("wozaiyunxingya啦啦啦");
        result.setData(true);
        return result;
    }

    @Override
    public Result<Boolean> removeHeadLine(HeadLine headLine) {
        return null;
    }

    @Override
    public Result<Boolean> modifyHeadLine(HeadLine headLine) {
        return null;
    }

    @Override
    public Result<HeadLine> getHeadLineById(int headLineId) {
        return null;
    }

    @Override
    public Result<List<HeadLine>> getHeadLineList(HeadLine headLineCondition, int pageIndex, int pageSize) {
        List<HeadLine> headLineList = new ArrayList<>();
        HeadLine headLine1 = new HeadLine();
        headLine1.setLineId(1L);
        headLine1.setLineName("头条1");
        headLine1.setLineLink("www.baidu.com");
        headLine1.setLineImg("图片");
        headLineList.add(headLine1);
        HeadLine headLine2 = new HeadLine();
        headLine2.setLineId(2L);
        headLine2.setLineName("头条2");
        headLine2.setLineLink("www.tengxun.com");
        headLine2.setLineImg("图片22");
        headLineList.add(headLine2);
        Result<List<HeadLine>> result = new Result<>();
        result.setData(headLineList);
        result.setCode(200);
        return result;
    }

    @Override
    public void sayHello() {
        System.out.println("啊给你联大的");
    }
}
