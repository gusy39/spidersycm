package com.ecmoho.promotion.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.DateUtil;
import com.ecmoho.base.Util.UrlUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import com.ecmoho.promotion.selenium.ProductSalesSeleniumSpider;
import com.ecmoho.promotion.service.PromotionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/6.
 */
@Component("productBaoServiceImpl")
public class ProductBaoServiceImpl implements PromotionService {
    private static final Log log= LogFactory.getLog(ProductBaoServiceImpl.class);
    @Resource
    private SpiderAccountDao spiderAccountDao;
    @Resource(name = "productHeaderBean")
    private HeaderBean productHeaderBean;
    @Resource(name = "productSalesProcessor")
    private PageProcessor pageProcessor;
    @Resource(name = "productSalesSeleniumSpider")
    private ProductSalesSeleniumSpider productSalesSeleniumSpider;

    @Override
    public void startGrap(int accountid, String dateStr, String grapfrequency) {
        SpiderSchqAcount spiderSchqAccount = spiderAccountDao.getSpiderSchqAccount(accountid);
        SpiderSchqChild spiderSchqChild = spiderAccountDao.getSpiderSchqChildByChildAccount("promotion-product");
        Map<String, String> loginUrlmap = new HashMap<String, String>();
        loginUrlmap.put("login_url", "http://branding.taobao.com/index.html#!/login/index");
        loginUrlmap.put("red_url", "http://branding.taobao.com/#!/report/index");
        loginUrlmap.put("login_name", spiderSchqAccount.getLoginName());
        loginUrlmap.put("password", spiderSchqAccount.getPassword());
        String refer_cookie = productSalesSeleniumSpider.getCookie(loginUrlmap);
        productHeaderBean.setCookie(refer_cookie);
        String geturlArr[] = spiderSchqChild.getGeturl().split("####");
        String tokenUrl=geturlArr[0];
        String targetUrl=geturlArr[1];
        String userInfoStr = UrlUtil.getUrlString(productHeaderBean, tokenUrl, "GET");
        log.info("userInfoStr："+userInfoStr);
        if ("".equals(userInfoStr)){
            log.info("Token获取失败或已失效。。。。");
             return;
        }
        String token="";
        try {
            JSONObject userInfoJsonObject = JSONObject.parseObject(userInfoStr);
            token = userInfoJsonObject.getJSONObject("data").getString("token");
            if (token==null||"".equals(token)){
                log.info("Token获取失败或已失效。。。。");
                return;
            }
        }catch (Exception e){
           log.info("json转换异常。。");
        }

        targetUrl=targetUrl.replaceAll("##TK##", token);
        HashMap<String, String> urlMap = new HashMap<String, String>();
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
        String finalTargetUrl = targetUrl.replaceAll("##SD##", startDate).replaceAll("##ED##", endDate);
        urlMap.put("create_at", startDate);
        urlMap.put("log_at",DateUtil.getNowDateStr("yyyy-MM-dd HH:mm:ss"));
        productHeaderBean.setUrlMap(urlMap);
        Spider.create(pageProcessor).addUrl(finalTargetUrl).thread(2000).run();
    }
}
