package com.yifeng.service.solo.impl;


import com.yifeng.entity.bo.ShopCategory;
import com.yifeng.entity.dto.Result;
import com.yifeng.service.solo.IShopCategoryService;
import org.yifeng.core.annotation.Service;

import java.util.List;

@Service
public class ShopCategoryServiceImpl implements IShopCategoryService {


    @Override
    public Result<Boolean> addShopCategory(ShopCategory shopCategory) {
        return null;
    }

    @Override
    public Result<Boolean> removeShopCategory(ShopCategory shopCategory) {
        return null;
    }

    @Override
    public Result<Boolean> modifyShopCategory(ShopCategory shopCategory) {
        return null;
    }

    @Override
    public Result<ShopCategory> getShopCategoryById(int shopCategoryId) {
        return null;
    }

    @Override
    public Result<List<ShopCategory>> getShopCategoryList(ShopCategory shopCategoryCondition, int pageIndex, int pageSize) {
        return null;
    }
}
