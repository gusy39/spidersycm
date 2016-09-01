package com.ecmoho.promotion.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderDataDao;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/4.
 * 钻展URL获取结果解析
 */

@Component("diamondBoothProcessor")
public class DiamondBoothProcessor implements PageProcessor {
    private Site site = Site.me().setTimeOut(15000).setRetryTimes(3).setSleepTime(3000);
    @Resource(name = "diamondHeaderBean")
    private HeaderBean diamondHeaderBean;
    @Resource(name = "spiderDataDao")
    protected SpiderDataDao spiderPromotionDataDao;

    @Override
    public void process(Page page) {
        System.out.println(page.getJson());
        String resultStr = page.getJson().toString();
        Map<String, String> urlMap = diamondHeaderBean.getUrlMap();
        Map<String, String> dataMap = new HashMap<String, String>();
        String result = resultStr.substring(11, resultStr.length() - 2);
        System.out.println("result:" + result);
        JSONObject data = JSON.parseObject(result).getJSONObject("data");
        if (data != null && !"{}".equals(data.toString())) {
            JSONArray jsonarray = data.getJSONArray("result");
            if (jsonarray.size() > 0) {
                JSONObject resultJsonObject = data.getJSONArray("result").getJSONObject(0);
                if (resultJsonObject != null) {
                    dataMap.put("click", resultJsonObject.getString("click"));
                    dataMap.put("ctrStr", resultJsonObject.getString("ctrStr"));
                    dataMap.put("charge", resultJsonObject.getString("charge"));
                    dataMap.put("ecpc", resultJsonObject.getString("ecpc"));
                    dataMap.put("inshopItemColNum", resultJsonObject.getString("inshopItemColNum"));
                    dataMap.put("roi15", resultJsonObject.getString("roi15"));
                    dataMap.put("alipayInShopNum15", resultJsonObject.getString("alipayInShopNum15"));
                    dataMap.put("dirShopColNum", resultJsonObject.getString("dirShopColNum"));
                    dataMap.put("adPv", resultJsonObject.getString("adPv"));
                    dataMap.put("showCartNum15", resultJsonObject.getString("showCartNum15"));
                    dataMap.put("ecpm", resultJsonObject.getString("ecpm"));
                    dataMap.put("log_at", urlMap.get("log_at"));
                    dataMap.put("create_at", urlMap.get("create_at"));
                    dataMap.put("accountid", urlMap.get("accountid"));
                    dataMap.put("grapfrequency", urlMap.get("grapfrequency"));
                    spiderPromotionDataDao.addData(dataMap, "promotion_diamond_report");
                }else{
                    System.out.println("结果为空");
                }
            }
        } else {
            System.out.println("获取结果异常");
        }
    }

    @Override
    public Site getSite() {
        if (!"".equals(diamondHeaderBean.getUserAgent()) && diamondHeaderBean.getUserAgent() != null) {
            site.addHeader("user-agent", diamondHeaderBean.getUserAgent());
        }
        if (!"".equals(diamondHeaderBean.getAccept()) && diamondHeaderBean.getAccept() != null) {
            site.addHeader("accept", diamondHeaderBean.getAccept());
        }
        if (!"".equals(diamondHeaderBean.getAcceptLanguage()) && diamondHeaderBean.getAcceptLanguage() != null) {
            site.addHeader("accept-language", diamondHeaderBean.getAcceptLanguage());
        }
        if (!"".equals(diamondHeaderBean.getOrgin()) && diamondHeaderBean.getOrgin() != null) {
            site.addHeader("origin", diamondHeaderBean.getOrgin());
        }
        if (!"".equals(diamondHeaderBean.getReferer()) && diamondHeaderBean.getReferer() != null) {
            site.addHeader("referer", diamondHeaderBean.getReferer());
        }
        if (!"".equals(diamondHeaderBean.getCookie()) && diamondHeaderBean.getCookie() != null) {
            site.addHeader("cookie", diamondHeaderBean.getCookie());
        }
        return site;
    }
}
