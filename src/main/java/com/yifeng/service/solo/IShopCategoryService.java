package com.yifeng.service.solo;


import com.yifeng.entity.bo.ShopCategory;
import com.yifeng.entity.dto.Result;

import java.util.List;

public interface IShopCategoryService {
    Result<Boolean> addShopCategory(ShopCategory shopCategory);
    Result<Boolean> removeShopCategory(ShopCategory shopCategory);
    Result<Boolean> modifyShopCategory(ShopCategory shopCategory);

    Result<ShopCategory> getShopCategoryById(int shopCategoryId);
    Result<List<ShopCategory>> getShopCategoryList(ShopCategory shopCategoryCondition, int pageIndex, int pageSize);
}
