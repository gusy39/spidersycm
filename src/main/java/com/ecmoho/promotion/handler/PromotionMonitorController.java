package com.ecmoho.promotion.handler;

import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.promotion.service.PromotionService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meidejing on 2016/6/29.
 * 推广费用数据抓取控制器
 */
@Controller
@RequestMapping(value = "/promotion")
public class PromotionMonitorController {

    @Resource
    private SpiderAccountDao spiderAccountDao;
    //执行抓取操作
    @RequestMapping(value = "{promotionType}/{accountid}/{dateStr}/{grabFrequency}",method = RequestMethod.GET)
    @ResponseBody
    public String subWayGrap(HttpServletRequest request,@PathVariable("promotionType") String promotionType,@PathVariable("accountid") String accountid,
                             @PathVariable("dateStr") String dateStr,@PathVariable("grabFrequency") String grabFrequency){
        System.out.println(accountid);
        ApplicationContext ac= WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        PromotionService service= (PromotionService) ac.getBean(promotionType);
        String accountArr[]=accountid.split("\\|");
        for (String account:accountArr){
            service.startGrap(Integer.valueOf(account),dateStr,grabFrequency);
        }
        return "success";
    }
    @RequestMapping(value = "/getshopList",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,String>> subWayGrap(){
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        List<Map<String,String>> getList=new ArrayList<Map<String,String>>();
        Map<String,String> dataMap=null;
        if (spiderSchqAccountList!=null&&spiderSchqAccountList.size()>0){
            for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList){
                dataMap=new HashMap<String,String>();
                dataMap.put("loginName",spiderSchqAcount.getLoginName());
                dataMap.put("id",spiderSchqAcount.getSid()+"");
                dataMap.put("subwayflag",spiderSchqAcount.getPromotionSubwayFlag());
                dataMap.put("taobaocustomersflag",spiderSchqAcount.getPromotionTaobaocustomersFlag());
                dataMap.put("majibaoflag",spiderSchqAcount.getPromotionMajibaoFlag());
                dataMap.put("diamondflag",spiderSchqAcount.getPromotionDiamondFlag());
                dataMap.put("productflag",spiderSchqAcount.getPromotionProductFlag());
                dataMap.put("accountantflag",spiderSchqAcount.getPromotionAccountantFlag());
                getList.add(dataMap);
            }
        }
        return getList;
    }

}
