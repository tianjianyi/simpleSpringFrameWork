package com.yifeng.service.combine.impl;


import com.yifeng.entity.bo.HeadLine;
import com.yifeng.entity.bo.ShopCategory;
import com.yifeng.entity.dto.MainPageInfoDto;
import com.yifeng.entity.dto.Result;
import com.yifeng.service.combine.IHeadLineShopCategoryCombineService;
import com.yifeng.service.solo.IHeadLineService;
import com.yifeng.service.solo.IShopCategoryService;
import org.yifeng.core.annotation.Service;
import org.yifeng.inject.annotation.Autowired;

import java.util.List;

@Service
public class HeadLineShopCategoryCombineServiceImpl implements IHeadLineShopCategoryCombineService {

    @Autowired
    private IHeadLineService iHeadLineService;

    @Autowired
    private IShopCategoryService iShopCategoryService;

    public Result<MainPageInfoDto> getMainPageInfo() {
        HeadLine headLine = new HeadLine();
        headLine.setEnableStatus(1);
        Result<List<HeadLine>> headLineRes = iHeadLineService.getHeadLineList(headLine, 1, 4);

        ShopCategory shopCategory = new ShopCategory();
        Result<List<ShopCategory>> shopCategoryRes = iShopCategoryService.getShopCategoryList(shopCategory, 1, 100);

        MainPageInfoDto mainPageInfoDto=new MainPageInfoDto();
        mainPageInfoDto.setHeadLineList(headLineRes.getData());
        mainPageInfoDto.setShopCategoryList(shopCategoryRes.getData());

        Result<MainPageInfoDto> result=new Result();
        result.setData(mainPageInfoDto);
        return result;
    }
    public void getMainPageInfo1(){
        System.out.println("122313");
        iHeadLineService.sayHello();

    }
}
