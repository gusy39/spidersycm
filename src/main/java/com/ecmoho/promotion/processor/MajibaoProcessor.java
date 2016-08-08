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
 * Created by meidejing on 2016/6/29.
 * 麻吉宝URL获取结果解析
 */
@Component("majibaoProcessor")
public class MajibaoProcessor implements PageProcessor{
    private Site site=Site.me().setTimeOut(15000).setRetryTimes(3).setSleepTime(3000);
    @Resource(name = "majibaoHeaderBean")
    private HeaderBean majibaoHeaderBean;
    @Resource(name="spiderDataDao")
    protected SpiderDataDao spiderPromotionDataDao;

    @Override
    public Site getSite() {

        if(!"".equals(majibaoHeaderBean.getUserAgent())&&majibaoHeaderBean.getUserAgent()!=null){
            site.addHeader("user-agent", majibaoHeaderBean.getUserAgent());
        }
        if(!"".equals(majibaoHeaderBean.getAccept())&&majibaoHeaderBean.getAccept()!=null){
            site.addHeader("accept", majibaoHeaderBean.getAccept());
        }
        if(!"".equals(majibaoHeaderBean.getAcceptLanguage())&&majibaoHeaderBean.getAcceptLanguage()!=null){
            site.addHeader("accept-language", majibaoHeaderBean.getAcceptLanguage());
        }
        if(!"".equals(majibaoHeaderBean.getOrgin())&&majibaoHeaderBean.getOrgin()!=null){
            site.addHeader("origin", majibaoHeaderBean.getOrgin());
        }
        if(!"".equals(majibaoHeaderBean.getReferer())&&majibaoHeaderBean.getReferer()!=null){
            site.addHeader("referer", majibaoHeaderBean.getReferer());
        }
        if(!"".equals(majibaoHeaderBean.getCookie())&&majibaoHeaderBean.getCookie()!=null){
            site.addHeader("cookie", majibaoHeaderBean.getCookie());
        }
        return site;
    }
    @Override
    public void process(Page page) {
//        System.out.println(page.getJson());
        String resultStr=page.getJson().toString();
        System.out.println("resultStr:"+resultStr);
        Map<String, String> urlMap = majibaoHeaderBean.getUrlMap();
        Map<String,String> dataMap=new HashMap<String,String>();
        String data = JSON.parseObject(resultStr).getString("data");
        if(data!=null &&!"{}".equals(data.toString())){
            JSONObject resultJsonObject =JSON.parseObject(resultStr).getJSONObject("data").getJSONObject("sum");
            dataMap.put("listpv",resultJsonObject.getString("listPV"));
            dataMap.put("detailuv",resultJsonObject.getString("detailUV"));
            dataMap.put("clickdetailuv",resultJsonObject.getString("clickDetailUV"));
            dataMap.put("shopcollect",resultJsonObject.getString("shopCollect"));
            dataMap.put("itemcart",resultJsonObject.getString("itemCart"));
            dataMap.put("cpafinish",resultJsonObject.getString("cpaFinish"));
            dataMap.put("cpacost",resultJsonObject.getString("cpaCost"));
            dataMap.put("log_at",urlMap.get("log_at"));
            dataMap.put("grapfrequency",urlMap.get("grapfrequency"));
            dataMap.put("create_at",urlMap.get("create_at"));
            dataMap.put("accountid",urlMap.get("accountid"));
            spiderPromotionDataDao.addData(dataMap,"promotion_majibao_report");
        }else{
            System.out.println("结果异常");
        }
    }

}
