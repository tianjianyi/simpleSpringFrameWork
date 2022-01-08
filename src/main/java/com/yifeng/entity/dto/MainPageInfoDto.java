package com.yifeng.entity.dto;

import com.yifeng.entity.bo.HeadLine;
import com.yifeng.entity.bo.ShopCategory;
import lombok.Data;

import java.util.List;

@Data
public class MainPageInfoDto {
    private List<HeadLine> headLineList;
    private List<ShopCategory> shopCategoryList;
}
