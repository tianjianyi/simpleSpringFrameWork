package com.yifeng.controller.superadmin;


import com.yifeng.entity.bo.ShopCategory;
import com.yifeng.entity.dto.Result;
import com.yifeng.service.solo.IShopCategoryService;
import org.yifeng.core.annotation.Controller;
import org.yifeng.inject.annotation.Autowired;

import java.util.List;

@Controller
public class ShopCategoryOperationController {
    @Autowired
    private IShopCategoryService iShopCategoryService;


    public Result<Boolean> addShopCategory(ShopCategory shopCategory) {
        return iShopCategoryService.addShopCategory(shopCategory);
    }

    public Result<Boolean> removeShopCategory(ShopCategory shopCategory) {
        return iShopCategoryService.removeShopCategory(shopCategory);
    }

    public Result<Boolean> modifyShopCategory(ShopCategory shopCategory) {
        return iShopCategoryService.modifyShopCategory(shopCategory);
    }

    public Result<ShopCategory> getShopCategoryById(int shopCategoryId) {
        return iShopCategoryService.getShopCategoryById(shopCategoryId);
    }

    public Result<List<ShopCategory>> getShopCategoryList(ShopCategory shopCategoryCondition, int pageIndex, int pageSize) {
        return iShopCategoryService.getShopCategoryList(shopCategoryCondition, pageIndex, pageSize);
    }
    
    

}
