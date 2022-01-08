package com.yifeng.service.solo;


import com.yifeng.entity.bo.HeadLine;
import com.yifeng.entity.dto.Result;
import org.yifeng.core.annotation.Service;

import java.util.List;


public interface IHeadLineService {
    Result<Boolean> addHeadLine(HeadLine headLine);
    Result<Boolean> removeHeadLine(HeadLine headLine);
    Result<Boolean> modifyHeadLine(HeadLine headLine);

    Result<HeadLine> getHeadLineById(int headLineId);
    Result<List<HeadLine>> getHeadLineList(HeadLine headLineCondition, int pageIndex, int pageSize);
    void sayHello();

}
