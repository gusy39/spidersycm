package com.ecmoho.promotion.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.DateUtil;
import com.ecmoho.base.Util.UrlUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import com.ecmoho.promotion.selenium.DiamondBoothSeleniumSpider;
import com.ecmoho.promotion.service.PromotionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/6.
 */
@Component("diamondBoothServiceImpl")
public class DiamondBoothServiceImpl implements PromotionService {
    private static final Log log= LogFactory.getLog(DiamondBoothServiceImpl.class);
    @Resource
    private SpiderAccountDao spiderAccountDao;
    @Resource(name = "diamondHeaderBean")
    private HeaderBean diamondHeaderBean;
    @Resource(name = "diamondBoothProcessor")
    private PageProcessor pageProcessor;
    @Resource(name = "diamondBoothSeleniumSpider")
    private DiamondBoothSeleniumSpider diamondBoothSeleniumSpider;

    @Override
    public void startGrap(int accountid, String dateStr, String grapfrequency) {
        SpiderSchqAcount spiderSchqAccount = spiderAccountDao.getSpiderSchqAccount(accountid);
        SpiderSchqChild spiderSchqChild = spiderAccountDao.getSpiderSchqChildByChildAccount("promotion-diamond");
        //自动登录url信息（后期存入数据库中，从数据库操作）
        Map<String, String> loginUrlmap = new HashMap<String, String>();
        loginUrlmap.put("login_url", "http://zuanshi.taobao.com/index.html");
        loginUrlmap.put("red_url", "http://zuanshi.taobao.com/indexdp.html");
        loginUrlmap.put("login_name", spiderSchqAccount.getLoginName());
        loginUrlmap.put("password", spiderSchqAccount.getPassword());
        String refferCookie = diamondBoothSeleniumSpider.getCookie(loginUrlmap);
        diamondHeaderBean.setCookie(refferCookie);
        String getUrlArr[] = spiderSchqChild.getGeturl().split("####");
        String csrfIDUrl=getUrlArr[0];
        String targetUrl=getUrlArr[1];
        //获取csrfID
        String userInfoStr = UrlUtil.getUrlString(diamondHeaderBean, csrfIDUrl, "POST");
        log.info("userInfoStr："+userInfoStr);
        if ("".equals(userInfoStr)){
            log.info("Token获取失败或已失效。。。。");
            return;
        }
        String csrfID="";
        try {
            String userInfo = userInfoStr.substring(8, userInfoStr.length() - 1);
            JSONObject userInfoJsonObject = JSONObject.parseObject(userInfo);
            csrfID = userInfoJsonObject.getJSONObject("data").getString("csrfID");
        } catch (Exception e) {
            log.info("json解析异常。。。");
        }

        if (csrfID==null||"".equals(csrfID)){
            log.info("cookie获取失败或已失效。。。。");
            return;
        }
        targetUrl=targetUrl.replaceAll("##csID##", csrfID);
        HashMap<String, String> urlMap = new HashMap<>();
        urlMap.put("accountid", accountid + "");
        urlMap.put("grapfrequency", grapfrequency);

        switch (grapfrequency) {
            case "day":
                List<String> lastFifteenDays = DateUtil.getLastFifteenDays(dateStr);
                for (String dayStr:lastFifteenDays){
                    createProcessor(urlMap,targetUrl,dayStr,dayStr);
                }
                break;
            case "week":
                createProcessor(urlMap,targetUrl,DateUtil.getLastWeekStartDate(dateStr),DateUtil.getLastWeekEndDate(dateStr));
                break;
            case "month":
                createProcessor(urlMap,targetUrl,DateUtil.getLastMonthStartDate(dateStr),DateUtil.getLastMonthEndDate(dateStr));
                break;
            default:
                log.info("传入执行周期有误。。。。");
                break;
        }
    }
    public void createProcessor(Map<String,String> urlMap,String targetUrl,String startDate,String endDate){
        String finalTargetUtl = targetUrl.replaceAll("##SD##",startDate).replaceAll("##ED##",endDate);
        urlMap.put("create_at", startDate);
        urlMap.put("log_at",DateUtil.getNowDateStr("yyyy-MM-dd HH:mm:ss"));
        diamondHeaderBean.setUrlMap(urlMap);
        Spider.create(pageProcessor).addUrl(finalTargetUtl).thread(2000).run();
    }
}
