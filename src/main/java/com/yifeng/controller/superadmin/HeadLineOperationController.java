package com.yifeng.controller.superadmin;


import com.yifeng.entity.bo.HeadLine;
import com.yifeng.entity.dto.Result;
import com.yifeng.service.solo.IHeadLineService;
import org.yifeng.core.annotation.Controller;
import org.yifeng.inject.annotation.Autowired;
import org.yifeng.mvc.annotation.RequestMapping;
import org.yifeng.mvc.annotation.RequestParam;
import org.yifeng.mvc.annotation.ResponseBody;
import org.yifeng.mvc.type.ModelAndView;
import org.yifeng.mvc.type.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/headline")
public class HeadLineOperationController {

    @Autowired
    private IHeadLineService iHeadLineService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView addHeadLine(@RequestParam("lineName")String lineName,
                                    @RequestParam("lineLink")String lineLink,
                                    @RequestParam("lineImg")String lineImg,
                                    @RequestParam("priority")Integer priority) {
        System.out.println(lineName);
        System.out.println("HeadLineOperationController.addHeadLine");
        HeadLine headLine1 = new HeadLine();
        headLine1.setLineId(3L);
        headLine1.setLineName(lineName);
        headLine1.setLineLink(lineLink);
        headLine1.setLineImg(lineImg);
        headLine1.setPriority(priority);
        Result<Boolean> result = iHeadLineService.addHeadLine(headLine1);
        ModelAndView mv = new ModelAndView();
        mv.setView("addheadline.jsp").addViewData("result", result);
        return mv;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public void removeHeadLine() {
        System.out.println("说删了就是删了哈");
//        return iHeadLineService.removeHeadLine(new HeadLine());
    }

    public Result<Boolean> modifyHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        return iHeadLineService.modifyHeadLine(new HeadLine());
    }


    public Result<HeadLine> getHeadLineById(HttpServletRequest req, HttpServletResponse resp) {
//        System.out.println("headLineId = " + headLineId);
        return iHeadLineService.getHeadLineById(1);
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<HeadLine>> getHeadLineList() {
        return iHeadLineService.getHeadLineList(null, 1, 100);
    }

}
