package com.yifeng.controller.frontend;


import com.yifeng.entity.dto.MainPageInfoDto;
import com.yifeng.entity.dto.Result;
import com.yifeng.service.combine.IHeadLineShopCategoryCombineService;
import lombok.Getter;
import org.yifeng.core.annotation.Controller;
import org.yifeng.inject.annotation.Autowired;
import org.yifeng.mvc.annotation.RequestMapping;
import org.yifeng.mvc.type.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Getter
@RequestMapping("/main")
public class MainPageController {

    @Autowired
    private IHeadLineShopCategoryCombineService iHeadLineShopCategoryCombineService;

    public Result<MainPageInfoDto> getMainPageInfo(HttpServletRequest request, HttpServletResponse response){
//        System.out.println("111111111");
        return iHeadLineShopCategoryCombineService.getMainPageInfo();
    }
    public void getMainPageInfo1(){
//        System.out.println("111111111");
        iHeadLineShopCategoryCombineService.getMainPageInfo1();
    }
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void throwEx(){
        throw new RuntimeException("抛出异常信息");
    }

}
