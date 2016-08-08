package com.ecmoho.promotion.processor;

import com.alibaba.fastjson.JSON;
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
 * Created by Zhangjunrui on 2016/7/5.
 * 品销宝URL获取结果解析
 */
@Component("productSalesProcessor")
public class ProductSalesProcessor implements PageProcessor {
    private Site site = Site.me().setTimeOut(15000).setRetryTimes(3).setSleepTime(3000);
    @Resource(name = "productHeaderBean")
    private HeaderBean productHeaderBean;
    @Resource(name = "spiderDataDao")
    protected SpiderDataDao spiderPromotionDataDao;

    @Override
    public void process(Page page) {
        String resultStr = page.getJson().toString();
        System.out.println("resultStr:" + resultStr);
        Map<String, String> urlMap = productHeaderBean.getUrlMap();
        Map<String, String> dataMap = new HashMap<String, String>();
        String result = resultStr.substring(9, resultStr.length() - 2);
        System.out.println("result:" + result);
        JSONObject data = JSON.parseObject(result).getJSONObject("data");
        if (data != null && !"{}".equals(data.toString())) {
            JSONObject resultJsonObject = data.getJSONArray("result").getJSONObject(0);
            for (String key : resultJsonObject.keySet()) {
                dataMap.put(key, resultJsonObject.getString(key));
            }
            dataMap.put("log_at", urlMap.get("log_at"));
            dataMap.put("create_at", urlMap.get("create_at"));
            dataMap.put("accountid", urlMap.get("accountid"));
            dataMap.put("grapfrequency", urlMap.get("grapfrequency"));
            spiderPromotionDataDao.addData(dataMap, "promotion_product_report");
        } else {
            System.out.println("获取结果异常");
        }
    }

    @Override
    public Site getSite() {
        if (!"".equals(productHeaderBean.getUserAgent()) && productHeaderBean.getUserAgent() != null) {
            site.addHeader("user-agent", productHeaderBean.getUserAgent());
        }
        if (!"".equals(productHeaderBean.getAccept()) && productHeaderBean.getAccept() != null) {
            site.addHeader("accept", productHeaderBean.getAccept());
        }
        if (!"".equals(productHeaderBean.getAcceptLanguage()) && productHeaderBean.getAcceptLanguage() != null) {
            site.addHeader("accept-language", productHeaderBean.getAcceptLanguage());
        }
        if (!"".equals(productHeaderBean.getReferer()) && productHeaderBean.getReferer() != null) {
            site.addHeader("referer", productHeaderBean.getReferer());
        }
        if (!"".equals(productHeaderBean.getCookie()) && productHeaderBean.getCookie() != null) {
            site.addHeader("cookie", productHeaderBean.getCookie());
        }
        return site;
    }
}
