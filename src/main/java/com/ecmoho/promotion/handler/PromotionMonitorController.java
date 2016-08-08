package com.ecmoho.promotion.handler;

import com.ecmoho.promotion.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
/**
 * Created by meidejing on 2016/6/29.
 * 推广费用数据抓取控制器
 */
@Controller
@RequestMapping(value = "/promotion")
public class PromotionMonitorController {
    @Resource(name = "subwayServiceImpl")
    private PromotionService subwayService;
    @Resource(name = "taobaoCustomersServiceImpl")
    private PromotionService taobaoCustomersService;
    @Resource(name = "majibaoServiceImpl")
    private PromotionService majibaoService;
    @Resource(name = "productBaoServiceImpl")
    private PromotionService productBaoService;
    @Resource(name = "diamondBoothServiceImpl")
    private PromotionService diamondService;
    @Resource(name = "accountantServiceImpl")
    private PromotionService accountantService;
    //抓取直通车数据
    @RequestMapping(value = "subwayGrap/{accountid}/{dateStr}/{grabFrequency}",method = RequestMethod.GET)
    @ResponseBody
    public String subWayGrap(HttpServletRequest request, @PathVariable("accountid") int accountid,
                             @PathVariable("dateStr") String dateStr,@PathVariable("grabFrequency") String grabFrequency){
        subwayService.startGrap(accountid,dateStr,grabFrequency);
        return "success";
    }
    //抓取淘宝客数据
    @RequestMapping(value = "taobaoCustomersGrap/{accountid}/{dateStr}/{grabFrequency}",method = RequestMethod.GET)
    @ResponseBody
    public String taobaoCustomersGrap(HttpServletRequest request, @PathVariable("accountid") int accountid,
                                      @PathVariable("dateStr") String dateStr,@PathVariable("grabFrequency") String grabFrequency){
        taobaoCustomersService.startGrap(accountid,dateStr,grabFrequency);
        return "success";
    }
    //抓取麻吉宝数据
    @RequestMapping(value = "majibaoGrap/{accountid}/{dateStr}/{grabFrequency}",method = RequestMethod.GET)
    @ResponseBody
    public String majibaoGrap(HttpServletRequest request, @PathVariable("accountid") int accountid,
                              @PathVariable("dateStr") String dateStr,@PathVariable("grabFrequency") String grabFrequency){
        majibaoService.startGrap(accountid,dateStr,grabFrequency);
        return "success";
    }
    //抓取钻展数据
    @RequestMapping(value = "diamondBoothGrap/{accountid}/{dateStr}/{grabFrequency}",method = RequestMethod.GET)
    @ResponseBody
    public String diamondBoothGrap(HttpServletRequest request, @PathVariable("accountid") int accountid,
                                 @PathVariable("dateStr") String dateStr,@PathVariable("grabFrequency") String grabFrequency){
        diamondService.startGrap(accountid,dateStr,grabFrequency);
        return "success";
    }
    //抓取品销宝数据
    @RequestMapping(value = "productBaoGrap/{accountid}/{dateStr}/{grabFrequency}",method = RequestMethod.GET)
    @ResponseBody
    public String productBaoGrap(HttpServletRequest request, @PathVariable("accountid") int accountid,
                                 @PathVariable("dateStr") String dateStr,@PathVariable("grabFrequency") String grabFrequency){
        productBaoService.startGrap(accountid,dateStr,grabFrequency);
        return "success";
    }
    //抓取账房数据
    @RequestMapping(value = "accountantGrap/{accountid}/{dateStr}/{grabFrequency}",method = RequestMethod.GET)
    @ResponseBody
    public String accountantGrap(HttpServletRequest request, @PathVariable("accountid") int accountid,
                              @PathVariable("dateStr") String dateStr,@PathVariable("grabFrequency") String grabFrequency){
        accountantService.startGrap(accountid,dateStr,grabFrequency);
        return "success";
    }
}
